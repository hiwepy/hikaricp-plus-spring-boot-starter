package com.zaxxer.hikari.spring.boot;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.metrics.MetricsTrackerFactory;
import com.zaxxer.hikari.spring.boot.util.MicrometerSystemClock;
import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.context.support.StaticApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HikaricpMonitoringTest {

    @Test
    void shouldCreateDropwizardAndPrometheusTrackers() {
        HikaricpWithDropwizardAutoConfiguration dropwizard = new HikaricpWithDropwizardAutoConfiguration();
        MetricRegistry registry = dropwizard.registry();
        assertNotNull(registry);
        assertNotNull(dropwizard.duridFilterRegistrationBean(registry));
        MetricsTrackerFactory prometheus = new HikaricpWithOnPrometheusAutoConfiguration().duridFilterRegistrationBean();
        assertNotNull(prometheus);
    }

    @Test
    void shouldCreateMicrometerInfrastructureWithAndWithoutExistingRegistry() {
        HikaricpWithOnMicrometerAutoConfiguration configuration = new HikaricpWithOnMicrometerAutoConfiguration();
        StaticApplicationContext empty = new StaticApplicationContext();
        configuration.setApplicationContext(empty);
        assertSame(empty, configuration.getApplicationContext());
        Clock clock = configuration.clock();
        assertSame(MicrometerSystemClock.instance(), clock);
        assertNotNull(configuration.nameMapper());
        MeterRegistry simple = configuration.meterRegistry(clock);
        assertTrue(simple instanceof SimpleMeterRegistry);
        assertNotNull(configuration.duridFilterRegistrationBean(simple));

        StaticApplicationContext populated = new StaticApplicationContext();
        populated.getBeanFactory().registerSingleton("existing", new SimpleMeterRegistry());
        configuration.setApplicationContext(populated);
        assertNotNull(configuration.meterRegistry(clock));
    }

    @Test
    void shouldExposeMetricEnumAndClockBehavior() {
        assertEquals(HikaricpWithMetricProperties.MetricType.DROPWIZARD,
                HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("DROPWIZARD"));
        assertEquals(HikaricpWithMetricProperties.MetricType.PROMETHEUS,
                HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("prometheus"));
        assertThrows(java.util.NoSuchElementException.class,
                () -> HikaricpWithMetricProperties.MetricType.valueOfIgnoreCase("missing"));
        long before = MicrometerSystemClock.now();
        assertTrue(MicrometerSystemClock.instance().wallTime() >= before);
        assertTrue(MicrometerSystemClock.instance().monotonicTime() > 0);
        assertNotNull(MicrometerSystemClock.nowDate());
    }
}
