package com.fixnow.api.domain.repository;

import com.fixnow.api.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findById(UUID userId);

    Optional<User> findByUserName(String username);
}
