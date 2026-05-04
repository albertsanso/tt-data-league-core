package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.PermissionJPAToPermissionMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.PermissionToPermissionJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.RoleJPAToRoleMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.RoleToRoleJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.PermissionJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.RoleJPA;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = RoleRepositoryImplTest.TestApplication.class)
@Import({
        RoleRepositoryImpl.class,
        RoleJPAToRoleMapper.class,
        RoleToRoleJPAMapper.class,
        PermissionJPAToPermissionMapper.class,
        PermissionToPermissionJPAMapper.class
})
class RoleRepositoryImplTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
    @EnableJpaRepositories(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
    static class TestApplication {
    }

    @Autowired
    private RoleRepositoryImpl repository;

    @Autowired
    private RoleRepositoryHelper roleHelper;

    @Autowired
    private PermissionRepositoryHelper permissionHelper;

    @BeforeEach
    void setUp() {
        roleHelper.deleteAll();
        permissionHelper.deleteAll();
    }

    @Test
    void findByNameReturnsSavedRole() {
        persistRole("ADMIN", new java.util.HashSet<>());

        Optional<Role> result = repository.findByName("ADMIN");

        assertTrue(result.isPresent());
        assertEquals("ADMIN", result.get().getName());
    }

    @Test
    void findByNameIsCaseInsensitive() {
        persistRole("GUEST", new java.util.HashSet<>());

        assertTrue(repository.findByName("guest").isPresent());
        assertTrue(repository.findByName("Guest").isPresent());
    }

    @Test
    void findByNameReturnsEmptyWhenAbsent() {
        assertTrue(repository.findByName("UNKNOWN").isEmpty());
    }

    @Test
    void existsByNameReturnsTrueWhenRoleExists() {
        persistRole("ANALYST", new java.util.HashSet<>());

        assertTrue(repository.existsByName("ANALYST"));
        assertTrue(repository.existsByName("analyst"));
    }

    @Test
    void existsByNameReturnsFalseWhenAbsent() {
        assertFalse(repository.existsByName("NONEXISTENT"));
    }

    @Test
    void saveAllPersistsMultipleRoles() {
        Role r1 = Role.createNew("ROLE_A", Set.of());
        Role r2 = Role.createNew("ROLE_B", Set.of());

        repository.saveAll(List.of(r1, r2));

        assertEquals(2, roleHelper.count());
    }

    private void persistRole(String name, Set<PermissionJPA> permissions) {
        RoleJPA jpa = new RoleJPA();
        jpa.setId(UUID.randomUUID());
        jpa.setName(name);
        jpa.setPermissions(new java.util.HashSet<>(permissions));
        roleHelper.saveAndFlush(jpa);
    }

    @Test
    void roleWithPermissionsIsPersistedAndReloadedCorrectly() {
        PermissionJPA permJpa = new PermissionJPA();
        permJpa.setId(UUID.randomUUID());
        permJpa.setResource(Resource.CLUB);
        permJpa.setAction(PermissionAction.READ);
        permissionHelper.saveAndFlush(permJpa);

        RoleJPA roleJpa = new RoleJPA();
        roleJpa.setId(UUID.randomUUID());
        roleJpa.setName("TESTER");
        roleJpa.setPermissions(Set.of(permJpa));
        roleHelper.saveAndFlush(roleJpa);

        Optional<Role> result = repository.findByName("TESTER");

        assertTrue(result.isPresent());
        assertEquals(1, result.get().getPermissions().size());
        Permission perm = result.get().getPermissions().iterator().next();
        assertEquals(Resource.CLUB, perm.getResource());
        assertEquals(PermissionAction.READ, perm.getAction());
    }
}



