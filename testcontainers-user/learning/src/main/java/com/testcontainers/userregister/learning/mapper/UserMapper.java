package com.testcontainers.userregister.learning.mapper;

import com.testcontainers.userregister.learning.dto.AccountDto;
import com.testcontainers.userregister.learning.dto.UserDto;
import com.testcontainers.userregister.learning.model.Account;
import com.testcontainers.userregister.learning.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDto userToDto(User user);

    User dtoToUser(UserDto dto);

    @Mapping(target = "isActive", source = "active")
    AccountDto accountToDto(Account account);

    @Mapping(target = "active", source = "isActive")
    Account dtoToAccount(AccountDto dto);
}
