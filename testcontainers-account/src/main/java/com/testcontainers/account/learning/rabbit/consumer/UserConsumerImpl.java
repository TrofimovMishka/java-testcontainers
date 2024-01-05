package com.testcontainers.account.learning.rabbit.consumer;

import com.testcontainers.account.learning.dto.AccountDto;
import com.testcontainers.account.learning.model.Message;
import com.testcontainers.account.learning.service.AccountService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.testcontainers.account.learning.rabbit.config.RabbitMqConfig.CREATE_ACCOUNT_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumerImpl implements UserConsumer {

    private final AccountService accountService;

    @Override
    @RabbitListener(queues = {CREATE_ACCOUNT_QUEUE})
    public void consume(Message message) {
        log.info("Received message id [{}] with content [{}] for event [{}]", message.getId(), message.getMsg(), message.getEvent());

        String[] content = message.getMsg().split("=");

        switch (message.getEvent()){
            case CREATE_ACCOUNT_EVENT -> {
                AccountDto account = accountService.createAccount(Long.parseLong(content[1]));
                log.info("Account created with id [{}]", account.id());
            }
        }
    }
}
