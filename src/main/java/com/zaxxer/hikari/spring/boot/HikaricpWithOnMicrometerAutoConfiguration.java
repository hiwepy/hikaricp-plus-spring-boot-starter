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


@Configuration
@ConditionalOnBean( HikariDataSource.class )
@ConditionalOnClass({ HikariDataSource.class, MetricRegistry.class, MeterRegistry.class })
@ConditionalOnProperty(prefix = HikaricpWithMetricProperties.PREFIX, value = "type", havingValue = "micrometer", matchIfMissing = false)
@EnableConfigurationProperties({ HikaricpWithMetricProperties.class })
/**
 * Auto-configuration for HikaricpWithOnMicrometer integration.
 * <p>Registers the necessary beans when the feature is enabled.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HikaricpWithOnMicrometerAutoConfiguration implements ApplicationContextAware {

	private ApplicationContext applicationContext;
	/**
	 * <p>Clock.</p>
	 * @return the clock
	 */
	
	@Bean
	@ConditionalOnMissingBean
	public Clock clock() {
		return MicrometerSystemClock.instance();
	}
	/**
	 * <p>Name mapper.</p>
	 * @return the hierarchical name mapper
	 */
	
	@Bean
	@ConditionalOnMissingBean
	public HierarchicalNameMapper nameMapper() {
		return HierarchicalNameMapper.DEFAULT;
	}
	/**
	 * <p>Meter registry.</p>
	 * @param clock the clock
	 * @return the meter registry
	 */
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MeterRegistry meterRegistry(Clock clock) {
		
		Map<String, MeterRegistry> beansOfType = getApplicationContext().getBeansOfType(MeterRegistry.class);
		if (!ObjectUtils.isEmpty(beansOfType)) {
			return new CompositeMeterRegistry( clock, beansOfType.values());
		}
		
		return new SimpleMeterRegistry();
	}
	/**
	 * <p>Durid filter registration bean.</p>
	 * @param registry the registry
	 * @return the metrics tracker factory
	 */
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MetricsTrackerFactory duridFilterRegistrationBean(MeterRegistry registry) {
		MetricsTrackerFactory metricsTrackerFactory = new MicrometerMetricsTrackerFactory(registry);
		return metricsTrackerFactory;
	}
	/** Sets the application context. */
	
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
	/** Gets the application context. */

	public ApplicationContext getApplicationContext() {
		return applicationContext;
	}

}
