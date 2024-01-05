package com.testcontainers.userregister.learning.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Table(name = "users")
@Entity
@Setter
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String name;
    String surname;
    OffsetDateTime birthday;

    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "account_id", referencedColumnName = "id")
    Account account;
}
