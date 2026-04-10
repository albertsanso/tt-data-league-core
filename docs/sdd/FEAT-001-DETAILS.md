# Build Plan

## Phase 1: Dependencies & Configuration (30 min)
1. Add security dependencies to `tt-data-league-core-domain/pom.xml`:
    - `org.springframework.security:spring-security-crypto` (for bcrypt hashing)
    - `commons-codec` (for encoding utilities if needed)
2. Add test dependencies:
    - `org.junit.jupiter:junit-jupiter-api` and `junit-jupiter-engine`
    - `org.mockito:mockito-core` and `mockito-junit-jupiter`

## Phase 2: Domain Model - User Entity (1.5h)
1. Create `org.cttelsamicsterrassa.data.core.domain.model.auth` package
2. Create `User` class extending `Entity`:
    - Fields: `id` (UUID), `username` (String, unique), `email` (String, unique), `passwordHash` (String), `createdAt` (LocalDateTime), `isActive` (boolean)
    - Factory methods: `createNew(username, email, plainPassword)` and `createExisting(id, username, email, passwordHash, createdAt, isActive)`
    - Methods:
        - `verifyPassword(plainPassword): boolean` - compares plain password with hash
        - `changePassword(newPlainPassword): void` - updates password hash
        - `disable(): void` - deactivates user
        - `enable(): void` - activates user
    - Use `User` constructor pattern consistent with existing entities (private constructor)

3. Create `PasswordHasher` interface in `org.cttelsamicsterrassa.data.core.domain.auth.service`:
    - Method: `hash(plainPassword): String`
    - Method: `verify(plainPassword, hash): boolean`

4. Create `BcryptPasswordHasher` implementation in domain service package (Spring-agnostic):
    - Implement secure bcrypt hashing using `org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder`
    - Use strength factor of 12 (reasonable security vs performance)

## Phase 3: Repository Interfaces (45 min)
1. Create `UserRepository` interface in `org.cttelsamicsterrassa.data.core.domain.repository.auth`:
    - Methods:
        - `findById(UUID id): Optional<User>`
        - `findByUsername(String username): Optional<User>`
        - `findByEmail(String email): Optional<User>`
        - `findAll(): List<User>`
        - `existsByUsername(String username): boolean`
        - `existsByEmail(String email): boolean`
        - `save(User user): void`
        - `delete(User user): void`
        - `deleteById(UUID id): void`

2. Create `PasswordHasherRepository` interface (dependency injection point):
    - Single method: `getHasher(): PasswordHasher`

## Phase 4: Domain Service (1h)
1. Create `AuthenticationService` in `org.cttelsamicsterrassa.data.core.domain.service.auth`:
    - Dependencies: `UserRepository`, `PasswordHasherRepository`
    - Methods:
        - `registerUser(username, email, plainPassword): User`
            - Validate inputs (username/email uniqueness, password strength)
            - Hash password
            - Create and save user
            - Return created user
        - `authenticateUser(username, plainPassword): Optional<User>`
            - Find user by username
            - Verify password
            - Return user if valid
        - `getUserByUsername(String username): Optional<User>`
        - `getUserByEmail(String email): Optional<User>`
        - `disableUser(UUID userId): void`
        - `enableUser(UUID userId): void`
        - `changeUserPassword(UUID userId, newPlainPassword): void`

2. Create `UserValidator` utility class:
    - Validation methods:
        - `validateUsername(String): List<String>` (check length 3-20, alphanumeric + underscore)
        - `validateEmail(String): List<String>` (basic email format)
        - `validatePassword(String): List<String>` (min 8 chars, mix of upper/lower/numbers/special)
    - Throw `ValidationException` on validation failure

## Phase 5: Unit Tests (2h)
1. Create test class `UserTest`:
    - Test user creation and factory methods
    - Test password verification
    - Test password change
    - Test user enable/disable state

2. Create test class `BcryptPasswordHasherTest`:
    - Test password hashing consistency
    - Test password verification success/failure
    - Test different passwords produce different hashes

3. Create test class `AuthenticationServiceTest`:
    - Mock `UserRepository` and `PasswordHasherRepository`
    - Test user registration success
    - Test duplicate username/email rejection
    - Test user authentication success/failure
    - Test password validation
    - Test user disable/enable operations

4. Create test class `UserValidatorTest`:
    - Test username validation (valid/invalid cases)
    - Test email validation (valid/invalid cases)
    - Test password validation (strong/weak passwords)

## Phase 6: Documentation & Integration Points (45 min)
1. Document all public interfaces in Javadoc
2. Create configuration class outline (for future Spring integration):
    - `org.cttelsamicsterrassa.data.core.domain.auth.config.AuthenticationConfiguration` (stub)
    - Document how repositories and password hasher should be provided

3. Update module AGENT.md (`tt-data-league-core-domain/AGENT.md`):
    - Add "Authentication Module" section under Domain Models
    - Document User entity, repositories, and services
    - Note: JPA implementation will be in `tt-data-league-core-repository-jpa`

4. Create or update `prompts/` with authentication-related prompts for future agent work

# Implementation Guidelines

## Code Style & Patterns
- Follow existing entity patterns (UUID, factory methods, private constructors)
- Use `Optional<T>` for repository queries instead of nullable returns
- Document all public methods with Javadoc
- Use meaningful exception types (`UserAlreadyExistsException`, `InvalidCredentialsException`, `ValidationException`)

## Security Considerations
- Never log or expose password hashes
- Always use bcrypt (never plain text or MD5/SHA)
- Use bcrypt strength 12 (reasonable for typical loads)
- Store salt as part of the hash (bcrypt does this automatically)
- Implement rate limiting at API layer (future work)
- Never return detailed auth failure info to users

## Testing Approach
- Use unit tests only (no database integration needed yet)
- Mock repository implementations
- Test both success and failure paths
- Test edge cases (empty strings, null values, boundary lengths)

# Notes
- Consider using an existing library for password hashing to ensure security best practices.
- Consider using a package `auth` under the `org.cttelsamicsterrassa.data.core.domain` module for the user model and authentication logic.
- Total estimated effort breakdown: Phase 1 (30 min) + Phase 2 (1.5h) + Phase 3 (45 min) + Phase 4 (1h) + Phase 5 (2h) + Phase 6 (45 min) = ~6.5h
- Phase 1-5 are required for core acceptance criteria; Phase 6 is for integration readiness
