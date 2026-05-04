package org.cttelsamicsterrassa.data.core.domain.repository.auth;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PermissionRepository {
    Optional<Permission> findById(UUID id);
    Optional<Permission> findByResourceAndAction(Resource resource, PermissionAction action);
    List<Permission> findAll();
    void save(Permission permission);
    void saveAll(List<Permission> permissions);
    void deleteById(UUID id);
}

