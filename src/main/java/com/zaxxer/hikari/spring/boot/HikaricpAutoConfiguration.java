package com.zaxxer.hikari.spring.boot;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.util.CollectionUtils;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.DynamicRoutingDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;

@Configuration
@ConditionalOnClass(com.zaxxer.hikari.HikariDataSource.class)
@ConditionalOnProperty(prefix = HikaricpProperties.PREFIX, name = "enabled", havingValue = "true", matchIfMissing = true)
@EnableConfigurationProperties({ HikaricpProperties.class, DataSourceProperties.class })
@AutoConfigureBefore(name = {
	"org.springframework.boot.jdbc.autoconfigure.DataSourceAutoConfiguration",
	"com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration"
})
/**
 * <p>Auto-configuration for Hikaricp integration with dynamic data source routing support.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HikaricpAutoConfiguration {
	
	/**
	 * Creates and configures the primary HikariDataSource bean
	 * @author <a href="https://github.com/loong10k">Loong Wan</a>
	 * @param basicProperties {@link DataSourceProperties} the configuration properties
	 * @param hikariProperties {@link HikaricpProperties} the configuration properties
	 * @return {@link HikariDataSource} the data source
	 */
	@Bean
	@Primary
	public DataSource dataSource(DataSourceProperties basicProperties, HikaricpProperties hikariProperties) {
		
		// Dynamic data source
		if(hikariProperties.isRoutable()) {
			
			Map<Object, Object> targetDataSources = new HashMap<Object, Object>();
			
			//Dynamic data source information based on configuration
			if (!CollectionUtils.isEmpty(hikariProperties.getSlaves())) {
				for (HikaricpDataSourceProperties slaveProperties : hikariProperties.getSlaves()) {
					// Dynamically creates a HikariDataSource
					HikariDataSource slaveDataSource = HikariDataSourceUtils.createDataSource(configureProperties(basicProperties, slaveProperties));
					targetDataSources.put(slaveProperties.getName(), slaveDataSource);
				}
			}
			
			// Dynamic data source support
			DynamicRoutingDataSource dataSource = new DynamicRoutingDataSource();
			dataSource.setTargetDataSources(targetDataSources);
			
			// Default data source
			HikariDataSource masterDataSource = HikariDataSourceUtils.createDataSource(configureProperties(basicProperties, hikariProperties));
			dataSource.setDefaultTargetDataSource(masterDataSource);
				
			return dataSource;
		}
		
		return HikariDataSourceUtils.createDataSource(configureProperties(basicProperties, hikariProperties));
			
	}
	/**
	 * <p>Configure properties.</p>
	 * @param basicProperties the basic properties
	 * @param hikariProperties the hikari properties
	 * @return the hikaricp data source properties
	 */
	
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
