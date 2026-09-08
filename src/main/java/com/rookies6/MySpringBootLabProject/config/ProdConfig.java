package com.rookies6.MySpringBootLabProject.config;

import com.rookies6.MySpringBootLabProject.dto.MyEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("prod")
@Configuration
public class ProdConfig {

    @Bean
    public MyEnvironment myEnvironment() {
        return MyEnvironment.builder()
                .mode("운영환경")
                .build();
    }
}