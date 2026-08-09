package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.metrics.micrometer.MicrometerMetricsTrackerFactory;
import com.zaxxer.hikari.spring.boot.util.MicrometerSystemClock;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;

/**
 * Tests for {@link HikaricpWithOnMicrometerAutoConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HikaricpWithOnMicrometerAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withBean(HikariDataSource.class, () -> {
            HikariDataSource ds = new HikariDataSource();
            ds.setDriverClassName("org.h2.Driver");
            ds.setJdbcUrl("jdbc:h2:mem:micrometertest;DB_CLOSE_DELAY=-1");
            ds.setUsername("sa");
            ds.setPassword("");
            ds.setConnectionTestQuery("SELECT 1");
            ds.setInitializationFailTimeout(1);
            return ds;
        })
        .withConfiguration(AutoConfigurations.of(HikaricpWithOnMicrometerAutoConfiguration.class))
        .withPropertyValues(
            "spring.datasource.hikari.metric.type=micrometer"
        );

    @Test
    void clockBeanCreated() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(Clock.class);
            assertThat(context.getBean(Clock.class)).isInstanceOf(MicrometerSystemClock.class);
        });
    }

    @Test
    void meterRegistryBeanCreated() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MeterRegistry.class);
        });
    }

    @Test
    void metricsTrackerFactoryBeanCreated() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(MetricsTrackerFactory.class);
            assertThat(context.getBean(MetricsTrackerFactory.class))
                .isInstanceOf(MicrometerMetricsTrackerFactory.class);
        });
    }

    @Test
    void beansNotCreatedWhenTypeMismatch() {
        new ApplicationContextRunner()
            .withBean(HikariDataSource.class, () -> {
                HikariDataSource ds = new HikariDataSource();
                ds.setDriverClassName("org.h2.Driver");
                ds.setJdbcUrl("jdbc:h2:mem:micrometermismatch;DB_CLOSE_DELAY=-1");
                ds.setUsername("sa");
                ds.setPassword("");
                ds.setConnectionTestQuery("SELECT 1");
                ds.setInitializationFailTimeout(1);
                return ds;
            })
            .withConfiguration(AutoConfigurations.of(HikaricpWithOnMicrometerAutoConfiguration.class))
            .withPropertyValues(
                "spring.datasource.hikari.metric.type=dropwizard"
            )
            .run(context -> {
                assertThat(context).doesNotHaveBean(MetricsTrackerFactory.class);
            });
    }
}
