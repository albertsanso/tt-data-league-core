package org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.PermissionJPA;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.RoleJPA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RoleToRoleJPAMapper implements Function<Role, RoleJPA> {

    private final PermissionToPermissionJPAMapper permissionMapper;

    @Autowired
    public RoleToRoleJPAMapper(PermissionToPermissionJPAMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    @Override
    public RoleJPA apply(Role role) {
        RoleJPA jpa = new RoleJPA();
        jpa.setId(role.getId());
        jpa.setName(role.getName());
        Set<PermissionJPA> permissions = role.getPermissions().stream()
                .map(permissionMapper)
                .collect(Collectors.toSet());
        jpa.setPermissions(permissions);
        return jpa;
    }
}

