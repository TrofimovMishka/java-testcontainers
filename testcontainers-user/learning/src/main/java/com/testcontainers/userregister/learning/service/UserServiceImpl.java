package com.testcontainers.userregister.learning.service;

import com.testcontainers.userregister.learning.dto.UserDto;
import com.testcontainers.userregister.learning.mapper.UserMapper;
import com.testcontainers.userregister.learning.model.User;
import com.testcontainers.userregister.learning.rabbit.producer.AccountProducer;
import com.testcontainers.userregister.learning.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final AccountProducer accountProducer;

    private int countOfAccounts = 0;


    @Override
    public UserDto createUser(UserDto userDto) {
        User user = userMapper.dtoToUser(userDto);
        User saved = userRepository.save(user);

        accountProducer.sendMessageCreateAccount(saved.getId());

        return userMapper.userToDto(saved);
    }

    @Override
    public UserDto findUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(EntityNotFoundException::new);
        return userMapper.userToDto(user);
    }

    @Override
    public void updateCountOfAccounts() {
        countOfAccounts++;
        log.info("Final number of accounts: " + countOfAccounts);
    }
}
