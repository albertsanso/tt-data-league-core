package org.cttelsamicsterrassa.data.core.domain.service.auth;

import org.cttelsamicsterrassa.data.core.domain.model.auth.Permission;
import org.cttelsamicsterrassa.data.core.domain.model.auth.PermissionAction;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Resource;
import org.cttelsamicsterrassa.data.core.domain.model.auth.Role;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public final class RbacCatalog {

    public static final String ADMIN = "ADMIN";
    public static final String CLUB_MANAGER = "CLUB_MANAGER";
    public static final String PRACTITIONER = "PRACTITIONER";
    public static final String GUEST = "GUEST";
    public static final String ANALYST = "ANALYST";

    private static final List<Resource> ALL_RESOURCES = Arrays.asList(Resource.values());

    private RbacCatalog() {
    }

    public static String defaultRoleName() {
        return GUEST;
    }

    public static List<Role> predefinedRoles() {
        Set<Permission> allPermissions = allPermissions();
        return List.of(
                Role.createNew(ADMIN, allPermissions),
                Role.createNew(CLUB_MANAGER, allPermissions),
                Role.createNew(PRACTITIONER, filterAllResourcesReadPermissions(allPermissions)),
                Role.createNew(GUEST, filter(allPermissions, List.of(Resource.CLUB, Resource.PRACTITIONER), List.of(PermissionAction.READ))),
                Role.createNew(ANALYST, filterAllResourcesReadPermissions(allPermissions))
        );
    }

    private static Set<Permission> allPermissions() {
        return ALL_RESOURCES.stream()
                .flatMap(resource -> Arrays.stream(PermissionAction.values())
                        .map(action -> Permission.createNew(resource, action)))
                .collect(Collectors.toSet());
    }

    private static Set<Permission> filterPermissionsByAction(Set<Permission> permissions, PermissionAction action) {
        return filter(permissions, ALL_RESOURCES, List.of(action));
    }

    private static Optional<Permission> filterPermissionsByResourceAndAction(Set<Permission> permissions, Resource resource, PermissionAction action) {
        return filter(permissions, List.of(resource), List.of(action)).stream().findFirst();
    }

    private static Set<Permission> filter(Set<Permission> permissions, List<Resource> resources, List<PermissionAction> actions) {
        return permissions.stream().filter(permission ->
                resources.contains(permission.getResource())
                        && actions.contains(permission.getAction()))
                .collect(Collectors.toSet());
    }

    private static Set<Permission> filterAllResourcesReadPermissions(Set<Permission> permissions) {
        return filterPermissionsByAction(permissions, PermissionAction.READ);
    }

    private static Set<Permission> filterAllResourcesWritePermissions(Set<Permission> permissions) {
        return filterPermissionsByAction(permissions, PermissionAction.WRITE);
    }

    private static Set<Permission> filterAllResourcesDeletePermissions(Set<Permission> permissions) {
        return filterPermissionsByAction(permissions, PermissionAction.DELETE);
    }
}

