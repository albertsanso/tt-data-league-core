package org.cttelsamicsterrassa.data.core.repository.jpa.auth.impl;

import jakarta.transaction.Transactional;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;
import org.cttelsamicsterrassa.data.core.domain.repository.auth.RoleRepository;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.RoleJPAToRoleMapper;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper.RoleToRoleJPAMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
@Component
public class RoleRepositoryImpl implements RoleRepository {

    private final RoleRepositoryHelper helper;
    private final RoleJPAToRoleMapper fromJpaMapper;
    private final RoleToRoleJPAMapper toJpaMapper;

    @Autowired
    public RoleRepositoryImpl(RoleRepositoryHelper helper,
                               RoleJPAToRoleMapper fromJpaMapper,
                               RoleToRoleJPAMapper toJpaMapper) {
        this.helper = helper;
        this.fromJpaMapper = fromJpaMapper;
        this.toJpaMapper = toJpaMapper;
    }

    @Override
    public Optional<Role> findById(UUID id) {
        return helper.findById(id).map(fromJpaMapper);
    }

    @Override
    public Optional<Role> findByName(String name) {
        return helper.findByNameIgnoreCase(name).map(fromJpaMapper);
    }

    @Override
    public List<Role> findAll() {
        return helper.findAll().stream().map(fromJpaMapper).collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return helper.existsByNameIgnoreCase(name);
    }

    @Override
    public void save(Role role) {
        helper.save(toJpaMapper.apply(role));
    }

    @Override
    public void saveAll(List<Role> roles) {
        helper.saveAll(roles.stream().map(toJpaMapper).collect(Collectors.toList()));
    }

    @Override
    public void deleteById(UUID id) {
        helper.deleteById(id);
    }
}

