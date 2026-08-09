package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import javax.sql.DataSource;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;

/**
 * Tests for {@link HikaricpAutoConfiguration}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HikaricpAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
        .withConfiguration(AutoConfigurations.of(HikaricpAutoConfiguration.class))
        .withPropertyValues(
            "spring.datasource.driver-class-name=org.h2.Driver",
            "spring.datasource.url=jdbc:h2:mem:testconfig;DB_CLOSE_DELAY=-1",
            "spring.datasource.username=sa",
            "spring.datasource.password=",
            "spring.datasource.hikari.enabled=true",
            "spring.datasource.hikari.connection-test-query=SELECT 1",
            "spring.datasource.hikari.initialization-fail-timeout=1"
        );

    @Test
    void dataSourceBeanCreatedWhenEnabled() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(DataSource.class);
            assertThat(context).hasSingleBean(HikariDataSource.class);
            DataSource ds = context.getBean(DataSource.class);
            assertThat(ds).isInstanceOf(HikariDataSource.class);
        });
    }

    @Test
    void dataSourceBeanNotCreatedWhenDisabled() {
        this.contextRunner
            .withPropertyValues("spring.datasource.hikari.enabled=false")
            .run(context -> {
                assertThat(context).doesNotHaveBean(DataSource.class);
            });
    }

    @Test
    void routableDataSourceCreatedWhenRoutableEnabled() {
        this.contextRunner
            .withPropertyValues("spring.datasource.hikari.routable=true")
            .run(context -> {
                assertThat(context).hasSingleBean(DataSource.class);
                DataSource ds = context.getBean(DataSource.class);
                assertThat(ds).isNotNull();
            });
    }

    @Test
    void configurationPropertiesAreBound() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(HikaricpProperties.class);
            HikaricpProperties props = context.getBean(HikaricpProperties.class);
            assertThat(props.isEnabled()).isTrue();
        });
    }

    @Test
    void dataSourcePropertiesAreBound() {
        this.contextRunner.run(context -> {
            assertThat(context).hasSingleBean(DataSourceProperties.class);
            DataSourceProperties props = context.getBean(DataSourceProperties.class);
            assertThat(props.getDriverClassName()).isEqualTo("org.h2.Driver");
        });
    }

    @Test
    void hikariDataSourceUsesConfiguredUrl() {
        this.contextRunner
            .withPropertyValues(
                "spring.datasource.hikari.max-pool-size=5",
                "spring.datasource.hikari.min-idle=2"
            )
            .run(context -> {
                HikariDataSource ds = context.getBean(HikariDataSource.class);
                assertThat(ds.getJdbcUrl()).isEqualTo("jdbc:h2:mem:testconfig;DB_CLOSE_DELAY=-1");
                assertThat(ds.getMaximumPoolSize()).isEqualTo(5);
                assertThat(ds.getMinimumIdle()).isEqualTo(2);
            });
    }

    @Test
    void hikariDataSourceWithCustomProperties() {
        this.contextRunner
            .withPropertyValues(
                "spring.datasource.hikari.connection-timeout=10000",
                "spring.datasource.hikari.idle-timeout=300000",
                "spring.datasource.hikari.max-lifetime=600000",
                "spring.datasource.hikari.auto-commit=false",
                "spring.datasource.hikari.read-only=false"
            )
            .run(context -> {
                HikariDataSource ds = context.getBean(HikariDataSource.class);
                assertThat(ds.getConnectionTimeout()).isEqualTo(10000);
                assertThat(ds.getIdleTimeout()).isEqualTo(300000);
                assertThat(ds.getMaxLifetime()).isEqualTo(600000);
                assertThat(ds.isAutoCommit()).isFalse();
                assertThat(ds.isReadOnly()).isFalse();
            });
    }

    @Test
    void hikariDataSourceWithSchemaAndCatalog() {
        this.contextRunner
            .withPropertyValues(
                "spring.datasource.hikari.schema=PUBLIC"
            )
            .run(context -> {
                HikariDataSource ds = context.getBean(HikariDataSource.class);
                assertThat(ds.getSchema()).isEqualTo("PUBLIC");
            });
    }
}
