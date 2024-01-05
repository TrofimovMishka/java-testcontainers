package com.testcontainers.account.learning.mapper;

import com.testcontainers.account.learning.dto.AccountDto;
import com.testcontainers.account.learning.dto.UserDto;
import com.testcontainers.account.learning.model.Account;
import com.testcontainers.account.learning.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    UserDto userToDto(User user);

    User dtoToUser(UserDto dto);

    @Mapping(target = "isActive", source = "active")
    AccountDto accountToDto(Account account);

    @Mapping(target = "active", source = "isActive")
    Account dtoToAccount(AccountDto dto);
}
