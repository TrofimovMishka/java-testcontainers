package com.testcontainers.account.learning.rabbit.producer;

import com.testcontainers.account.learning.model.Account;
import com.testcontainers.account.learning.model.Message;
import com.testcontainers.account.learning.model.enums.EventEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

import static com.testcontainers.account.learning.rabbit.config.RabbitMqConfig.ACCOUNT_CREATED_EXCHANGE;
import static com.testcontainers.account.learning.rabbit.config.RabbitMqConfig.ACCOUNT_CREATED_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserProducerImpl implements UserProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessageAccountWasCreated(Long userId, Account account) {
        Message msg = new Message();
        String message = String.format("%s : Created account with id=%d for user=%d", OffsetDateTime.now(), userId, account.getId());
        msg.setMsg(message);
        msg.setEvent(EventEnum.ACCOUNT_CREATED_EVENT);

        rabbitTemplate.convertAndSend(ACCOUNT_CREATED_EXCHANGE, ACCOUNT_CREATED_QUEUE, msg);

        log.info("Message with id [{}] send to [{}] exchange with routing key [{}]", msg.getId(), ACCOUNT_CREATED_EXCHANGE, ACCOUNT_CREATED_QUEUE);
    }
}
