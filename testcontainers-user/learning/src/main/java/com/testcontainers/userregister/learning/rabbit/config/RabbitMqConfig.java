package com.testcontainers.userregister.learning.rabbit.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String CREATE_ACCOUNT_QUEUE = "q.create-account-queue";
    public static final String CREATE_ACCOUNT_EXCHANGE = "x.create-account-exchange";

    public static final String ACCOUNT_CREATED_QUEUE = "q.account-created-queue";
    public static final String ACCOUNT_CREATED_EXCHANGE = "x.account-created-exchange";

    @Bean
    public Queue queue() {
        return new Queue(CREATE_ACCOUNT_QUEUE, false);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(CREATE_ACCOUNT_EXCHANGE, false, false);
    }

    @Bean
    Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).withQueueName();
    }

    @Bean
    public Queue accountQueue() {
        return new Queue(ACCOUNT_CREATED_QUEUE, false);
    }

    @Bean
    public DirectExchange accountExchange() {
        return new DirectExchange(ACCOUNT_CREATED_EXCHANGE, false, false);
    }

    @Bean
    Binding accountBinding(Queue accountQueue, DirectExchange accountExchange) {
        return BindingBuilder.bind(accountQueue).to(accountExchange).withQueueName();
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}