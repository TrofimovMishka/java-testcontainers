package com.testcontainers.account.learning.dto;

import java.time.OffsetDateTime;

public record UserDto(
        Long id,
        String name,
        String surname,
        OffsetDateTime birthday,
        AccountDto account
) {
}
