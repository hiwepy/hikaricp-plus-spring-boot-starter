package com.zaxxer.hikari.spring.boot;

import com.zaxxer.hikari.spring.boot.ds.DynamicRoutingDataSource;
import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import org.junit.jupiter.api.Test;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class DynamicRoutingDataSourceTest {

    @Test
    void shouldInitializeInspectAddAndRemoveTargets() {
        ExposedRoutingDataSource routing = new ExposedRoutingDataSource();
        DataSource master = mock(DataSource.class);
        Map<Object, Object> initial = new HashMap<Object, Object>();
        initial.put("master", master);
        routing.setTargetDataSources(initial);
        routing.setDefaultTargetDataSource(master);
        routing.afterPropertiesSet();

        assertEquals(initial, routing.getTargetDataSources());
        assertNotNull(routing.getResolvedDataSources());
        DataSourceRoutingKeyHolder.setDataSourceKey("master");
        assertEquals("master", routing.lookupKey());

        Map<Object, Object> additional = new HashMap<Object, Object>();
        additional.put("read", mock(DataSource.class));
        routing.setNewTargetDataSources(additional);
        assertTrue(routing.getTargetDataSources().containsKey("read"));
        routing.removeTargetDataSource("read");
        assertFalse(routing.getTargetDataSources().containsKey("read"));
    }

    @Test
    void shouldCreateAndAddTargetFromInheritedBasicProperties() {
        ExposedRoutingDataSource routing = new ExposedRoutingDataSource();
        DataSource master = mock(DataSource.class);
        Map<Object, Object> initial = new HashMap<Object, Object>();
        initial.put("master", master);
        routing.setTargetDataSources(initial);
        routing.setDefaultTargetDataSource(master);
        routing.afterPropertiesSet();

        DataSourceProperties basic = new DataSourceProperties();
        basic.setUrl("jdbc:h2:mem:routing"); basic.setUsername("sa"); basic.setPassword("");
        basic.setDriverClassName("org.h2.Driver");
        HikaricpDataSourceProperties target = new HikaricpDataSourceProperties();
        target.setName("slave"); target.setInitializationFailTimeout(-1);
        routing.setTargetDataSource(basic, target);

        assertTrue(routing.getTargetDataSources().containsKey("slave"));
        assertEquals("jdbc:h2:mem:routing", target.getJdbcUrl());
        assertEquals("sa", target.getUsername());
    }

    private static final class ExposedRoutingDataSource extends DynamicRoutingDataSource {
        Object lookupKey() {
            return determineCurrentLookupKey();
        }
    }
}
