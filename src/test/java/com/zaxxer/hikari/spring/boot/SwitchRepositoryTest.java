package com.zaxxer.hikari.spring.boot;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.springframework.biz.jdbc.DataSourceRoutingKeyHolder;

import com.zaxxer.hikari.spring.boot.ds.annotation.SwitchRepository;

/**
 * Tests for {@link SwitchRepository}.
 *
 * @author [@Loong Wan](https://github.com/loong10k)
 */
class SwitchRepositoryTest {

    @SwitchRepository("slave1")
    public void annotatedMethod() {
    }

    @SwitchRepository
    public void defaultAnnotatedMethod() {
    }

    @Test
    void annotationWithValue() throws NoSuchMethodException {
        Method method = SwitchRepositoryTest.class.getMethod("annotatedMethod");
        SwitchRepository annotation = method.getAnnotation(SwitchRepository.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo("slave1");
    }

    @Test
    void annotationWithDefaultValue() throws NoSuchMethodException {
        Method method = SwitchRepositoryTest.class.getMethod("defaultAnnotatedMethod");
        SwitchRepository annotation = method.getAnnotation(SwitchRepository.class);
        assertThat(annotation).isNotNull();
        assertThat(annotation.value()).isEqualTo(DataSourceRoutingKeyHolder.MASTER_DATASOURCE);
    }

    @Test
    void annotationRetentionIsRuntime() {
        assertThat(SwitchRepository.class.isAnnotationPresent(java.lang.annotation.Documented.class)).isTrue();
        assertThat(SwitchRepository.class.isAnnotationPresent(java.lang.annotation.Inherited.class)).isTrue();
    }

    @Test
    void annotationIsTargetingMethod() {
        assertThat(SwitchRepository.class.getAnnotation(java.lang.annotation.Target.class).value())
            .contains(ElementType.METHOD);
    }

    @Test
    void annotationHasRuntimeRetention() {
        assertThat(SwitchRepository.class.getAnnotation(java.lang.annotation.Retention.class).value())
            .isEqualTo(RetentionPolicy.RUNTIME);
    }
}
