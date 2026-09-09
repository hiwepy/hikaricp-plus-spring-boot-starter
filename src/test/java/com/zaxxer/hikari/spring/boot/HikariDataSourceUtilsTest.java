package com.zaxxer.hikari.spring.boot;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import com.zaxxer.hikari.spring.boot.util.HikariDataSourceUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HikariDataSourceUtilsTest {

    @Test
    void shouldCopyAllPoolSettings() {
        HikaricpDataSourceProperties source = properties();
        HikariDataSource target = new HikariDataSource();
        HikariDataSourceUtils.configureProperties(source, target);

        assertEquals(2, target.getMinimumIdle());
        assertEquals(8, target.getMaximumPoolSize());
        assertEquals(60000L, target.getMaxLifetime());
        assertEquals("SELECT 1", target.getConnectionTestQuery());
        assertEquals("SELECT 2", target.getConnectionInitSql());
        assertEquals("schema1", target.getSchema());
        assertEquals("catalog1", target.getCatalog());
        assertTrue(target.isReadOnly());
        assertEquals("value", target.getDataSourceProperties().getProperty("key"));
    }

    @Test
    void shouldCreateHikariDataSourceFromBothPropertyTypes() {
        HikariDataSource created = HikariDataSourceUtils.createDataSource(properties());
        assertEquals("jdbc:h2:mem:test", created.getJdbcUrl());
        assertEquals("sa", created.getUsername());
        assertEquals(8, created.getMaximumPoolSize());

        DataSourceProperties basic = new DataSourceProperties();
        basic.setUrl("jdbc:h2:mem:basic");
        basic.setUsername("user");
        HikariDataSource fromBasic = HikariDataSourceUtils.createDataSource(basic, HikariDataSource.class);
        assertEquals("jdbc:h2:mem:basic", fromBasic.getJdbcUrl());
    }

    static HikaricpDataSourceProperties properties() {
        HikaricpDataSourceProperties value = new HikaricpDataSourceProperties();
        value.setName("pool"); value.setJdbcUrl("jdbc:h2:mem:test"); value.setUsername("sa"); value.setPassword("");
        value.setInitializationFailTimeout(-1); value.setMinIdle(2); value.setMaxPoolSize(8);
        value.setMaxLifetime(60000); value.setConnectionInitSql("SELECT 2"); value.setConnectionTestQuery("SELECT 1");
        value.setConnectionTimeout(30000); value.setValidationTimeout(5000); value.setIdleTimeout(10000);
        value.setAutoCommit(false); value.setReadOnly(true); value.setIsolateInternalQueries(true);
        value.setAllowPoolSuspension(true); value.setLeakDetectionThreshold(3000);
        value.setSchema("schema1"); value.setCatalog("catalog1");
        Properties data = new Properties(); data.setProperty("key", "value"); value.setDataSourceProperties(data);
        Properties health = new Properties(); health.setProperty("health", "value"); value.setHealthCheckProperties(health);
        return value;
    }
}
