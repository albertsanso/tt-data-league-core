package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.PermissionJPAToPermissionMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.PermissionToPermissionJPAMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.PermissionJPA;
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
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(classes = PermissionRepositoryImplTest.TestApplication.class)
@Import({
        PermissionRepositoryImpl.class,
        PermissionJPAToPermissionMapper.class,
        PermissionToPermissionJPAMapper.class
})
class PermissionRepositoryImplTest {

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EntityScan(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
    @EnableJpaRepositories(basePackages = "org.cttelsamicsterrassa.data.core.repository.jpa")
    static class TestApplication {
    }

    @Autowired
    private PermissionRepositoryImpl repository;

    @Autowired
    private PermissionRepositoryHelper helper;

    @BeforeEach
    void setUp() {
        helper.deleteAll();
    }

    @Test
    void findByResourceAndActionReturnsCorrectPermission() {
        PermissionJPA jpa = new PermissionJPA();
        jpa.setId(UUID.randomUUID());
        jpa.setResource(Resource.CLUB);
        jpa.setAction(PermissionAction.READ);
        helper.saveAndFlush(jpa);

        Optional<Permission> result = repository.findByResourceAndAction(Resource.CLUB, PermissionAction.READ);

        assertTrue(result.isPresent());
        assertEquals(Resource.CLUB, result.get().getResource());
        assertEquals(PermissionAction.READ, result.get().getAction());
    }

    @Test
    void findByResourceAndActionReturnsEmptyWhenAbsent() {
        Optional<Permission> result = repository.findByResourceAndAction(Resource.MATCH, PermissionAction.DELETE);

        assertTrue(result.isEmpty());
    }

    @Test
    void saveAllPersistsMultiplePermissions() {
        Permission p1 = Permission.createNew(Resource.CLUB, PermissionAction.READ);
        Permission p2 = Permission.createNew(Resource.MATCH, PermissionAction.WRITE);

        repository.saveAll(List.of(p1, p2));

        assertEquals(2, helper.count());
    }

    @Test
    void findAllReturnsSavedPermissions() {
        PermissionJPA jpa1 = new PermissionJPA();
        jpa1.setId(UUID.randomUUID());
        jpa1.setResource(Resource.PRACTITIONER);
        jpa1.setAction(PermissionAction.READ);
        helper.saveAndFlush(jpa1);

        List<Permission> result = repository.findAll();

        assertEquals(1, result.size());
        assertEquals(Resource.PRACTITIONER, result.getFirst().getResource());
    }
}

