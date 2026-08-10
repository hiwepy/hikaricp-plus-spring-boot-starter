package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link HikaricpWithMetricProperties}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class HikaricpWithMetricPropertiesTest {

    @Test
    void defaultValues() {
        HikaricpWithMetricProperties props = new HikaricpWithMetricProperties();
        assertThat(props.isEnabled()).isFalse();
        assertThat(props.getType()).isEqualTo(HikaricpWithMetricProperties.MetricType.DROPWIZARD);
    }

    @Test
    void prefixConstant() {
        assertThat(HikaricpWithMetricProperties.PREFIX).isEqualTo("spring.datasource.hikari.metric");
    }

    @Test
    void setAndGetEnabled() {
        HikaricpWithMetricProperties props = new HikaricpWithMetricProperties();
        props.setEnabled(true);
        assertThat(props.isEnabled()).isTrue();
    }

    @Test
    void setAndGetType() {
        HikaricpWithMetricProperties props = new HikaricpWithMetricProperties();
        props.setType(HikaricpWithMetricProperties.MetricType.MICROMETER);
        assertThat(props.getType()).isEqualTo(HikaricpWithMetricProperties.MetricType.MICROMETER);
    }

    @Test
    void metricTypeEnumValues() {
        HikaricpWithMetricProperties.MetricType[] values = HikaricpWithMetricProperties.MetricType.values();
        assertThat(values).hasSize(3);
        assertThat(values).contains(
            HikaricpWithMetricProperties.MetricType.DROPWIZARD,
            HikaricpWithMetricProperties.MetricType.MICROMETER,
            HikaricpWithMetricProperties.MetricType.PROMETHEUS
        );
    }

    @Test
    void metricTypeGet() {
        assertThat(HikaricpWithMetricProperties.MetricType.DROPWIZARD.get()).isEqualTo("dropwizard");
        assertThat(HikaricpWithMetricProperties.MetricType.MICROMETER.get()).isEqualTo("micrometer");
        assertThat(HikaricpWithMetricProperties.MetricType.PROMETHEUS.get()).isEqualTo("prometheus");
    }

    @Test
    void metricTypeEquals() {
        HikaricpWithMetricProperties.MetricType type = HikaricpWithMetricProperties.MetricType.DROPWIZARD;
        assertThat(type.equals(HikaricpWithMetricProperties.MetricType.DROPWIZARD)).isTrue();
        assertThat(type.equals(HikaricpWithMetricProperties.MetricType.MICROMETER)).isFalse();
    }

    @Test
    void metricTypeEqualsString() {
        HikaricpWithMetricProperties.MetricType type = HikaricpWithMetricProperties.MetricType.DROPWIZARD;
        assertThat(type.equals("dropwizard")).isTrue();
        assertThat(type.equals("micrometer")).isFalse();
    }

    @Test
    void metricTypeValueOfIgnoreCase() {
        assertThat(HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("dropwizard"))
            .isEqualTo(HikaricpWithMetricProperties.MetricType.DROPWIZARD);
        assertThat(HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("MICROMETER"))
            .isEqualTo(HikaricpWithMetricProperties.MetricType.MICROMETER);
        assertThat(HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("Prometheus"))
            .isEqualTo(HikaricpWithMetricProperties.MetricType.PROMETHEUS);
    }

    @Test
    void metricTypeValueOfIgnoreCaseThrowsOnUnknown() {
        assertThatThrownBy(() -> HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("unknown"))
            .isInstanceOf(NoSuchElementException.class)
            .hasMessageContaining("Cannot found metricType with key 'unknown'");
    }
}
