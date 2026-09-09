package com.zaxxer.hikari.spring.boot;

import com.zaxxer.hikari.spring.boot.ds.HikaricpDataSourceProperties;
import org.junit.jupiter.api.Test;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.ArrayList;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HikaricpDataSourcePropertiesTest {

    @Test
    void shouldRoundTripEveryWritableProperty() throws Exception {
        HikaricpDataSourceProperties properties = new HikaricpDataSourceProperties();
        for (PropertyDescriptor descriptor : Introspector.getBeanInfo(
                HikaricpDataSourceProperties.class, Object.class).getPropertyDescriptors()) {
            if (descriptor.getReadMethod() == null || descriptor.getWriteMethod() == null) {
                continue;
            }
            Object value = sampleValue(descriptor.getPropertyType(), descriptor.getName());
            descriptor.getWriteMethod().invoke(properties, value);
            assertEquals(value, descriptor.getReadMethod().invoke(properties), descriptor.getName());
        }
    }

    @Test
    void shouldRoundTripRootAndMetricProperties() {
        HikaricpProperties properties = new HikaricpProperties();
        properties.setEnabled(true);
        properties.setRoutable(true);
        properties.setSlaves(new ArrayList<HikaricpDataSourceProperties>());
        assertEquals(HikaricpProperties.PREFIX, "spring.datasource.hikari");
        assertEquals(true, properties.isEnabled());
        assertEquals(true, properties.isRoutable());
        assertNotNull(properties.getSlaves());

        HikaricpWithMetricProperties metric = new HikaricpWithMetricProperties();
        metric.setEnabled(true);
        metric.setType(HikaricpWithMetricProperties.MetricType.MICROMETER);
        assertEquals(true, metric.isEnabled());
        assertEquals(HikaricpWithMetricProperties.MetricType.MICROMETER, metric.getType());
        assertEquals("micrometer", metric.getType().get());
        assertEquals(true, metric.getType().equals("MICROMETER"));
        assertEquals(true, metric.getType().equals(HikaricpWithMetricProperties.MetricType.MICROMETER));
    }

    private Object sampleValue(Class<?> type, String name) {
        if (type == String.class) return name;
        if (type == int.class || type == Integer.class) return 7;
        if (type == long.class || type == Long.class) return 7000L;
        if (type == boolean.class || type == Boolean.class) return true;
        if (type == Properties.class) {
            Properties value = new Properties();
            value.setProperty("key", name);
            return value;
        }
        throw new IllegalArgumentException(type.getName());
    }
}
