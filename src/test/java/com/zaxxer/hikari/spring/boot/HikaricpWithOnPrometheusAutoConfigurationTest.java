package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.prometheus.PrometheusMetricsTrackerFactory;

import io.prometheus.client.CollectorRegistry;

/**
 * Tests for {@link HikaricpWithOnPrometheusAutoConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HikaricpWithOnPrometheusAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withBean(HikariDataSource.class, () -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:prometheustest;DB_CLOSE_DELAY=-1");
            ds.setUsername("sa");
            ds.setPassword("");
            ds.setConnectionTestQuery("SELECT 1");
            ds.setInitializationFailTimeout(1);
            return ds;
        })
        .withBean(CollectorRegistry.class, CollectorRegistry::new)
        .withConfiguration(AutoConfigurations.of(HikaricpWithOnPrometheusAutoConfiguration.class))
        .withPropertyValues(
            "spring.datasource.hikari.metric.type=prometheus"
        );

    @Test
    void metricsTrackerFactoryBeanCreated() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MetricsTrackerFactory.class);
            assertThat(context.getBean(MetricsTrackerFactory.class))
                .isInstanceOf(PrometheusMetricsTrackerFactory.class);
        });
    }

    @Test
    void beansNotCreatedWhenTypeMismatch() {
        new ApplicationContextRunner()
            .withBean(HikariDataSource.class, () -> {
                HikariDataSource ds = new HikariDataSource();
                ds.setDriverClassName("org.h2.Driver");
                ds.setJdbcUrl("jdbc:h2:mem:prometheusmismatch;DB_CLOSE_DELAY=-1");
                ds.setUsername("sa");
                ds.setPassword("");
                ds.setConnectionTestQuery("SELECT 1");
                ds.setInitializationFailTimeout(1);
                return ds;
            })
            .withBean(CollectorRegistry.class, CollectorRegistry::new)
            .withConfiguration(AutoConfigurations.of(HikaricpWithOnPrometheusAutoConfiguration.class))
            .withPropertyValues(
                "spring.datasource.hikari.metric.type=dropwizard"
            )
            .run(context -> {
                assertThat(context).doesNotHaveBean(MetricsTrackerFactory.class);
            });
    }
}
