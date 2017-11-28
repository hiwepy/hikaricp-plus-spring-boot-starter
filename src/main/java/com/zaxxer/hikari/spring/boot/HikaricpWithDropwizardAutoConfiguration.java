package com.zaxxer.hikari.spring.boot;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;

/**
 * 
 * @className	： HikaricpWithDropwizardAutoConfiguration
 * @description	： 基于Dropwizard监控组件的HikariDataSource监控
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月27日 下午9:50:09
 * @version 	V1.0
 */
@Configuration
@ConditionalOnBean( HikariDataSource.class )
@ConditionalOnClass({ HikariDataSource.class, MetricRegistry.class})
@ConditionalOnProperty(prefix = HikaricpWithMetricProperties.PREFIX, value = "type", havingValue = "dropwizard", matchIfMissing = false)
@EnableConfigurationProperties({ HikaricpWithMetricProperties.class })
public class HikaricpWithDropwizardAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean
	public MetricRegistry registry() {
		return new MetricRegistry();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MetricsTrackerFactory duridFilterRegistrationBean(MetricRegistry registry) {
		MetricsTrackerFactory metricsTrackerFactory = new CodahaleMetricsTrackerFactory(registry);
		return metricsTrackerFactory;
	}
	

}
