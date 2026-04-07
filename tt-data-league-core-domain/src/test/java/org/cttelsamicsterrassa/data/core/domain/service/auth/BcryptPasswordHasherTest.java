package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BcryptPasswordHasherTest {

    private final BcryptPasswordHasher passwordHasher = new BcryptPasswordHasher();

    @Test
    void hashAndVerifyValidPassword() {
        String hash = passwordHasher.hash("Secret123!");

        assertTrue(passwordHasher.verify("Secret123!", hash));
        assertFalse(passwordHasher.verify("WrongPass123!", hash));
    }

    @Test
    void samePasswordProducesDifferentHashesWithBcryptSalt() {
        String hash1 = passwordHasher.hash("Secret123!");
        String hash2 = passwordHasher.hash("Secret123!");

        assertNotEquals(hash1, hash2);
        assertTrue(passwordHasher.verify("Secret123!", hash1));
        assertTrue(passwordHasher.verify("Secret123!", hash2));
    }
}

