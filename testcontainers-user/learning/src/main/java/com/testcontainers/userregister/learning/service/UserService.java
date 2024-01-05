package com.testcontainers.userregister.learning.service;

import com.testcontainers.userregister.learning.dto.UserDto;

public interface UserService {

    UserDto createUser(UserDto user);

    UserDto findUser(Long userId);

    void updateCountOfAccounts();
}
