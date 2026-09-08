package com.rookies6.MySpringBootLabProject.runner;

import com.rookies6.MySpringBootLabProject.MyPropProperties;
import com.rookies6.MySpringBootLabProject.dto.MyEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class MyPropRunner implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(MyPropRunner.class);

    @Value("${myprop.username}")
    private String username;

    @Value("${myprop.port}")
    private int port;

    private final MyPropProperties myPropProperties;
    private final MyEnvironment myEnvironment;

    public MyPropRunner(MyPropProperties myPropProperties, MyEnvironment myEnvironment) {
        this.myPropProperties = myPropProperties;
        this.myEnvironment = myEnvironment;
    }

    @Override
    public void run(String... args) throws Exception {
        logger.debug("================ MyPropRunner DEBUG ================");
        logger.debug("@Value username: {}", username);
        logger.debug("@Value port: {}", port);
        
        logger.info("================ MyPropRunner INFO ================");
        logger.info("Properties username: {}", myPropProperties.getUsername());
        logger.info("Properties port: {}", myPropProperties.getPort());
        logger.info("Current Environment: {}", myEnvironment.getMode());
    }
}