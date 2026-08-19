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


@Configuration
@ConditionalOnBean( HikariDataSource.class )
@ConditionalOnClass({ HikariDataSource.class, MetricRegistry.class})
@ConditionalOnProperty(prefix = HikaricpWithMetricProperties.PREFIX, value = "type", havingValue = "dropwizard", matchIfMissing = false)
@EnableConfigurationProperties({ HikaricpWithMetricProperties.class })
/**
 * Auto-configuration for HikaricpWithDropwizard integration.
 * <p>Registers the necessary beans when the feature is enabled.</p>
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 * @since 1.0.0
 */
public class HikaricpWithDropwizardAutoConfiguration {
	/**
	 * <p>Registry.</p>
	 * @return the metric registry
	 */
	
	@Bean
	@ConditionalOnMissingBean
	public MetricRegistry registry() {
		return new MetricRegistry();
	}
	/**
	 * <p>Durid filter registration bean.</p>
	 * @param registry the registry
	 * @return the metrics tracker factory
	 */
	
	@Bean
	@ConditionalOnMissingBean(value = MetricsTrackerFactory.class)
	public MetricsTrackerFactory duridFilterRegistrationBean(MetricRegistry registry) {
		MetricsTrackerFactory metricsTrackerFactory = new CodahaleMetricsTrackerFactory(registry);
		return metricsTrackerFactory;
	}
	

}
