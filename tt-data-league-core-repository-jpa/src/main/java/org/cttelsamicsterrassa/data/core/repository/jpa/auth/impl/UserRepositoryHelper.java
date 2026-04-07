package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.UserJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepositoryHelper extends JpaRepository<UserJPA, UUID> {
    Optional<UserJPA> findByUsername(String username);
    Optional<UserJPA> findByEmail(String email);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}

