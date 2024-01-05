package com.testcontainers.userregister.learning.rabbit.consumer;

import com.testcontainers.userregister.learning.model.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;

import static com.testcontainers.userregister.learning.rabbit.config.RabbitMqConfig.ACCOUNT_CREATED_QUEUE;

public interface AccountConsumer {
    @RabbitListener(queues = {ACCOUNT_CREATED_QUEUE})
    void consumeAccountCreated(Message message);
}
