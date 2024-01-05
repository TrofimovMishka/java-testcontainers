package com.testcontainers.userregister.learning.rabbit.producer;

public interface AccountProducer {
    void sendMessageCreateAccount(Long userId);
}
