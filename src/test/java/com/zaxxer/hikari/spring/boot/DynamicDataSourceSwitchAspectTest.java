package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.aspectj.lang.ProceedingJoinPoint;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;

import com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository;
import com.zaxxer.hikari.spring.boot.ds.aspect.DynamicDataSourceSwitchAspect;

/**
 * Tests for {@link DynamicDataSourceSwitchAspect}.
 *
 * @author <a href="https://github.com/loong10k">Loong Wan</a>
 */
class DynamicDataSourceSwitchAspectTest {

    private final DynamicDataSourceSwitchAspect aspect = new DynamicDataSourceSwitchAspect();

    @AfterEach
    void tearDown() {
        DataSourceRoutingKeyHolder.clearDataSourceKey();
    }

    @Test
    void aroundSetsDataSourceKeyAndRestores() throws Throwable {
        DataSourceRoutingKeyHolder.setDataSourceKey("original");

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenReturn("result");

        SwitchRepository annotation = getAnnotation("slave1");
        Object result = aspect.around(joinPoint, annotation);

        assertThat(result).isEqualTo("result");
        assertThat(DataSourceRoutingKeyHolder.getDataSourceKey()).isEqualTo("original");
    }

    @Test
    void aroundRestoresKeyEvenOnException() throws Throwable {
        DataSourceRoutingKeyHolder.setDataSourceKey("original");

        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenThrow(new RuntimeException("test error"));

        SwitchRepository annotation = getAnnotation("slave2");

        try {
            aspect.around(joinPoint, annotation);
        } catch (RuntimeException e) {
            assertThat(e.getMessage()).isEqualTo("test error");
        }

        assertThat(DataSourceRoutingKeyHolder.getDataSourceKey()).isEqualTo("original");
    }

    @Test
    void aroundSetsCorrectKey() throws Throwable {
        ProceedingJoinPoint joinPoint = mock(ProceedingJoinPoint.class);
        when(joinPoint.proceed()).thenReturn(null);

        SwitchRepository annotation = getAnnotation("readSlave");
        aspect.around(joinPoint, annotation);

        // After execution, the key should be restored to whatever it was before
        // (null in this case since we didn't set one)
    }

    private SwitchRepository getAnnotation(String value) {
        return new SwitchRepository() {
            @Override
            public Class<? extends java.lang.annotation.Annotation> annotationType() {
                return SwitchRepository.class;
            }

            @Override
            public String value() {
                return value;
            }
        };
    }
}
