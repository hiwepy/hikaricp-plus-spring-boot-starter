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

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.ReflectionUtils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.HikaricpProperties;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;


@SuppressWarnings("unchecked")
public class DynamicDataSource extends AbstractRoutingDataSource {

	protected static Field targetDataSourcesField = ReflectionUtils.findField(DynamicDataSource.class,
			"targetDataSources");
	protected static Field resolvedDataSourcesField = ReflectionUtils.findField(DynamicDataSource.class,
			"resolvedDataSources");

	@Override
	protected Object determineCurrentLookupKey() {
		return DataSourceContextHolder.getDatabaseName();
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

	public void setTargetDataSource(DataSourceProperties properties, HikaricpProperties HikariProperties,
			String name, String url, String username, String password) {

		// 动态创建Hikari数据源
		HikariDataSource targetDataSource = HikariDataSourceUtils.createDataSource(properties, HikariProperties, url, username, password);

		getTargetDataSources().put(name, targetDataSource);

		Object lookupKey = resolveSpecifiedLookupKey(name);
		DataSource dataSource = resolveSpecifiedDataSource(targetDataSource);
		getResolvedDataSources().put(lookupKey, dataSource);
		
	}
	
	/**
	 * 
	 * @description	： 为动态数据源设置新的数据源目标源集
	 * @author 		： 万大龙（743）
	 * @date 		：2017年10月17日 下午2:34:21
	 * @param properties
	 * @param HikariProperties
	 * @param dsSetting
	 */
	public void setTargetDataSource(DataSourceProperties properties, HikaricpProperties HikariProperties,
			DynamicDataSourceSetting dsSetting) {
		this.setTargetDataSource(properties, HikariProperties, HikariProperties.getName(), properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

	/**
	 * @description	： 为动态数据源设置新的数据源目标源集合
	 * @author 		： 万大龙（743）
	 * @date 		：2017年10月17日 下午2:33:39
	 * @param targetDataSources
	 */
	public void setNewTargetDataSources(Map<Object, Object> targetDataSources) {

		getTargetDataSources().putAll(targetDataSources);

		for (Map.Entry<Object, Object> entry : targetDataSources.entrySet()) {
			Object lookupKey = resolveSpecifiedLookupKey(entry.getKey());
			DataSource dataSource = resolveSpecifiedDataSource(entry.getValue());
			getResolvedDataSources().put(lookupKey, dataSource);
		}

	}

	public void removeTargetDataSource(String name) {

		getTargetDataSources().remove(name);
		Object lookupKey = resolveSpecifiedLookupKey(name);
		getResolvedDataSources().remove(lookupKey);
		
	}
}
