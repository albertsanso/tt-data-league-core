package org.cttelsamicsterrassa.data.core.domain.repository.auth;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleRepository {
    Optional<Role> findById(UUID id);
    Optional<Role> findByName(String name);
    List<Role> findAll();
    boolean existsByName(String name);
    void save(Role role);
    void saveAll(List<Role> roles);
    void deleteById(UUID id);
}

