package com.testcontainers.account.learning.rabbit.producer;

import com.testcontainers.account.learning.model.Account;

public interface UserProducer {
    void sendMessageAccountWasCreated(Long userId, Account account);
}
