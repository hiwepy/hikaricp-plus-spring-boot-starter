package com.zaxxer.hikari.spring.boot;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/** Verifies that the Boot 2 HikariCP starter application context loads. */
@SpringBootTest(classes = DataBaseConfiguration.class)
class DemoApplicationTest {

    @Test
    void contextLoads() {
    }
}
