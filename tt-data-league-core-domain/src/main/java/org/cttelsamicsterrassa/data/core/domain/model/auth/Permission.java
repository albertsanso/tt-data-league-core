package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.albertsanso.commons.model.Entity;

import java.util.Objects;
import java.util.UUID;

public class Permission extends Entity {

    private final UUID id;
    private final Resource resource;
    private final PermissionAction action;

    private Permission(UUID id, Resource resource, PermissionAction action) {
        this.id = id;
        this.resource = resource;
        this.action = action;
    }

    public static Permission createNew(Resource resource, PermissionAction action) {
        return new Permission(UUID.randomUUID(), resource, action);
    }

    public static Permission createExisting(UUID id, Resource resource, PermissionAction action) {
        return new Permission(id, resource, action);
    }

    public UUID getId() {
        return id;
    }

    public Resource getResource() {
        return resource;
    }

    public PermissionAction getAction() {
        return action;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Permission that)) return false;
        return resource == that.resource && action == that.action;
    }

    @Override
    public int hashCode() {
        return Objects.hash(resource, action);
    }

    @Override
    public String toString() {
        return "Permission{resource=" + resource + ", action=" + action + "}";
    }
}

