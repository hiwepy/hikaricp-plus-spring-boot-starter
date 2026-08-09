package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.zaxxer.hikari.spring.boot.util.MicrometerSystemClock;

/**
 * Tests for {@link MicrometerSystemClock}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class MicrometerSystemClockTest {

    @Test
    void instanceReturnsSingleton() {
        MicrometerSystemClock clock1 = MicrometerSystemClock.instance();
        MicrometerSystemClock clock2 = MicrometerSystemClock.instance();
        assertThat(clock1).isSameAs(clock2);
    }

    @Test
    void wallTimeReturnsReasonableValue() {
        MicrometerSystemClock clock = MicrometerSystemClock.instance();
        long wallTime = clock.wallTime();
        assertThat(wallTime).isGreaterThan(0);
        assertThat(Math.abs(wallTime - System.currentTimeMillis())).isLessThan(5000);
    }

    @Test
    void monotonicTimeReturnsReasonableValue() {
        MicrometerSystemClock clock = MicrometerSystemClock.instance();
        long monotonicTime = clock.monotonicTime();
        assertThat(monotonicTime).isGreaterThan(0);
        assertThat(Math.abs(monotonicTime - System.currentTimeMillis())).isLessThan(5000);
    }

    @Test
    void nowReturnsReasonableValue() {
        long now = MicrometerSystemClock.now();
        assertThat(now).isGreaterThan(0);
        assertThat(Math.abs(now - System.currentTimeMillis())).isLessThan(5000);
    }

    @Test
    void nowDateReturnsNonNull() {
        String date = MicrometerSystemClock.nowDate();
        assertThat(date).isNotNull().isNotEmpty();
    }

    @Test
    void successiveCallsReturnIncreasingValues() throws InterruptedException {
        long first = MicrometerSystemClock.now();
        Thread.sleep(10);
        long second = MicrometerSystemClock.now();
        assertThat(second).isGreaterThanOrEqualTo(first);
    }
}
