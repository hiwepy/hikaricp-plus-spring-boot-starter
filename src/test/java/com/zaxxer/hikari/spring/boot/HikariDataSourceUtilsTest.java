package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;

/**
 * Tests for {@link HikariDataSourceUtils}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HikariDataSourceUtilsTest {

    @Test
    void createDataSourceWithH2() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
            assertThat(ds.getDriverClassName()).isEqualTo("org.h2.Driver");
            assertThat(ds.getJdbcUrl()).isEqualTo("jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1");
            assertThat(ds.getUsername()).isEqualTo("sa");
        }
    }

    @Test
    void createDataSourceAppliesAllProperties() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb2;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setMinIdle(3);
        props.setMaxPoolSize(10);
        props.setMaxLifetime(1800000);
        props.setConnectionInitSql("SELECT 1");
        props.setConnectionTimeout(10000);
        props.setValidationTimeout(3000);
        props.setIdleTimeout(300000);
        props.setAutoCommit(false);
        props.setReadOnly(false);
        props.setIsolateInternalQueries(false);
        props.setRegisterMbeans(false);
        props.setAllowPoolSuspension(true);
        props.setLeakDetectionThreshold(5000);
        props.setTransactionIsolationName("TRANSACTION_READ_COMMITTED");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
            assertThat(ds.getMinimumIdle()).isEqualTo(3);
            assertThat(ds.getMaximumPoolSize()).isEqualTo(10);
            assertThat(ds.getMaxLifetime()).isEqualTo(1800000);
            assertThat(ds.getConnectionInitSql()).isEqualTo("SELECT 1");
            assertThat(ds.getConnectionTimeout()).isEqualTo(10000);
            assertThat(ds.getValidationTimeout()).isEqualTo(3000);
            assertThat(ds.getIdleTimeout()).isEqualTo(300000);
            assertThat(ds.isAutoCommit()).isFalse();
            assertThat(ds.isReadOnly()).isFalse();
            assertThat(ds.isIsolateInternalQueries()).isFalse();
            assertThat(ds.isRegisterMbeans()).isFalse();
            assertThat(ds.isAllowPoolSuspension()).isTrue();
            assertThat(ds.getLeakDetectionThreshold()).isEqualTo(5000);
        }
    }

    @Test
    void createDataSourceWithJndiName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb3;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setJndiName("java:comp/env/jdbc/test");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithSchemaAndCatalog() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb4;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setSchema("PUBLIC");
        props.setCatalog("TESTDB");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
            assertThat(ds.getSchema()).isEqualTo("PUBLIC");
        }
    }

    @Test
    void createDataSourceWithDataSourceProperties() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb5;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        Properties dsProps = new Properties();
        dsProps.setProperty("cachePrepStmts", "true");
        props.setDataSourceProperties(dsProps);

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithHealthCheckProperties() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb6;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        Properties hcProps = new Properties();
        hcProps.setProperty("enabled", "true");
        props.setHealthCheckProperties(hcProps);

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithoutConnectionTestQuery() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb7;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setInitializationFailTimeout(1);
        props.setConnectionTestQuery("");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithEmptyConnectionInitSql() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb8;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setConnectionInitSql("");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithEmptyJndiName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb9;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setJndiName("");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithEmptySchema() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb10;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setSchema("");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }

    @Test
    void createDataSourceWithEmptyCatalog() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("org.h2.Driver");
        props.setJdbcUrl("jdbc:h2:mem:testdb11;DB_CLOSE_DELAY=-1");
        props.setUsername("sa");
        props.setPassword("");
        props.setConnectionTestQuery("SELECT 1");
        props.setInitializationFailTimeout(1);
        props.setCatalog("");

        try (HikariDataSource ds = HikariDataSourceUtils.createDataSource(props)) {
            assertThat(ds).isNotNull();
        }
    }
}
