package org.cttelsamicsterrassa.data.core.domain.model.auth;

import org.albertsanso.commons.model.Entity;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class Role extends Entity {

    private final UUID id;
    private final String name;
    private final Set<Permission> permissions;

    private Role(UUID id, String name, Set<Permission> permissions) {
        this.id = id;
        this.name = normalizeName(name);
        this.permissions = new HashSet<>(permissions);
    }

    public static Role createNew(String name, Set<Permission> permissions) {
        return new Role(UUID.randomUUID(), name, permissions);
    }

    public static Role createExisting(UUID id, String name, Set<Permission> permissions) {
        return new Role(id, name, permissions);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Permission> getPermissions() {
        return Collections.unmodifiableSet(permissions);
    }

    public void addPermission(Permission permission) {
        permissions.add(permission);
    }

    public boolean hasPermission(Resource resource, PermissionAction action) {
        return permissions.stream()
                .anyMatch(p -> p.getResource() == resource && p.getAction() == action);
    }

    private static String normalizeName(String name) {
        return name.trim().toUpperCase();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role that)) return false;
        return Objects.equals(normalizeName(name), normalizeName(that.name));
    }

    @Override
    public int hashCode() {
        return Objects.hash(normalizeName(name));
    }

    @Override
    public String toString() {
        return "Role{name=" + name + "}";
    }
}

