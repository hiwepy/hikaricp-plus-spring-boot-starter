package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;
import org.springframework.boot.jdbc.autoconfigure.DataSourceProperties;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.DynamicRoutingDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;

/**
 * Tests for {@link DynamicRoutingDataSource}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class DynamicRoutingDataSourceTest {

    private DynamicRoutingDataSource routingDataSource;
    private HikariDataSource masterDataSource;
    private HikariDataSource slaveDataSource;

    @BeforeEach
    void setUp() {
        routingDataSource = new DynamicRoutingDataSource();

        masterDataSource = createH2DataSource("masterdb");
        slaveDataSource = createH2DataSource("slavedb");

        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource);
        targetDataSources.put("slave", slaveDataSource);

        routingDataSource.setTargetDataSources(targetDataSources);
        routingDataSource.setDefaultTargetDataSource(masterDataSource);
        routingDataSource.afterPropertiesSet();
    }

    @AfterEach
    void tearDown() {
        DataSourceRoutingKeyHolder.clearDataSourceKey();
        if (masterDataSource != null) {
            masterDataSource.close();
        }
        if (slaveDataSource != null) {
            slaveDataSource.close();
        }
    }

    private HikariDataSource createH2DataSource(String dbName) {
        HikariDataSource ds = new HikariDataSource();
        ds.setDriverClassName("org.h2.Driver");
        ds.setJdbcUrl("jdbc:h2:mem:" + dbName + ";DB_CLOSE_DELAY=-1");
        ds.setUsername("sa");
        ds.setPassword("");
        ds.setConnectionTestQuery("SELECT 1");
        ds.setInitializationFailTimeout(1);
        return ds;
    }

    @Test
    void determineCurrentLookupKeyReturnsCurrentKey() throws Exception {
        Method method = DynamicRoutingDataSource.class.getDeclaredMethod("determineCurrentLookupKey");
        method.setAccessible(true);
        // Without setting a key, it returns whatever DataSourceRoutingKeyHolder has
        Object result = method.invoke(routingDataSource);
        assertThat(result).isIn(null, "defaultDataSource", DataSourceRoutingKeyHolder.getDataSourceKey());
    }

    @Test
    void determineCurrentLookupKeyReturnsSetKey() throws Exception {
        DataSourceRoutingKeyHolder.setDataSourceKey("slave");
        Method method = DynamicRoutingDataSource.class.getDeclaredMethod("determineCurrentLookupKey");
        method.setAccessible(true);
        assertThat(method.invoke(routingDataSource)).isEqualTo("slave");
    }

    @Test
    void getTargetDataSourcesReturnsAllSources() {
        Map<Object, Object> targets = routingDataSource.getTargetDataSources();
        assertThat(targets).containsKey("master");
        assertThat(targets).containsKey("slave");
    }

    @Test
    void getResolvedDataSourcesReturnsResolvedSources() {
        Map<Object, DataSource> resolved = routingDataSource.getResolvedDataSources();
        assertThat(resolved).containsKey("master");
        assertThat(resolved).containsKey("slave");
    }

    @Test
    void afterPropertiesSetRegistersKeys() {
        assertThat(DataSourceRoutingKeyHolder.dataSourceKeys).contains("master", "slave");
    }

    @Test
    void setTargetDataSourceByNameAddsNewSource() {
        DataSourceProperties basicProps = new DataSourceProperties();
        basicProps.setDriverClassName("org.h2.Driver");
        basicProps.setUrl("jdbc:h2:mem:newdb;DB_CLOSE_DELAY=-1");
        basicProps.setUsername("sa");
        basicProps.setPassword("");

        HikaricpDataSourceProperties hikariProps = new HikaricpDataSourceProperties();
        hikariProps.setDriverClassName("org.h2.Driver");
        hikariProps.setJdbcUrl("jdbc:h2:mem:newdb;DB_CLOSE_DELAY=-1");
        hikariProps.setUsername("sa");
        hikariProps.setPassword("");
        hikariProps.setConnectionTestQuery("SELECT 1");
        hikariProps.setInitializationFailTimeout(1);

        routingDataSource.setTargetDataSource("newslave", basicProps, hikariProps);

        Map<Object, Object> targets = routingDataSource.getTargetDataSources();
        assertThat(targets).containsKey("newslave");
    }

    @Test
    void setTargetDataSourceByPropertiesUsesName() {
        DataSourceProperties basicProps = new DataSourceProperties();
        basicProps.setDriverClassName("org.h2.Driver");
        basicProps.setUrl("jdbc:h2:mem:nameddb;DB_CLOSE_DELAY=-1");
        basicProps.setUsername("sa");
        basicProps.setPassword("");

        HikaricpDataSourceProperties hikariProps = new HikaricpDataSourceProperties();
        hikariProps.setName("namedslave");
        hikariProps.setDriverClassName("org.h2.Driver");
        hikariProps.setJdbcUrl("jdbc:h2:mem:nameddb;DB_CLOSE_DELAY=-1");
        hikariProps.setUsername("sa");
        hikariProps.setPassword("");
        hikariProps.setConnectionTestQuery("SELECT 1");
        hikariProps.setInitializationFailTimeout(1);

        routingDataSource.setTargetDataSource(basicProps, hikariProps);

        Map<Object, Object> targets = routingDataSource.getTargetDataSources();
        assertThat(targets).containsKey("namedslave");
    }

    @Test
    void setTargetDataSourceFallsBackToBasicProperties() {
        DataSourceProperties basicProps = new DataSourceProperties();
        basicProps.setDriverClassName("org.h2.Driver");
        basicProps.setUrl("jdbc:h2:mem:fallbackdb;DB_CLOSE_DELAY=-1");
        basicProps.setUsername("sa");
        basicProps.setPassword("secret");

        HikaricpDataSourceProperties hikariProps = new HikaricpDataSourceProperties();
        // No username, password, jdbcUrl, driverClassName set - should fall back to basicProps
        hikariProps.setConnectionTestQuery("SELECT 1");
        hikariProps.setInitializationFailTimeout(1);

        routingDataSource.setTargetDataSource("fallback", basicProps, hikariProps);

        Map<Object, Object> targets = routingDataSource.getTargetDataSources();
        assertThat(targets).containsKey("fallback");
    }

    @Test
    void setNewTargetDataSourcesAddsAll() {
        Map<Object, Object> newSources = new HashMap<>();
        HikariDataSource newDs = createH2DataSource("newdb");
        newSources.put("newkey", newDs);

        routingDataSource.setNewTargetDataSources(newSources);

        Map<Object, Object> targets = routingDataSource.getTargetDataSources();
        assertThat(targets).containsKey("newkey");
        newDs.close();
    }

    @Test
    void removeTargetDataSourceRemovesKey() {
        routingDataSource.removeTargetDataSource("slave");
        Map<Object, Object> targets = routingDataSource.getTargetDataSources();
        assertThat(targets).doesNotContainKey("slave");
    }

    @Test
    void resolveSpecifiedLookupKeyResolvesString() throws Exception {
        Method method = org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource.class
            .getDeclaredMethod("resolveSpecifiedLookupKey", Object.class);
        method.setAccessible(true);
        Object resolved = method.invoke(routingDataSource, "master");
        assertThat(resolved).isEqualTo("master");
    }
}
