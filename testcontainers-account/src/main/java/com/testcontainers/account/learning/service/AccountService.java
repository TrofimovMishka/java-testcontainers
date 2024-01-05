package com.testcontainers.account.learning.service;

import com.testcontainers.account.learning.dto.AccountDto;

public interface AccountService {

    AccountDto createAccount(Long userId);

    AccountDto findAccount(Long userId);
}
