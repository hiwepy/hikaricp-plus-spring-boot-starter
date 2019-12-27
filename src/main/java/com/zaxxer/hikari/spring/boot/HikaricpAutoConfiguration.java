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
import com.zaxxer.hikari.spring.boot.ds.DynamicRoutingDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;

/**
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
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
	 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
	 * @param basicProperties {@link DataSourceProperties} 参数对象
	 * @param hikariProperties {@link HikaricpProperties} 参数对象
	 * @return {@link HikariDataSource} 数据源
	 */
	@Bean
	@Primary
	public DataSource dataSource(DataSourceProperties basicProperties, HikaricpProperties hikariProperties) {
		
		// 动态数据源
		if(hikariProperties.isRoutable()) {
			
			Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
			
			//基于配置文件的动态数据源信息
			if (!CollectionUtils.isEmpty(hikariProperties.getSlaves())) {
				for (HikaricpDataSourceProperties slaveProperties : hikariProperties.getSlaves()) {
					// 动态创建Hikari数据源
					HikariDataSource slaveDataSource = HikariDataSourceUtils.createDataSource(configureProperties(basicProperties, hikariProperties));
					targetDataSources.put(slaveProperties.getName(), slaveDataSource);
				}
			}
			
			// 动态数据源支持
			DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
			dataSource.setTargetDataSources(targetDataSources);// 该方法是AbstractRoutingDataSource的方法
			
			// 默认的数据源
			HikariDataSource masterDataSource = HikariDataSourceUtils.createDataSource(configureProperties(basicProperties, hikariProperties));
			dataSource.setDefaultTargetDataSource(masterDataSource);// 默认的datasource设置为myTestDbDataSource
				
			return dataSource;
		}
		
		return HikariDataSourceUtils.createDataSource(configureProperties(basicProperties, hikariProperties));
			
	}
	
	private HikaricpDataSourceProperties configureProperties(DataSourceProperties basicProperties, HikaricpDataSourceProperties hikariProperties) {
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
        return hikariProperties;
	}

}
