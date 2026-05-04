package org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.PermissionJPA;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PermissionToPermissionJPAMapper implements Function<Permission, PermissionJPA> {
    @Override
    public PermissionJPA apply(Permission permission) {
        PermissionJPA jpa = new PermissionJPA();
        jpa.setId(permission.getId());
        jpa.setResource(permission.getResource());
        jpa.setAction(permission.getAction());
        return jpa;
    }
}

