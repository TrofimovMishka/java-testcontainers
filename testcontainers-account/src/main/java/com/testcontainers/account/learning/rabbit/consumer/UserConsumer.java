package com.testcontainers.account.learning.rabbit.consumer;

import com.testcontainers.account.learning.model.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.testcontainers.account.learning.rabbit.config.RabbitMqConfig.CREATE_ACCOUNT_QUEUE;

public interface UserConsumer {
    @RabbitListener(queues = {CREATE_ACCOUNT_QUEUE})
    void consume(Message message);
}
