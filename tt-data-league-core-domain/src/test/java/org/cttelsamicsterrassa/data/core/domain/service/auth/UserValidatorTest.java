package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidatorTest {

    private final UserValidator userValidator = new UserValidator();

    @Test
    void validateUsername() {
        assertTrue(userValidator.validateUsername("player_1").isEmpty());
        assertFalse(userValidator.validateUsername("p").isEmpty());
        assertFalse(userValidator.validateUsername("invalid-user").isEmpty());
    }

    @Test
    void validateEmail() {
        assertTrue(userValidator.validateEmail("player1@example.com").isEmpty());
        assertFalse(userValidator.validateEmail("player1example.com").isEmpty());
    }

    @Test
    void validatePassword() {
        assertTrue(userValidator.validatePassword("Secret123!").isEmpty());
        assertFalse(userValidator.validatePassword("short").isEmpty());
        assertFalse(userValidator.validatePassword("alllowercase123!").isEmpty());
        assertFalse(userValidator.validatePassword("ALLUPPERCASE123!").isEmpty());
        assertFalse(userValidator.validatePassword("NoNumber!").isEmpty());
        assertFalse(userValidator.validatePassword("NoSpecial123").isEmpty());
    }
}

