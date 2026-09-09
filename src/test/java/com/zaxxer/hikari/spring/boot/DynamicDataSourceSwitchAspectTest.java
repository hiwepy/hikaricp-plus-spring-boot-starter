package com.zaxxer.hikari.spring.boot;

import com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository;
import com.zaxxer.hikari.spring.boot.ds.aspect.DynamicDataSourceSwitchAspect;
import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.Test;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DynamicDataSourceSwitchAspectTest {

    @Test
    void shouldSwitchForInvocationAndRestorePreviousKey() throws Throwable {
        DataSourceRoutingKeyHolder.setDataSourceKey("master");
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenAnswer(invocation -> DataSourceRoutingKeyHolder.getDataSourceKey());

        Object result = new DynamicDataSourceSwitchAspect().around(joinPoint, annotation());

        assertEquals("slave", result);
        assertEquals("master", DataSourceRoutingKeyHolder.getDataSourceKey());
    }

    @Test
    void shouldRestorePreviousKeyWhenInvocationFails() throws Exception {
        DataSourceRoutingKeyHolder.setDataSourceKey("master");
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        try {
            when(joinPoint.proceed()).thenThrow(new IllegalStateException("expected"));
        } catch (Throwable throwable) {
            throw new AssertionError(throwable);
        }

        assertThrows(IllegalStateException.class,
                () -> new DynamicDataSourceSwitchAspect().around(joinPoint, annotation()));
        assertEquals("master", DataSourceRoutingKeyHolder.getDataSourceKey());
    }

    private SwitchRepository annotation() throws Exception {
        Method method = Fixture.class.getDeclaredMethod("read");
        return method.getAnnotation(SwitchRepository.class);
    }

    private static final class Fixture {
        @SwitchRepository("slave")
        void read() {
        }
    }
}
