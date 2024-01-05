package com.testcontainers.userregister.learning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Table(name = "accounts")
@Entity
@Setter
@Getter
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    OffsetDateTime creationDate;
    boolean isActive;

}
