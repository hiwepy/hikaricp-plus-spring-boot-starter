package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.dropwizard.CodahaleMetricsTrackerFactory;

/**
 * Tests for {@link HikaricpWithDropwizardAutoConfiguration}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class HikaricpWithDropwizardAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withBean(HikariDataSource.class, () -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:dropwizardtest;DB_CLOSE_DELAY=-1");
            ds.setUsername("sa");
            ds.setPassword("");
            ds.setConnectionTestQuery("SELECT 1");
            ds.setInitializationFailTimeout(1);
            return ds;
        })
        .withConfiguration(AutoConfigurations.of(HikaricpWithDropwizardAutoConfiguration.class))
        .withPropertyValues(
            "spring.datasource.hikari.metric.type=dropwizard"
        );

    @Test
    void metricRegistryBeanCreated() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MetricRegistry.class);
        });
    }

    @Test
    void metricsTrackerFactoryBeanCreated() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MetricsTrackerFactory.class);
            assertThat(context.getBean(MetricsTrackerFactory.class))
                .isInstanceOf(CodahaleMetricsTrackerFactory.class);
        });
    }

    @Test
    void beansNotCreatedWhenTypeMismatch() {
        new ApplicationContextRunner()
            .withBean(HikariDataSource.class, () -> {
                HikariDataSource ds = new HikariDataSource();
                ds.setDriverClassName("org.h2.Driver");
                ds.setJdbcUrl("jdbc:h2:mem:dropwizardmismatch;DB_CLOSE_DELAY=-1");
                ds.setUsername("sa");
                ds.setPassword("");
                ds.setConnectionTestQuery("SELECT 1");
                ds.setInitializationFailTimeout(1);
                return ds;
            })
            .withConfiguration(AutoConfigurations.of(HikaricpWithDropwizardAutoConfiguration.class))
            .withPropertyValues(
                "spring.datasource.hikari.metric.type=micrometer"
            )
            .run(context -> {
                assertThat(context).doesNotHaveBean(MetricsTrackerFactory.class);
            });
    }
}
