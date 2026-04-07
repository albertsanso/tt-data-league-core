package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.albertsanso.commons.model.Entity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

public class User extends Entity {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

    private final UUID id;
    private final LocalDateTime createdAt;

    private String username;
    private String email;
    private String passwordHash;
    private boolean active;

    private User(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt, boolean active) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.active = active;
    }

    public static User createNew(String username, String email, String plainPassword) {
        return new User(
                UUID.randomUUID(),
                username,
                email,
                PASSWORD_ENCODER.encode(plainPassword),
                LocalDateTime.now(),
                true
        );
    }

    public static User createExisting(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt, boolean active) {
        return new User(id, username, email, passwordHash, createdAt, active);
    }

    public UUID getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public boolean verifyPassword(String plainPassword) {
        return PASSWORD_ENCODER.matches(plainPassword, passwordHash);
    }

    public void changePassword(String newPlainPassword) {
        passwordHash = PASSWORD_ENCODER.encode(newPlainPassword);
    }

    public void disable() {
        active = false;
    }

    public void enable() {
        active = true;
    }
}

