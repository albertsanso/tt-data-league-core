package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PermissionTest {

    @Test
    void createNewAssignsUniqueId() {
        Permission p1 = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Permission p2 = Permission.createNew(Resource.CLUB, PermissionAction.READ);

        assertNotNull(p1.getId());
        assertNotNull(p2.getId());
        assertNotEquals(p1.getId(), p2.getId());
    }

    @Test
    void equalsIsTrueForSameResourceAndAction() {
        Permission p1 = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Permission p2 = Permission.createExisting(UUID.randomUUID(), Resource.CLUB, PermissionAction.READ);

        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    void equalsIsFalseForDifferentAction() {
        Permission p1 = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Permission p2 = Permission.createNew(Resource.CLUB, PermissionAction.WRITE);

        assertNotEquals(p1, p2);
    }

    @Test
    void equalsIsFalseForDifferentResource() {
        Permission p1 = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Permission p2 = Permission.createNew(Resource.MATCH, PermissionAction.READ);

        assertNotEquals(p1, p2);
    }

    @Test
    void gettersReturnCorrectValues() {
        UUID id = UUID.randomUUID();
        Permission p = Permission.createExisting(id, Resource.PRACTITIONER, PermissionAction.DELETE);

        assertEquals(id, p.getId());
        assertEquals(Resource.PRACTITIONER, p.getResource());
        assertEquals(PermissionAction.DELETE, p.getAction());
    }

    @Test
    void setContainsDeduplicated() {
        java.util.Set<Permission> set = new java.util.HashSet<>();
        set.add(Permission.createNew(Resource.CLUB, PermissionAction.READ));
        set.add(Permission.createNew(Resource.CLUB, PermissionAction.READ));

        assertTrue(set.size() == 1);
    }
}

