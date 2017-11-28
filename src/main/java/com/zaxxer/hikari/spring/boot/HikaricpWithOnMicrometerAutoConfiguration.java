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
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import com.zaxxer.hikari.spring.boot.util.MicrometerSystemClock;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.dropwizard.DropwizardMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;

/**
 * 
 * @className	： HikaricpWithOnMicrometerAutoConfiguration
 * @description	：  基于Micrometer监控组件的HikariDataSource监控
 * @author 		： <a href="https://github.com/vindell">vindell</a>
 * @date		： 2017年11月27日 下午9:50:05
 * @version 	V1.0
 */
@Configuration
@ConditionalOnBean( HikariDataSource.class )
@ConditionalOnClass({ HikariDataSource.class, MetricRegistry.class, MeterRegistry.class })
@ConditionalOnProperty(prefix = HikaricpWithMetricProperties.PREFIX, value = "type", havingValue = "micrometer", matchIfMissing = false)
@EnableConfigurationProperties({ HikaricpWithMetricProperties.class })
public class HikaricpWithOnMicrometerAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public Clock clock() {
		return MicrometerSystemClock.instance();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public HierarchicalNameMapper nameMapper() {
		return HierarchicalNameMapper.DEFAULT;
	}
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MeterRegistry meterRegistry(HierarchicalNameMapper nameMapper, Clock clock) {
		DropwizardMeterRegistry meterRegistry = new DropwizardMeterRegistry(nameMapper, clock);
		return meterRegistry;
	}
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MetricsTrackerFactory duridFilterRegistrationBean(MeterRegistry registry) {
		MetricsTrackerFactory metricsTrackerFactory = new MicrometerMetricsTrackerFactory(registry);
		return metricsTrackerFactory;
	}
	

}
