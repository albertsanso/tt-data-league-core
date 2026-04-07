package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.cttelsamicsterrassa.data.core.domain.model.auth.User;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordHasher passwordHasher;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, passwordHasher, new UserValidator());
    }

    @Test
    void registerUserSavesWhenUsernameAndEmailAreAvailable() {
        when(userRepository.existsByUsername("player_1")).thenReturn(false);
        when(userRepository.existsByEmail("player1@example.com")).thenReturn(false);
        when(passwordHasher.hash("Secret123!")).thenReturn("hash");

        User user = authenticationService.registerUser("player_1", "player1@example.com", "Secret123!");

        assertEquals("player_1", user.getUsername());
        assertEquals("player1@example.com", user.getEmail());
        assertEquals("hash", user.getPasswordHash());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserRejectsDuplicateUsername() {
        when(userRepository.existsByUsername("player_1")).thenReturn(true);

        assertThrows(UserAlreadyExistsException.class,
                () -> authenticationService.registerUser("player_1", "player1@example.com", "Secret123!"));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateUserReturnsUserForValidCredentials() {
        User user = User.createExisting(
                UUID.randomUUID(),
                "player_1",
                "player1@example.com",
                "hash",
                LocalDateTime.now(),
                true
        );
        when(userRepository.findByUsername("player_1")).thenReturn(Optional.of(user));
        when(passwordHasher.verify("Secret123!", "hash")).thenReturn(true);

        Optional<User> authResult = authenticationService.authenticateUser("player_1", "Secret123!");

        assertTrue(authResult.isPresent());
        assertEquals(user.getId(), authResult.get().getId());
    }

    @Test
    void authenticateUserReturnsEmptyWhenPasswordIsInvalid() {
        User user = User.createExisting(
                UUID.randomUUID(),
                "player_1",
                "player1@example.com",
                "hash",
                LocalDateTime.now(),
                true
        );
        when(userRepository.findByUsername("player_1")).thenReturn(Optional.of(user));
        when(passwordHasher.verify("WrongPass123!", "hash")).thenReturn(false);

        Optional<User> authResult = authenticationService.authenticateUser("player_1", "WrongPass123!");

        assertFalse(authResult.isPresent());
    }
}

