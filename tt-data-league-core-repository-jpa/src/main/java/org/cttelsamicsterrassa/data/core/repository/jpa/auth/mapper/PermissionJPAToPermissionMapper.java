package org.cttelsamicsterrassa.data.core.repository.jpa.auth.mapper;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.repository.jpa.auth.model.PermissionJPA;
import org.springframework.stereotype.Component;

import java.util.function.Function;

@Component
public class PermissionJPAToPermissionMapper implements Function<PermissionJPA, Permission> {
    @Override
    public Permission apply(PermissionJPA jpa) {
        return Permission.createExisting(jpa.getId(), jpa.getResource(), jpa.getAction());
    }
}

