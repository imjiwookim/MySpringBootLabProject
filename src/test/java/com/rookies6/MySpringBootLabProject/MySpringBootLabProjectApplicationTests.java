package com.rookies6.MySpringBootLabProject;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test") // <- 이 줄을 추가해 주세요!
@SpringBootTest
class MySpringBootLabProjectApplicationTests {

    @Test
    void contextLoads() {
    }
}