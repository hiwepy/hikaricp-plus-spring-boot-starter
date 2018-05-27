package com.zaxxer.hikari.spring.boot;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

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
import com.zaxxer.hikari.spring.boot.ds.DynamicDataSourceSetting;
import com.zaxxer.hikari.spring.boot.ds.DynamicRoutingDataSource;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;

/**
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 */
@Configuration
@ConditionalOnClass(com.zaxxer.hikari.HikariDataSource.class)
@ConditionalOnProperty(prefix = HikaricpProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ HikaricpProperties.class, DataSourceProperties.class })
@AutoConfigureBefore(name = {
	"org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
	"com.baomidou.mybatisplus.spring.boot.starter.MybatisPlusAutoConfiguration"
})
public class HikaricpAutoConfiguration {
	
	/**
	 * 配置HikariDataSource
	 * @author 		： <a href="https://github.com/vindell">vindell</a>
	 * @param properties {@link DataSourceProperties} 参数对象
	 * @param hikariProperties {@link HikaricpProperties} 参数对象
	 * @return {@link HikariDataSource} 数据源
	 */
	@Bean
	@Primary
	public DataSource dataSource(DataSourceProperties properties, HikaricpProperties hikariProperties) {
		
		// 启用动态数据源
		if(hikariProperties.isDynamic()) {
			
			Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
			
			//基于配置文件的动态数据源信息
			if (!CollectionUtils.isEmpty(hikariProperties.getSlaves())) {
				for (DynamicDataSourceSetting slave : hikariProperties.getSlaves()) {
					// 动态创建Hikari数据源
					targetDataSources.put(slave.getName(), HikariDataSourceUtils.createDataSource(properties, hikariProperties, slave.getUrl(), slave.getUsername(), slave.getPassword()));
				}
			}

			// 动态数据源支持
			DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
			dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
			
			// 默认的数据源
			HikariDataSource masterDataSource = HikariDataSourceUtils.createDataSource(properties, hikariProperties, properties.determineUrl(),
					properties.determineUsername(), properties.determinePassword());
			
			dataSource.setDefaultTargetDataSource(masterDataSource);// 默认的datasource设置为myTestDbDataSource
			
			return dataSource;
			
		}
		
		return HikariDataSourceUtils.createDataSource(properties, hikariProperties, properties.determineUrl(),
				properties.determineUsername(), properties.determinePassword());
	}

}
