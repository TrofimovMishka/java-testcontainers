package com.testcontainers.account.learning.service;

import com.testcontainers.account.learning.dto.AccountDto;
import com.testcontainers.account.learning.mapper.AccountMapper;
import com.testcontainers.account.learning.model.Account;
import com.testcontainers.account.learning.model.User;
import com.testcontainers.account.learning.rabbit.producer.UserProducer;
import com.testcontainers.account.learning.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AccountMapper accountMapper;
    private final UserProducer userProducer;

    @Transactional
    @Override
    public AccountDto createAccount(Long userId) {
        AccountDto accountDto = new AccountDto(null, OffsetDateTime.now(), true);
        Account account = accountMapper.dtoToAccount(accountDto);

        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        user.setAccount(account);
        User saved = userRepository.save(user);

        userProducer.sendMessageAccountWasCreated(userId, account);

        return accountMapper.accountToDto(saved.getAccount());
    }

    @Override
    public AccountDto findAccount(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        Account account = user.getAccount();
        return accountMapper.accountToDto(account);
    }
}
