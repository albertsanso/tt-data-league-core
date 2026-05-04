package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;
import org.cttelsamicsterrassa.data.core.domain.model.auth.User;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.RoleRepository;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
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
    private RoleRepository roleRepository;

    @Mock
    private PasswordHasher passwordHasher;

    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        authenticationService = new AuthenticationService(userRepository, roleRepository, passwordHasher, new UserValidator());
    }

    @Test
    void registerUserSavesWhenUsernameAndEmailAreAvailable() {
        when(userRepository.existsByUsername("player_1")).thenReturn(false);
        when(userRepository.existsByEmail("player1@example.com")).thenReturn(false);
        when(passwordHasher.hash("Secret123!")).thenReturn("hash");
        when(roleRepository.findByName(RbacCatalog.defaultRoleName())).thenReturn(Optional.empty());

        User user = authenticationService.registerUser("player_1", "player1@example.com", "Secret123!");

        assertEquals("player_1", user.getUsername());
        assertEquals("player1@example.com", user.getEmail());
        assertEquals("hash", user.getPasswordHash());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void registerUserAssignsDefaultRoleWhenRoleExists() {
        Role guestRole = Role.createNew(RbacCatalog.GUEST, Set.of());
        when(userRepository.existsByUsername("player_1")).thenReturn(false);
        when(userRepository.existsByEmail("player1@example.com")).thenReturn(false);
        when(passwordHasher.hash("Secret123!")).thenReturn("hash");
        when(roleRepository.findByName(RbacCatalog.defaultRoleName())).thenReturn(Optional.of(guestRole));

        User user = authenticationService.registerUser("player_1", "player1@example.com", "Secret123!");

        assertTrue(user.hasRole(RbacCatalog.GUEST));
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
                UUID.randomUUID(), "player_1", "player1@example.com", "hash", LocalDateTime.now(), true);
        when(userRepository.findByUsername("player_1")).thenReturn(Optional.of(user));
        when(passwordHasher.verify("Secret123!", "hash")).thenReturn(true);
        when(roleRepository.findByName(RbacCatalog.defaultRoleName())).thenReturn(Optional.empty());

        Optional<User> authResult = authenticationService.authenticateUser("player_1", "Secret123!");

        assertTrue(authResult.isPresent());
        assertEquals(user.getId(), authResult.get().getId());
    }

    @Test
    void authenticateUserReturnsEmptyWhenPasswordIsInvalid() {
        User user = User.createExisting(
                UUID.randomUUID(), "player_1", "player1@example.com", "hash", LocalDateTime.now(), true);
        when(userRepository.findByUsername("player_1")).thenReturn(Optional.of(user));
        when(passwordHasher.verify("WrongPass123!", "hash")).thenReturn(false);

        Optional<User> authResult = authenticationService.authenticateUser("player_1", "WrongPass123!");

        assertFalse(authResult.isPresent());
    }

    @Test
    void assignRoleThrowsWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(InvalidCredentialsException.class,
                () -> authenticationService.assignRole(userId, RbacCatalog.ADMIN));
    }

    @Test
    void assignRoleThrowsWhenRoleNotFound() {
        UUID userId = UUID.randomUUID();
        User user = User.createExisting(userId, "u", "u@e.com", "h", LocalDateTime.now(), true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName("UNKNOWN_ROLE")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class,
                () -> authenticationService.assignRole(userId, "UNKNOWN_ROLE"));
    }

    @Test
    void assertAuthorizedThrowsAuthorizationExceptionWhenNotPermitted() {
        UUID userId = UUID.randomUUID();
        User user = User.createExisting(userId, "u", "u@e.com", "h", LocalDateTime.now(), true);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertThrows(AuthorizationException.class,
                () -> authenticationService.assertAuthorized(userId, Resource.CLUB, PermissionAction.DELETE));
    }

    @Test
    void userHasPermissionReturnsTrueForAuthorisedUser() {
        UUID userId = UUID.randomUUID();
        Role adminRole = Role.createNew(RbacCatalog.ADMIN,
                Set.of(Permission.createNew(Resource.CLUB, PermissionAction.DELETE)));
        User user = User.createExisting(userId, "u", "u@e.com", "h", LocalDateTime.now(), true, Set.of(adminRole));
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        assertTrue(authenticationService.userHasPermission(userId, Resource.CLUB, PermissionAction.DELETE));
    }

    @Test
    void userHasPermissionReturnsFalseWhenUserAbsent() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertFalse(authenticationService.userHasPermission(userId, Resource.CLUB, PermissionAction.READ));
    }
}
