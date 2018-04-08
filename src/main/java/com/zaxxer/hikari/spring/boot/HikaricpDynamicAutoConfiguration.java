package com.zaxxer.hikari.spring.boot;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.DataSourceContextHolder;
import com.zaxxer.hikari.spring.boot.ds.DynamicDataSource;
import com.zaxxer.hikari.spring.boot.ds.DynamicDataSourceSetting;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;


@Configuration
@ConditionalOnClass(com.zaxxer.hikari.HikariDataSource.class)
@ConditionalOnProperty("spring.datasource.hikari")
@EnableConfigurationProperties({ HikaricpDynamicProperties.class, HikaricpProperties.class, DataSourceProperties.class })
@AutoConfigureBefore(HikaricpAutoConfiguration.class)
public class HikaricpDynamicAutoConfiguration {

	@Autowired(required = false) 
	@Qualifier("targetDataSources") 
	protected Map<Object, Object> targetDataSources;
	
	@Bean("targetDataSources")
	@ConditionalOnProperty(prefix = HikaricpDynamicProperties.PREFIX, value = "enabled", havingValue = "true")
	public Map<Object, Object> targetDataSources() {
		return new HashMap<Object, Object>();
	}
	
	/*
	 * @Primary 该注解表示在同一个接口有多个实现类可以注入的时候，默认选择哪一个，而不是让@autowire注解报错
	 * @Qualifier 根据名称进行注入，通常是在具有相同的多个类型的实例的一个注入（例如有多个DataSource类型的实例）
	 */
	@Bean(DataSourceContextHolder.DEFAULT_DATASOURCE)
	@ConditionalOnProperty(prefix = HikaricpDynamicProperties.PREFIX, value = "enabled", havingValue = "true")
	@Primary
	public DynamicDataSource dynamicDataSource(DataSourceProperties properties, HikaricpProperties HikariProperties,
			HikaricpDynamicProperties dynamicProperties) {

		if(targetDataSources == null) {
			targetDataSources = new HashMap<Object, Object>();
		}
		
		//基于配置文件的动态数据源信息
		if (!CollectionUtils.isEmpty(dynamicProperties.getDataSourceList())) {
			for (DynamicDataSourceSetting dsSetting : dynamicProperties.getDataSourceList()) {
				// 动态创建Hikari数据源
				targetDataSources.put(dsSetting.getName(), HikariDataSourceUtils.createDataSource(properties, HikariProperties, dsSetting.getUrl(), dsSetting.getUsername(), dsSetting.getPassword()));
			}
		}

		// 动态数据源支持
		DynamicDataSource dataSource = new DynamicDataSource();
		dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
		
		// 默认的数据源
		HikariDataSource defaultTargetDataSource = HikariDataSourceUtils.createDataSource(properties, HikariProperties, properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
		
		dataSource.setDefaultTargetDataSource(defaultTargetDataSource);// 默认的datasource设置为myTestDbDataSource
		
		return dataSource;
	}

	
}
