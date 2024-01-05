package com.testcontainers.userregister.learning.rabbit.consumer;

import com.testcontainers.userregister.learning.model.Message;
import com.testcontainers.userregister.learning.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.testcontainers.userregister.learning.rabbit.config.RabbitMqConfig.ACCOUNT_CREATED_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountConsumerImpl implements AccountConsumer {

    private final UserService userService;

    @Override
    @RabbitListener(queues = {ACCOUNT_CREATED_QUEUE})
    public void consumeAccountCreated(Message message) {
        log.info("Received message id [{}] with content [{}] for event [{}]", message.getId(), message.getMsg(), message.getEvent());

        switch (message.getEvent()){
            case ACCOUNT_CREATED_EVENT -> userService.updateCountOfAccounts();
        }
    }
}