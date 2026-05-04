package org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.RoleJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoleJPAToRoleMapper implements Function<RoleJPA, Role> {

    private final PermissionJPAToPermissionMapper permissionMapper;

    @Autowired
    public RoleJPAToRoleMapper(PermissionJPAToPermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public Role apply(RoleJPA jpa) {
        Set<Permission> permissions = jpa.getPermissions().stream()
                .map(permissionMapper)
                .collect(Collectors.toSet());
        return Role.createExisting(jpa.getId(), jpa.getName(), permissions);
    }
}

