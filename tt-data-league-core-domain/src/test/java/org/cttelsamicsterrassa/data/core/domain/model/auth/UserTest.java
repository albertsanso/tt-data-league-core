package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserTest {

    @Test
    void createNewCreatesActiveUserWithHashedPassword() {
        User user = User.createNew("player_1", "player1@example.com", "Secret123!");

        assertNotNull(user.getId());
        assertTrue(user.isActive());
        assertNotNull(user.getCreatedAt());
        assertNotEquals("Secret123!", user.getPasswordHash());
        assertTrue(user.verifyPassword("Secret123!"));
    }

    @Test
    void createExistingRestoresState() {
        UUID id = UUID.randomUUID();
        LocalDateTime createdAt = LocalDateTime.now().minusDays(1);
        User user = User.createExisting(id, "player_2", "p2@example.com", "hashed", createdAt, false);

        assertTrue(id.equals(user.getId()));
        assertTrue(createdAt.equals(user.getCreatedAt()));
        assertFalse(user.isActive());
    }

    @Test
    void changePasswordUpdatesStoredHash() {
        User user = User.createNew("player_3", "p3@example.com", "Secret123!");
        String oldHash = user.getPasswordHash();

        user.changePassword("NewSecret456@");

        assertNotEquals(oldHash, user.getPasswordHash());
        assertTrue(user.verifyPassword("NewSecret456@"));
        assertFalse(user.verifyPassword("Secret123!"));
    }

    @Test
    void disableAndEnableToggleState() {
        User user = User.createNew("player_4", "p4@example.com", "Secret123!");

        user.disable();
        assertFalse(user.isActive());

        user.enable();
        assertTrue(user.isActive());
    }
}

