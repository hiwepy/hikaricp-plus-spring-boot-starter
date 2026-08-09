package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Properties;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;

/**
 * Tests for {@link HikaricpDataSourceProperties}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class HikaricpDataSourcePropertiesTest {

    @Test
    void defaultValues() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        assertThat(props.getName()).isNull();
        assertThat(props.getJndiName()).isNull();
        assertThat(props.getPoolName()).isNull();
        assertThat(props.getSchema()).isNull();
        assertThat(props.getCatalog()).isNull();
        assertThat(props.getDriverClassName()).isNull();
        assertThat(props.getJdbcUrl()).isNull();
        assertThat(props.getUsername()).isNull();
        assertThat(props.getPassword()).isNull();
        assertThat(props.getInitializationFailTimeout()).isEqualTo(1);
        assertThat(props.getMinIdle()).isEqualTo(5);
        assertThat(props.getMaxPoolSize()).isEqualTo(50);
        assertThat(props.getMaxLifetime()).isPositive();
        assertThat(props.getConnectionInitSql()).isEmpty();
        assertThat(props.getConnectionTestQuery()).isEqualTo("SELECT 1");
        assertThat(props.getConnectionTimeout()).isPositive();
        assertThat(props.getValidationTimeout()).isPositive();
        assertThat(props.getIdleTimeout()).isPositive();
        assertThat(props.getTransactionIsolationName()).isNull();
        assertThat(props.isAutoCommit()).isTrue();
        assertThat(props.isReadOnly()).isFalse();
        assertThat(props.isIsolateInternalQueries()).isFalse();
        assertThat(props.isRegisterMbeans()).isFalse();
        assertThat(props.isAllowPoolSuspension()).isFalse();
        assertThat(props.getLeakDetectionThreshold()).isEqualTo(0);
        assertThat(props.getDataSourceProperties()).isNotNull();
        assertThat(props.getHealthCheckProperties()).isNotNull();
    }

    @Test
    void setAndGetName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setName("test-db");
        assertThat(props.getName()).isEqualTo("test-db");
    }

    @Test
    void setAndGetJndiName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setJndiName("java:comp/env/jdbc/test");
        assertThat(props.getJndiName()).isEqualTo("java:comp/env/jdbc/test");
    }

    @Test
    void setAndGetPoolName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setPoolName("mypool");
        assertThat(props.getPoolName()).isEqualTo("mypool");
    }

    @Test
    void setAndGetSchema() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setSchema("public");
        assertThat(props.getSchema()).isEqualTo("public");
    }

    @Test
    void setAndGetCatalog() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setCatalog("mydb");
        assertThat(props.getCatalog()).isEqualTo("mydb");
    }

    @Test
    void setAndGetDriverClassName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setDriverClassName("com.mysql.cj.jdbc.Driver");
        assertThat(props.getDriverClassName()).isEqualTo("com.mysql.cj.jdbc.Driver");
    }

    @Test
    void setAndGetJdbcUrl() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setJdbcUrl("jdbc:mysql://localhost:3306/test");
        assertThat(props.getJdbcUrl()).isEqualTo("jdbc:mysql://localhost:3306/test");
    }

    @Test
    void setAndGetUsername() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setUsername("admin");
        assertThat(props.getUsername()).isEqualTo("admin");
    }

    @Test
    void setAndGetPassword() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setPassword("secret");
        assertThat(props.getPassword()).isEqualTo("secret");
    }

    @Test
    void setAndGetInitializationFailTimeout() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setInitializationFailTimeout(5000);
        assertThat(props.getInitializationFailTimeout()).isEqualTo(5000);
    }

    @Test
    void setAndGetMinIdle() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setMinIdle(10);
        assertThat(props.getMinIdle()).isEqualTo(10);
    }

    @Test
    void setAndGetMaxPoolSize() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setMaxPoolSize(100);
        assertThat(props.getMaxPoolSize()).isEqualTo(100);
    }

    @Test
    void setAndGetMaxLifetime() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setMaxLifetime(60000);
        assertThat(props.getMaxLifetime()).isEqualTo(60000);
    }

    @Test
    void setAndGetConnectionInitSql() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setConnectionInitSql("SET NAMES utf8");
        assertThat(props.getConnectionInitSql()).isEqualTo("SET NAMES utf8");
    }

    @Test
    void setAndGetConnectionTestQuery() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setConnectionTestQuery("SELECT 1");
        assertThat(props.getConnectionTestQuery()).isEqualTo("SELECT 1");
    }

    @Test
    void setAndGetConnectionTimeout() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setConnectionTimeout(10000);
        assertThat(props.getConnectionTimeout()).isEqualTo(10000);
    }

    @Test
    void setAndGetValidationTimeout() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setValidationTimeout(3000);
        assertThat(props.getValidationTimeout()).isEqualTo(3000);
    }

    @Test
    void setAndGetIdleTimeout() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setIdleTimeout(300000);
        assertThat(props.getIdleTimeout()).isEqualTo(300000);
    }

    @Test
    void setAndGetTransactionIsolationName() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setTransactionIsolationName("TRANSACTION_READ_COMMITTED");
        assertThat(props.getTransactionIsolationName()).isEqualTo("TRANSACTION_READ_COMMITTED");
    }

    @Test
    void setAndGetAutoCommit() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setAutoCommit(false);
        assertThat(props.isAutoCommit()).isFalse();
    }

    @Test
    void setAndGetReadOnly() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setReadOnly(true);
        assertThat(props.isReadOnly()).isTrue();
    }

    @Test
    void setAndGetIsolateInternalQueries() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setIsolateInternalQueries(true);
        assertThat(props.isIsolateInternalQueries()).isTrue();
    }

    @Test
    void setAndGetRegisterMbeans() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setRegisterMbeans(true);
        assertThat(props.isRegisterMbeans()).isTrue();
    }

    @Test
    void setAndGetAllowPoolSuspension() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setAllowPoolSuspension(true);
        assertThat(props.isAllowPoolSuspension()).isTrue();
    }

    @Test
    void setAndGetLeakDetectionThreshold() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        props.setLeakDetectionThreshold(5000);
        assertThat(props.getLeakDetectionThreshold()).isEqualTo(5000);
    }

    @Test
    void setAndGetDataSourceProperties() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        Properties p = new Properties();
        p.setProperty("cachePrepStmts", "true");
        props.setDataSourceProperties(p);
        assertThat(props.getDataSourceProperties()).isEqualTo(p);
    }

    @Test
    void setAndGetHealthCheckProperties() {
        HikaricpDataSourceProperties props = new HikaricpDataSourceProperties();
        Properties p = new Properties();
        p.setProperty("enabled", "true");
        props.setHealthCheckProperties(p);
        assertThat(props.getHealthCheckProperties()).isEqualTo(p);
    }
}
