package com.testcontainers.userregister.learning.repository;

import com.testcontainers.userregister.learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
