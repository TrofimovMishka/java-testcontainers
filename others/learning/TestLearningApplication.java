package com.testcontainers.userregister.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

//@TestConfiguration(proxyBeanMethods = false)
public class TestLearningApplication {
//
//	@Bean
//	@ServiceConnection
//	RabbitMQContainer rabbitContainer() {
//		return new RabbitMQContainer(DockerImageName.parse("rabbitmq:latest"));
//	}
//
//	public static void main(String[] args) {
//		SpringApplication.from(LearningApplication::main).with(TestLearningApplication.class).run(args);
//	}

}
