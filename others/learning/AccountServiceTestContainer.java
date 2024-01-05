package com.testcontainers.userregister.learning;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public interface AccountServiceTestContainer {

    String DOCKER_IMAGE_NAME = "test-containers-account:1.0.6-SNAPSHOT";
    int PORT = 9011;

    @Container
    GenericContainer<?> container = new GenericContainer<>(DOCKER_IMAGE_NAME)
                    .withExposedPorts(PORT);

//    @DynamicPropertySource
//    static void registerProperties(DynamicPropertyRegistry registry) {
//        registry.add();
//    }
}