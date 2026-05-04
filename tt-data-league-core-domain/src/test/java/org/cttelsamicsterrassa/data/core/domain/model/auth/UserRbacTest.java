package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserRbacTest {

    private User newUser() {
        return User.createExisting(UUID.randomUUID(), "player", "p@example.com", "hash", LocalDateTime.now(), true);
    }

    private Role roleWithPermission(String name, Resource resource, PermissionAction action) {
        return Role.createNew(name, Set.of(Permission.createNew(resource, action)));
    }

    @Test
    void assignRoleAddsRoleToUser() {
        User user = newUser();
        Role role = Role.createNew("GUEST", Set.of());

        user.assignRole(role);

        assertTrue(user.getRoles().contains(role));
    }

    @Test
    void assignRoleDeduplicatesExistingRole() {
        User user = newUser();
        Role role1 = Role.createNew("GUEST", Set.of());
        Role role2 = Role.createNew("guest", Set.of()); // same normalised name

        user.assignRole(role1);
        user.assignRole(role2);

        assertTrue(user.getRoles().size() == 1);
    }

    @Test
    void hasRoleReturnsTrueForAssignedRole() {
        User user = newUser();
        user.assignRole(Role.createNew("ADMIN", Set.of()));

        assertTrue(user.hasRole("ADMIN"));
        assertTrue(user.hasRole("admin"));
        assertTrue(user.hasRole(" admin "));
    }

    @Test
    void hasRoleReturnsFalseForUnassignedRole() {
        User user = newUser();

        assertFalse(user.hasRole("ADMIN"));
    }

    @Test
    void hasPermissionDelegatesAcrossRoles() {
        User user = newUser();
        user.assignRole(roleWithPermission("GUEST", Resource.CLUB, PermissionAction.READ));

        assertTrue(user.hasPermission(Resource.CLUB, PermissionAction.READ));
        assertFalse(user.hasPermission(Resource.CLUB, PermissionAction.WRITE));
        assertFalse(user.hasPermission(Resource.MATCH, PermissionAction.READ));
    }

    @Test
    void hasPermissionReturnsFalseWhenNoRolesAssigned() {
        User user = newUser();

        assertFalse(user.hasPermission(Resource.CLUB, PermissionAction.READ));
    }

    @Test
    void getRolesReturnsUnmodifiableView() {
        User user = newUser();

        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> user.getRoles().add(Role.createNew("X", Set.of())));
    }

    @Test
    void createExistingWithRolesRestoresRoles() {
        Role role = Role.createNew("ANALYST", Set.of());
        User user = User.createExisting(
                UUID.randomUUID(), "u", "u@e.com", "hash", LocalDateTime.now(), true, Set.of(role));

        assertTrue(user.hasRole("ANALYST"));
    }
}

