package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HikaricpProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class HikaricpPropertiesTest {

    @Test
    void defaultValues() {
        HikaricpProperties props = new HikaricpProperties();
        assertThat(props.isEnabled()).isFalse();
        assertThat(props.isRoutable()).isFalse();
        assertThat(props.getSlaves()).isEmpty();
    }

    @Test
    void prefixConstant() {
        assertThat(HikaricpProperties.PREFIX).isEqualTo("spring.datasource.hikari");
    }

    @Test
    void setAndGetEnabled() {
        HikaricpProperties props = new HikaricpProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    void setAndGetRoutable() {
        HikaricpProperties props = new HikaricpProperties();
        props.setRoutable(true);
        assertThat(props.isRoutable()).isTrue();
    }

    @Test
    void setAndGetSlaves() {
        HikaricpProperties props = new HikaricpProperties();
        assertThat(props.getSlaves()).isNotNull().isEmpty();
    }

    @Test
    void inheritsDataSourceProperties() {
        HikaricpProperties props = new HikaricpProperties();
        props.setJdbcUrl("jdbc:mysql://localhost/test");
        props.setUsername("user");
        props.setPassword("pass");
        props.setDriverClassName("com.mysql.cj.jdbc.Driver");
        assertThat(props.getJdbcUrl()).isEqualTo("jdbc:mysql://localhost/test");
        assertThat(props.getUsername()).isEqualTo("user");
        assertThat(props.getPassword()).isEqualTo("pass");
        assertThat(props.getDriverClassName()).isEqualTo("com.mysql.cj.jdbc.Driver");
    }
}
