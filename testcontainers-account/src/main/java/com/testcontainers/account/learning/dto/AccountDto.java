package com.testcontainers.account.learning.dto;

import java.time.OffsetDateTime;

public record AccountDto(
        Long id,
        OffsetDateTime creationDate,
        boolean isActive
) {
}
