package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RoleTest {

    @Test
    void createNewAssignsUniqueId() {
        Role r1 = Role.createNew("ADMIN", Set.of());
        Role r2 = Role.createNew("ADMIN", Set.of());

        assertNotNull(r1.getId());
        assertNotNull(r2.getId());
    }

    @Test
    void createExistingRestoresId() {
        UUID id = UUID.randomUUID();
        Role role = Role.createExisting(id, "ANALYST", Set.of());

        assertEquals(id, role.getId());
        assertEquals("ANALYST", role.getName());
    }

    @Test
    void hasPermissionReturnsTrueForAssignedPermission() {
        Permission readClub = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Role role = Role.createNew("GUEST", Set.of(readClub));

        assertTrue(role.hasPermission(Resource.CLUB, PermissionAction.READ));
    }

    @Test
    void hasPermissionReturnsFalseForMissingPermission() {
        Permission readClub = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Role role = Role.createNew("GUEST", Set.of(readClub));

        assertFalse(role.hasPermission(Resource.CLUB, PermissionAction.WRITE));
        assertFalse(role.hasPermission(Resource.MATCH, PermissionAction.READ));
    }

    @Test
    void addPermissionDeduplicatesExistingPermission() {
        Role role = Role.createNew("TEST", Set.of());
        Permission readClub = Permission.createNew(Resource.CLUB, PermissionAction.READ);

        role.addPermission(readClub);
        role.addPermission(Permission.createNew(Resource.CLUB, PermissionAction.READ));

        assertEquals(1, role.getPermissions().size());
    }

    @Test
    void getPermissionsReturnsUnmodifiableView() {
        Role role = Role.createNew("TEST", Set.of());

        assertFalse(role.getPermissions() instanceof java.util.HashSet);
        org.junit.jupiter.api.Assertions.assertThrows(UnsupportedOperationException.class,
                () -> role.getPermissions().add(Permission.createNew(Resource.CLUB, PermissionAction.READ)));
    }

    @Test
    void nameIsNormalisedToUpperCase() {
        Role role = Role.createNew(" admin ", Set.of());
        assertEquals("ADMIN", role.getName());
    }

    @Test
    void equalsIsTrueForSameNormalisedName() {
        Role r1 = Role.createNew("admin", Set.of());
        Role r2 = Role.createNew("ADMIN", Set.of());

        assertEquals(r1, r2);
    }
}

