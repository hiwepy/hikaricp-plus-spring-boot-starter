/*
 * Copyright (c) 2017, vindell (https://github.com/vindell).
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.zaxxer.hikari.spring.boot.ds;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;

@SuppressWarnings("unchecked")
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

	/**
     * 用于在维护数据源时保证不会被其他线程修改
     */
    private static Lock lock = new ReentrantLock();
	protected final Logger logger = LoggerFactory.getLogger(getClass());
	protected static Field targetDataSourcesField = ReflectionUtils.findField(DynamicRoutingDataSource.class,
			"targetDataSources");
	protected static Field resolvedDataSourcesField = ReflectionUtils.findField(DynamicRoutingDataSource.class,
			"resolvedDataSources");
	
	@Override
	protected Object determineCurrentLookupKey() {
		 logger.info("Current DataSource is [{}]", DataSourceRoutingKeyHolder.getDataSourceKey());
		return DataSourceRoutingKeyHolder.getDataSourceKey();
	}
	
	public Map<Object, Object> getTargetDataSources() {
		targetDataSourcesField.setAccessible(true);
		Object targetDataSources = ReflectionUtils.getField(targetDataSourcesField, this);
		targetDataSourcesField.setAccessible(false);
		return (Map<Object, Object>) targetDataSources;
	}

	public Map<Object, DataSource> getResolvedDataSources() {
		resolvedDataSourcesField.setAccessible(true);
		Object resolvedDataSources = ReflectionUtils.getField(resolvedDataSourcesField, this);
		resolvedDataSourcesField.setAccessible(false);
		return (Map<Object, DataSource>) resolvedDataSources;
	}
	
	public void setTargetDataSource(String name, DataSourceProperties basicProperties, HikaricpDataSourceProperties hikariProperties) {

		lock.lock();
		
		try {
			
			//if not found prefix 'spring.datasource.hikari' jdbc properties ,'spring.datasource' prefix jdbc properties will be used.
	        if (hikariProperties.getUsername() == null) {
	        	hikariProperties.setUsername(basicProperties.determineUsername());
	        }
	        if (hikariProperties.getPassword() == null) {
	        	hikariProperties.setPassword(basicProperties.determinePassword());
	        }
	        if (hikariProperties.getJdbcUrl() == null) {
	        	hikariProperties.setJdbcUrl(basicProperties.determineUrl());
	        }
	        if(hikariProperties.getDriverClassName() == null){
	        	hikariProperties.setDriverClassName(basicProperties.determineDriverClassName());
	        }
			
			// 动态创建Hikari数据源
			HikariDataSource targetDataSource = HikariDataSourceUtils.createDataSource(hikariProperties);

			getTargetDataSources().put(name, targetDataSource);
			
			// reset resolvedDataSources
			this.afterPropertiesSet();
			
		} finally {
            lock.unlock();
        }
		
	}
	
	public void setTargetDataSource(DataSourceProperties properties, HikaricpDataSourceProperties hikariProperties,
			DynamicDataSourceSetting dsSetting) {
		this.setTargetDataSource(hikariProperties.getName(), properties, hikariProperties);
	}

	public void setNewTargetDataSources(Map<Object, Object> targetDataSources) {
		
		lock.lock();
		
		try {
			
			getTargetDataSources().putAll(targetDataSources);
			// reset resolvedDataSources
			this.afterPropertiesSet();
			
		} finally {
	        lock.unlock();
	    }
	}

	public void removeTargetDataSource(String name) {
		
		lock.lock();
		
		try {
			
			getTargetDataSources().remove(name);
			// reset resolvedDataSources
			this.afterPropertiesSet();
		
		} finally {
	        lock.unlock();
	    }
	}
	
	@Override
	public void afterPropertiesSet() {
		super.afterPropertiesSet();
		getTargetDataSources().forEach((key, value) -> {
			Object lookupKey = resolveSpecifiedLookupKey(key);
			DataSourceRoutingKeyHolder.dataSourceKeys.add(lookupKey);
		});
	}
	
}
