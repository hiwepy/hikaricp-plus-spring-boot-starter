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
import com.zaxxer.hikari.metrics.prometheus.PrometheusMetricsTrackerFactory;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * 
 * @className	： HikaricpWithOnPrometheusAutoConfiguration
 * @description	： TODO(描述这个类的作用)
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月27日 下午9:50:01
 * @version 	V1.0
 */
@Configuration
@ConditionalOnBean( HikariDataSource.class )
@ConditionalOnClass({ HikariDataSource.class, MetricRegistry.class, MeterRegistry.class })
@ConditionalOnProperty(prefix = HikaricpWithMetricProperties.PREFIX, value = "enabled", havingValue = "true", matchIfMissing = false)
@EnableConfigurationProperties({ HikaricpWithMetricProperties.class })
public class HikaricpWithOnPrometheusAutoConfiguration {
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MetricsTrackerFactory duridFilterRegistrationBean() {
		MetricsTrackerFactory metricsTrackerFactory = new PrometheusMetricsTrackerFactory();
		return metricsTrackerFactory;
	}
	

}
