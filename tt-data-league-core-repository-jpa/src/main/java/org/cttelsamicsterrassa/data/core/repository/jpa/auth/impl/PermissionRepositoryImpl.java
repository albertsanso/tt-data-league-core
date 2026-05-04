package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.PermissionRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.PermissionJPAToPermissionMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.PermissionToPermissionJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Component
public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionRepositoryHelper helper;
    private final PermissionJPAToPermissionMapper fromJpaMapper;
    private final PermissionToPermissionJPAMapper toJpaMapper;

    @Autowired
    public PermissionRepositoryImpl(PermissionRepositoryHelper helper,
                                     PermissionJPAToPermissionMapper fromJpaMapper,
                                     PermissionToPermissionJPAMapper toJpaMapper) {
        this.helper = helper;
        this.fromJpaMapper = fromJpaMapper;
        this.toJpaMapper = toJpaMapper;
    }

    @Override
    public Optional<Permission> findById(UUID id) {
        return helper.findById(id).map(fromJpaMapper);
    }

    @Override
    public Optional<Permission> findByResourceAndAction(Resource resource, PermissionAction action) {
        return helper.findByResourceAndAction(resource, action).map(fromJpaMapper);
    }

    @Override
    public List<Permission> findAll() {
        return helper.findAll().stream().map(fromJpaMapper).collect(Collectors.toList());
    }

    @Override
    public void save(Permission permission) {
        helper.save(toJpaMapper.apply(permission));
    }

    @Override
    public void saveAll(List<Permission> permissions) {
        helper.saveAll(permissions.stream().map(toJpaMapper).collect(Collectors.toList()));
    }

    @Override
    public void deleteById(UUID id) {
        helper.deleteById(id);
    }
}

