package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.albertsanso.commons.model.Entity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class User extends Entity {

    private static final BCryptPasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder(12);

    private final UUID id;
    private final LocalDateTime createdAt;

    private String username;
    private String email;
    private String passwordHash;
    private boolean active;
    private final Set<Role> roles;

    private User(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt, boolean active, Set<Role> roles) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.passwordHash = passwordHash;
        this.createdAt = createdAt;
        this.active = active;
        this.roles = new HashSet<>(roles);
    }

    public static User createNew(String username, String email, String plainPassword) {
        return new User(
                UUID.randomUUID(),
                username,
                email,
                PASSWORD_ENCODER.encode(plainPassword),
                LocalDateTime.now(),
                true,
                Collections.emptySet()
        );
    }

    public static User createExisting(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt, boolean active) {
        return new User(id, username, email, passwordHash, createdAt, active, Collections.emptySet());
    }

    public static User createExisting(UUID id, String username, String email, String passwordHash, LocalDateTime createdAt, boolean active, Set<Role> roles) {
        return new User(id, username, email, passwordHash, createdAt, active, roles);
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

    public Set<Role> getRoles() {
        return Collections.unmodifiableSet(roles);
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

    public void assignRole(Role role) {
        roles.add(role);
    }

    public boolean hasRole(String roleName) {
        String normalized = roleName.trim().toUpperCase();
        return roles.stream().anyMatch(r -> r.getName().equalsIgnoreCase(normalized));
    }

    public boolean hasPermission(Resource resource, PermissionAction action) {
        return roles.stream().anyMatch(r -> r.hasPermission(resource, action));
    }
}
