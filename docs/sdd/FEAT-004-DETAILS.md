# FEAT-004 — Add Roles and Permissions to User Model

## Context

The compiled class files in `tt-data-league-core-domain/target/` reveal that the domain layer
design for this feature was previously established and built. Source files are **absent** from
`src/` and must be recreated from the known compiled contracts. The JPA infrastructure layer has
**not yet been implemented**.

This build plan covers:
1. Recreating and finalising the missing domain source files (matching the compiled design).
2. Implementing the full JPA persistence layer for `Role` and `Permission`.
3. Extending `UserJPA` to persist user-role relationships.
4. Updating `AuthenticationService` to wire in RBAC behaviour.
5. Adding domain unit tests and infrastructure integration tests.

---

## Build Plan

### Phase 1 — Domain Enums (resource and action vocabulary)

1. Create `PermissionAction` enum in `model/auth/`:
   - Constants: `READ`, `WRITE`, `DELETE`
   - Follow the project enum pattern: `private final String value`, `getValue()`, `fromValue(String)` with `UNKNOWN` fallback.

2. Create `Resource` enum in `model/auth/`:
   - Constants: `CLUB`, `PRACTITIONER`, `CLUB_MEMBER`, `SEASON_PLAYER`, `SEASON_PLAYER_RESULT`, `MATCH`
   - Same enum pattern as `PermissionAction`.

### Phase 2 — Domain Entities: `Permission` and `Role`

3. Create `Permission` entity in `model/auth/`:
   - Fields: `private final UUID id`, `private final Resource resource`, `private final PermissionAction action`
   - Factory methods: `createNew(Resource, PermissionAction)` and `createExisting(UUID, Resource, PermissionAction)`
   - Override `equals` and `hashCode` by `(resource, action)` — two permissions are identical if they target the same resource and action.
   - Getter: `getId()`, `getResource()`, `getAction()`

4. Create `Role` entity in `model/auth/`:
   - Fields: `private final UUID id`, `private final String name`, `private final Set<Permission> permissions`
   - Initialise `permissions` as a mutable `HashSet` internally; expose via `Collections.unmodifiableSet(permissions)`.
   - Factory methods: `createNew(String name, Set<Permission> permissions)` and `createExisting(UUID id, String name, Set<Permission> permissions)`
   - Command method: `addPermission(Permission permission)` — guards against duplicates.
   - Query method: `hasPermission(Resource resource, PermissionAction action): boolean` — checks whether any stored permission matches both resource and action.
   - Override `equals` and `hashCode` by normalised `name` (case-insensitive); this allows set membership tests by role name.
   - Private static helper: `normalizeName(String name)` — `name.trim().toUpperCase()`

### Phase 3 — Extend `User` with RBAC fields

5. Update `User` entity in `model/auth/`:
   - Add `private final Set<Role> roles` — mutable `HashSet`, initialised empty.
   - Update private constructor to accept `Set<Role> roles`.
   - `createNew(...)` continues to accept only `username, email, plainPassword`; roles set is initialised empty.
   - Add overloaded `createExisting(UUID, String, String, String, LocalDateTime, boolean, Set<Role>)` — used when reconstructing from JPA.
   - Keep the existing `createExisting(UUID, String, String, String, LocalDateTime, boolean)` overload for backward compatibility (empty roles set).
   - Expose: `getRoles(): Set<Role>` — returns `Collections.unmodifiableSet(roles)`.
   - Command method: `assignRole(Role role)` — adds role if not already present (check by normalised name).
   - Query method: `hasRole(String roleName): boolean` — checks whether any role matches the normalised name.
   - Query method: `hasPermission(Resource resource, PermissionAction action): boolean` — delegates to roles.

### Phase 4 — Repository Contracts

6. Create `PermissionRepository` interface in `repository/auth/`:
   ```
   Optional<Permission> findById(UUID id)
   Optional<Permission> findByResourceAndAction(Resource resource, PermissionAction action)
   List<Permission> findAll()
   void save(Permission permission)
   void saveAll(List<Permission> permissions)
   void deleteById(UUID id)
   ```

7. Create `RoleRepository` interface in `repository/auth/`:
   ```
   Optional<Role> findById(UUID id)
   Optional<Role> findByName(String name)
   List<Role> findAll()
   boolean existsByName(String name)
   void save(Role role)
   void saveAll(List<Role> roles)
   void deleteById(UUID id)
   ```

### Phase 5 — Domain Utilities and Exceptions

8. Create `RbacCatalog` utility class in `service/auth/`:
   - Constants (Strings): `ADMIN`, `CLUB_MANAGER`, `PRACTITIONER`, `GUEST`, `ANALYST`
   - `public static String defaultRoleName()` — returns `GUEST`
   - `public static List<Role> predefinedRoles()` — returns the five standard roles with their canonical permission sets:
     - `ADMIN` → all `READ`, `WRITE`, `DELETE` on all resources
     - `CLUB_MANAGER` → `READ`, `WRITE`, `DELETE` on all resources
     - `PRACTITIONER` → `READ` on all resources
     - `GUEST` → `READ` on `CLUB` and `PRACTITIONER` only
     - `ANALYST` → `READ` on all resources
   - Private helpers: `allPermissions()`, `readPermissions(List<Resource>)`
   - No-arg private constructor (utility class must not be instantiated).

9. Create `AuthorizationException` in `service/auth/`:
   - Extends `RuntimeException`
   - Single-arg constructor: `AuthorizationException(String message)`

### Phase 6 — Update `AuthenticationService`

10. Inject `RoleRepository` into `AuthenticationService` (constructor injection after `UserRepository`).
11. In `registerUser(...)`:
    - After creating the user, look up the default role by `RbacCatalog.defaultRoleName()`.
    - If the default role is found: call `user.assignRole(role)`.
    - Save the user (role association persisted through the user save).
12. In `authenticateUser(...)`:
    - After fetching the user, eagerly load and assign roles via `roleRepository.findByName(...)` or rely on the mapper to restore roles from persistence (preferred — mapper handles full hydration).
13. Add method `assignRole(UUID userId, String roleName)`:
    - Load user by `userId` (throw `InvalidCredentialsException` if absent).
    - Load role by `roleName` (throw `IllegalArgumentException` if absent).
    - Call `user.assignRole(role)`.
    - Save user.
14. Add method `userHasPermission(UUID userId, Resource resource, PermissionAction action): boolean`:
    - Load user; return `false` if absent.
    - Delegate to `user.hasPermission(resource, action)`.
15. Add method `assertAuthorized(UUID userId, Resource resource, PermissionAction action): void`:
    - Calls `userHasPermission(...)`; throws `AuthorizationException` if not authorized.

### Phase 7 — Domain Unit Tests

16. Create `PermissionTest` in `test/.../model/auth/`:
    - `createNewAssignsUniqueId`
    - `equalsIsTrueForSameResourceAndAction` (two different instances with same resource+action)

17. Create `RoleTest` in `test/.../model/auth/`:
    - `createNewAssignsUniqueId`
    - `hasPermissionReturnsTrueForAssignedPermission`
    - `hasPermissionReturnsFalseForMissingPermission`
    - `addPermissionDeduplicatesExistingPermission`

18. Create `UserRbacTest` in `test/.../model/auth/`:
    - `assignRoleAddsRoleToUser`
    - `assignRoleDeduplicatesExistingRole`
    - `hasRoleReturnsTrueForAssignedRole`
    - `hasRoleReturnsFalseForUnassignedRole`
    - `hasPermissionDelegatesAcrossRoles`
    - `hasPermissionReturnsFalseWhenNoRolesAssigned`

19. Update `AuthenticationServiceTest` to cover:
    - `registerUserAssignsDefaultRoleWhenRoleExists`
    - `registerUserDoesNotFailWhenDefaultRoleAbsent` (graceful degradation)
    - `assignRoleThrowsWhenUserNotFound`
    - `assignRoleThrowsWhenRoleNotFound`
    - `assertAuthorizedThrowsAuthorizationExceptionWhenNotPermitted`
    - `userHasPermissionReturnsFalseWhenUserAbsent`

### Phase 8 — JPA Infrastructure: `Permission` and `Role`

20. Create `PermissionJPA` in `repository/jpa/auth/model/`:
    - Table: `Permission`
    - Columns: `id` (UUID, PK), `resource` (VARCHAR), `action` (VARCHAR)
    - Unique constraint on `(resource, action)`

21. Create `RoleJPA` in `repository/jpa/auth/model/`:
    - Table: `Role`
    - Columns: `id` (UUID, PK), `name` (VARCHAR, unique)
    - `@ManyToMany` to `PermissionJPA` via join table `Role_Permission` (`role_id`, `permission_id`)

22. Update `UserJPA` in `repository/jpa/auth/model/`:
    - Add `@ManyToMany` relationship to `RoleJPA` via join table `AppUser_Role` (`user_id`, `role_id`).
    - Expose setter/getter `roles`.

### Phase 9 — JPA Mappers: `Permission` and `Role`

23. Create `PermissionToPermissionJPAMapper` in `repository/jpa/auth/mapper/`:
    - `Function<Permission, PermissionJPA>` — maps domain to JPA.

24. Create `PermissionJPAToPermissionMapper` in `repository/jpa/auth/mapper/`:
    - `Function<PermissionJPA, Permission>` — maps JPA to domain via `Permission.createExisting(...)`.

25. Create `RoleToRoleJPAMapper` in `repository/jpa/auth/mapper/`:
    - `Function<Role, RoleJPA>` — maps domain role and its permission set to JPA.

26. Create `RoleJPAToRoleMapper` in `repository/jpa/auth/mapper/`:
    - `Function<RoleJPA, Role>` — maps JPA to domain via `Role.createExisting(...)`.
    - Delegates permission mapping to `PermissionJPAToPermissionMapper`.

27. Update `UserJPAToUserMapper` — call `User.createExisting(id, ..., roles)` using mapped roles.
28. Update `UserToUserJPAMapper` — map domain roles to `RoleJPA` and set on `UserJPA`.

### Phase 10 — JPA Repository Helpers and Implementations

29. Create `PermissionRepositoryHelper` in `repository/jpa/auth/impl/`:
    - Extends `JpaRepository<PermissionJPA, UUID>`
    - Custom query: `Optional<PermissionJPA> findByResourceAndAction(String resource, String action)`

30. Create `RoleRepositoryHelper` in `repository/jpa/auth/impl/`:
    - Extends `JpaRepository<RoleJPA, UUID>`
    - Custom queries: `Optional<RoleJPA> findByNameIgnoreCase(String name)`, `boolean existsByNameIgnoreCase(String name)`

31. Create `PermissionRepositoryImpl` in `repository/jpa/auth/impl/`:
    - Implements `PermissionRepository`
    - Constructor-injected: `PermissionRepositoryHelper`, `PermissionJPAToPermissionMapper`, `PermissionToPermissionJPAMapper`

32. Create `RoleRepositoryImpl` in `repository/jpa/auth/impl/`:
    - Implements `RoleRepository`
    - Constructor-injected: `RoleRepositoryHelper`, `RoleJPAToRoleMapper`, `RoleToRoleJPAMapper`

### Phase 11 — Infrastructure Integration Tests

33. Create `RoleRepositoryImplTest` in `test/.../repository/jpa/auth/impl/`:
    - Spring Boot test with `@EntityScan` and `@EnableJpaRepositories`.
    - `findByNameReturnsSavedRole`
    - `findByNameReturnsEmptyWhenAbsent`
    - `existsByNameReturnsTrueWhenRoleExists`
    - `saveAllPersistsMultipleRoles`
    - Test that a role with permissions can be saved and re-loaded with permissions intact.

34. Create `PermissionRepositoryImplTest` in `test/.../repository/jpa/auth/impl/`:
    - `findByResourceAndActionReturnsCorrectPermission`
    - `findByResourceAndActionReturnsEmptyWhenAbsent`
    - `saveAllPersistsMultiplePermissions`

35. Update `UserRepositoryImplTest` (or add role coverage to auth suite):
    - `saveUserWithRolesAndReloadPreservesRoles`

### Phase 12 — Build Verification

36. Run `mvn test -pl tt-data-league-core-domain` — all domain tests must pass.
37. Run `mvn test -pl tt-data-league-core-repository-jpa` — all infrastructure tests must pass.
38. Run `mvn test` from root — full test suite must pass with no regressions.

---

## Implementation Guidelines

### Code Style & Patterns

- Follow the suffix conventions from `AGENTS.md`:  
  `PermissionJPA`, `RoleJPA`, `PermissionToPermissionJPAMapper`, `PermissionJPAToPermissionMapper`,  
  `RoleRepositoryHelper`, `PermissionRepositoryImpl` etc.
- `RbacCatalog` is a utility class: `private` no-arg constructor, all `public static` methods, no Spring annotations.
- `AuthorizationException` and `AuthenticationService` changes stay co-located in `service/auth/`.
- Keep enum patterns consistent with existing enums in the codebase (value field, `getValue()`, `fromValue()`, `UNKNOWN` fallback).

### Security Considerations

- Never expose `Role` or `Permission` objects from `UserJPA` outside the mapper boundary.
- `RbacCatalog.predefinedRoles()` produces new transient `Role` objects each call — callers must persist them via `RoleRepository.saveAll(...)` during seeding; the catalog is not a singleton store.
- `assertAuthorized` should produce an `AuthorizationException` with a message that does not leak internal role names to untrusted callers in production contexts.

### Testing Approach

- Domain tests (`PermissionTest`, `RoleTest`, `UserRbacTest`): pure JUnit 5. No mocks needed — entity logic is deterministic.
- `AuthenticationServiceTest` additions: follow the existing `@Mock` + explicit constructor instantiation pattern.  
  Mock both `UserRepository` and `RoleRepository`.
- Infrastructure tests: follow the `PlayersSingleMatchRepositoryImplTest` pattern — `@SpringBootTest` with `@EntityScan`, `@EnableJpaRepositories`, real in-memory database, `@BeforeEach` cleanup.

---

## Notes

- **Compiled class contracts are authoritative.** If the source recreated in Phase 1–6 matches the compiled API exactly, no other module should break.
- The `UserJPA` change (adding `@ManyToMany`) requires schema migration in any environment that already has the `AppUser` table. Document and handle this during deployment.
- `RbacCatalog.predefinedRoles()` is used both for testing and for application seed data. An application startup seeder (out of scope for this feature) should call `RoleRepository.saveAll(RbacCatalog.predefinedRoles())` only when roles are absent.
- The `PRACTITIONER` role name in `RbacCatalog` overlaps with the `Practicioner` domain entity name (the entity uses the legacy `Practicioner` spelling). Use `PRACTITIONER` (standard spelling) in the role catalog to avoid confusion — these are separate concepts.
- Future work: consider a `RoleSeeder` service in a separate feature to automate database seeding of the predefined roles on application start.

