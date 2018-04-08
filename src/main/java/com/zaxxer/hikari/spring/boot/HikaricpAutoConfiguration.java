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
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
@Configuration
@ConditionalOnClass(com.zaxxer.hikari.HikariDataSource.class)
@ConditionalOnProperty("spring.datasource.hikari")
@EnableConfigurationProperties({ HikaricpProperties.class, DataSourceProperties.class })
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class HikaricpAutoConfiguration {
	
	/**
	 * 配置HikariDataSource
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @param properties {@link DataSourceProperties} 参数对象
	 * @param hikariProperties {@link HikaricpProperties} 参数对象
	 * @return {@link HikariDataSource} 数据源
	 */
	@Bean(DataSourceContextHolder.DEFAULT_DATASOURCE)
	@ConditionalOnMissingBean(AbstractRoutingDataSource.class)
	@Primary
	public HikariDataSource HikariDataSource(DataSourceProperties properties, HikaricpProperties hikariProperties) {
		return HikariDataSourceUtils.createDataSource(properties, hikariProperties, properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

}
