package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.RoleJPA;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepositoryHelper extends JpaRepository<RoleJPA, UUID> {
    Optional<RoleJPA> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
}

