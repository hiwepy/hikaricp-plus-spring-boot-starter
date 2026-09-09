package com.zaxxer.hikari.spring.boot;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.spring.boot.ds.DynamicRoutingDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HikaricpAutoConfigurationTest {

    @Test
    void shouldCreateSingleDataSourceAndInheritBasicProperties() {
        DataSourceProperties basic = basicProperties();
        HikaricpProperties hikari = new HikaricpProperties();
        hikari.setInitializationFailTimeout(-1);

        DataSource result = new HikaricpAutoConfiguration().dataSource(basic, hikari);

        assertTrue(result instanceof HikariDataSource);
        HikariDataSource dataSource = (HikariDataSource) result;
        assertEquals("jdbc:h2:mem:auto", dataSource.getJdbcUrl());
        assertEquals("sa", dataSource.getUsername());
        assertEquals("org.h2.Driver", dataSource.getDriverClassName());
    }

    @Test
    void shouldCreateRoutableMasterAndSlaveDataSources() {
        HikaricpProperties master = new HikaricpProperties();
        master.setRoutable(true);
        master.setInitializationFailTimeout(-1);
        HikaricpDataSourceProperties slave = HikariDataSourceUtilsTest.properties();
        slave.setName("slave");
        master.setSlaves(Collections.singletonList(slave));

        DataSource result = new HikaricpAutoConfiguration().dataSource(basicProperties(), master);

        assertTrue(result instanceof DynamicRoutingDataSource);
        DynamicRoutingDataSource routing = (DynamicRoutingDataSource) result;
        assertTrue(routing.getTargetDataSources().containsKey("slave"));
    }

    private DataSourceProperties basicProperties() {
        DataSourceProperties basic = new DataSourceProperties();
        basic.setUrl("jdbc:h2:mem:auto");
        basic.setUsername("sa");
        basic.setPassword("");
        basic.setDriverClassName("org.h2.Driver");
        return basic;
    }
}
