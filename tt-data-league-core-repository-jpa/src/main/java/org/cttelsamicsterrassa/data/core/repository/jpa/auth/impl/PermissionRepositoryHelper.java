package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.PermissionJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PermissionRepositoryHelper extends JpaRepository<PermissionJPA, UUID> {
    Optional<PermissionJPA> findByResourceAndAction(Resource resource, PermissionAction action);
}

