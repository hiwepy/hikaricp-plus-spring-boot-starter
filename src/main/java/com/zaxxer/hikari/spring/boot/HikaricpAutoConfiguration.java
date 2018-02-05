package com.zaxxer.hikari.spring.boot;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.DataSourceContextHolder;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;

/**
 * 
 * @className	： HikaricpAutoConfiguration
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月27日 下午8:54:30
 * @version 	V1.0
 */
@Configuration
@ConditionalOnClass(com.zaxxer.hikari.HikariDataSource.class)
@ConditionalOnProperty("spring.datasource.hikari")
@EnableConfigurationProperties({ HikaricpProperties.class, DataSourceProperties.class })
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class HikaricpAutoConfiguration {
	
	/**
	 * 
	 * @description	：  配置HikariDataSource
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @date 		：2017年11月27日 下午9:51:27
	 * @param properties
	 * @param HikariProperties
	 * @return
	 */
	@Bean(DataSourceContextHolder.DEFAULT_DATASOURCE)
	@ConditionalOnMissingBean(AbstractRoutingDataSource.class)
	@Primary
	public HikariDataSource HikariDataSource(DataSourceProperties properties, HikaricpProperties HikariProperties) {
		return HikariDataSourceUtils.createDataSource(properties, HikariProperties, properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

}
