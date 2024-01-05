package com.testcontainers.userregister.learning.dto;

import java.time.OffsetDateTime;

public record AccountDto(
        Long id,
        OffsetDateTime creationDate,
        boolean isActive
) {
}
