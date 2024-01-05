package com.testcontainers.userregister.learning.rabbit.producer;

import com.testcontainers.userregister.learning.model.Message;
import com.testcontainers.userregister.learning.model.enums.EventEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static com.testcontainers.userregister.learning.rabbit.config.RabbitMqConfig.CREATE_ACCOUNT_EXCHANGE;
import static com.testcontainers.userregister.learning.rabbit.config.RabbitMqConfig.CREATE_ACCOUNT_QUEUE;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountProducerImpl implements AccountProducer {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessageCreateAccount(Long userId) {
        Message msg = new Message();
        msg.setMsg("userId=" + userId);
        msg.setEvent(EventEnum.CREATE_ACCOUNT_EVENT);

        rabbitTemplate.convertAndSend(CREATE_ACCOUNT_EXCHANGE, CREATE_ACCOUNT_QUEUE, msg);

        log.info("Message with id [{}] send to [{}] exchange with routing key [{}]", msg.getId(), CREATE_ACCOUNT_EXCHANGE, CREATE_ACCOUNT_QUEUE);
    }
}
