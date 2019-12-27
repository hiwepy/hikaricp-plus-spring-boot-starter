package com.zaxxer.hikari.spring.boot;

import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ObjectUtils;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import com.zaxxer.hikari.spring.boot.util.MicrometerSystemClock;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.composite.CompositeMeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.micrometer.core.instrument.util.HierarchicalNameMapper;

/**
 * 基于Micrometer监控组件的HikariDataSource监控
 * @author 		： <a href="https://github.com/hiwepy">hiwepy</a>
 */
@Configuration
@ConditionalOnBean( HikariDataSource.class )
@ConditionalOnClass({ HikariDataSource.class, MetricRegistry.class, MeterRegistry.class })
@ConditionalOnProperty(prefix = HikaricpWithMetricProperties.PREFIX, value = "type", havingValue = "micrometer", matchIfMissing = false)
@EnableConfigurationProperties({ HikaricpWithMetricProperties.class })
public class HikaricpWithOnMicrometerAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	
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
	public MeterRegistry meterRegistry(Clock clock) {
		
		Map<String, MeterRegistry> beansOfType = getApplicationContext().getBeansOfType(MeterRegistry.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			return new CompositeMeterRegistry( clock, beansOfType.values());
		}
		
		return new SimpleMeterRegistry();
	}
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MetricsTrackerFactory duridFilterRegistrationBean(MeterRegistry registry) {
		MetricsTrackerFactory metricsTrackerFactory = new MicrometerMetricsTrackerFactory(registry);
		return metricsTrackerFactory;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
