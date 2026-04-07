# tt-data-league-core-domain Module - Agent Guide

## Module Overview

**Module Name:** `tt-data-league-core-domain`  
**Module Type:** Core Domain Module (Pure Domain Logic)  
**Package Root:** `org.cttelsamicsterrassa.data.core.domain`  
**Version:** 0.0.1-SNAPSHOT  
**Java Target:** Java 21  

This module contains the core domain models, repository interfaces, and domain services for the table tennis league data management system. It follows a domain-driven design (DDD) approach with clean separation of concerns.

---

## Architecture & Design Patterns

### Layer Structure
```
domain/
├── model/           # Domain entities and value objects
├── repository/      # Repository interfaces (abstraction layer)
└── service/         # Domain services and utilities
```

### Design Principles
- **Domain-Driven Design (DDD):** Domain models are pure Java objects without infrastructure dependencies
- **Repository Pattern:** Abstracts data persistence (implementation in `tt-data-league-core-repository-jpa`)
- **Entity-based Design:** Core domain uses entity objects extending the base `Entity` class from commons-core
- **UUID as Identity:** All entities use UUID for unique identification
- **Factory Pattern:** Static factory methods (`createNew`, `createExisting`) for entity instantiation

---

## Domain Models

### Core Entities

#### 1. **Club** (`model/Club.java`)
- **Purpose:** Represents a table tennis club
- **Identity:** UUID-based
- **Key Attributes:**
  - `id`: UUID (unique identifier)
  - `name`: String (club name, unique)
  - `yearRanges`: List<String> (seasons in which club participates)
- **Key Methods:**
  - `modifyName(String)`: Update club name
  - `addYearRange(String)`: Add a season year range
  - `removeYearRange(String)`: Remove a season year range
  - `addYearRanges(List<String>)`: Bulk add year ranges
- **Factory Methods:**
  - `createNew(String name)`: Create new club with random UUID
  - `createExisting(UUID id, String name)`: Create from persisted data
- **Extends:** `Entity` from commons-core

#### 2. **Practicioner** (`model/Practicioner.java`)
- **Purpose:** Represents a person participating in table tennis activities
- **Identity:** UUID-based
- **Key Attributes:**
  - `id`: UUID
  - `firstName`: String
  - `secondName`: String
  - `fullName`: String
  - `birthDate`: Date
- **Factory Methods:**
  - `createNew(...)`: Create new practitioner
  - `createExisting(...)`: Create from persisted data
- **Extends:** `Entity` from commons-core

#### 3. **ClubMember** (`model/ClubMember.java`)
- **Purpose:** Represents the association between a Practicioner and a Club
- **Identity:** UUID-based
- **Key Attributes:**
  - `id`: UUID
  - `practicioner`: Practicioner reference
  - `club`: Club reference
  - `memberNumber`: String (optional)
  - `entryDate`: Date
- **Purpose:** Links practitioners to clubs with temporal information

#### 4. **License** (`model/License.java`)
- **Purpose:** Represents a table tennis license (Federation membership)
- **Key Attributes:**
  - License identifier/number
  - License status
  - Validity period information
- **Use Case:** Track licensing status for season players

#### 5. **SeasonPlayer** (`model/SeasonPlayer.java`)
- **Purpose:** Represents a player registered for a specific season
- **Identity:** UUID-based
- **Key Attributes:**
  - `id`: UUID
  - `clubMember`: ClubMember reference
  - `license`: License reference
  - `yearRange`: String (e.g., "2023-2024")
- **Key Methods:**
  - Factory methods for creation
- **Purpose:** Links club members to seasons with their license information

#### 6. **SeasonPlayerResult** (`model/SeasonPlayerResult.java`)
- **Purpose:** Records match results for a season player
- **Key Attributes:**
  - Player performance metrics
  - Match outcomes
  - Competition results
- **Used For:** Tracking seasonal player performance

#### 7. **CompetitionInfo** (`model/CompetitionInfo.java`)
- **Purpose:** Contains competition metadata
- **Key Attributes:**
  - `competitionType()`: Type of competition (league, cup, etc.)
  - `competitionCategory()`: Category/division
  - `competitionGroup()`: Group classification
- **Record Type:** Value object (record-based)

#### 8. **MatchInfo** (`model/MatchInfo.java`)
- **Purpose:** Represents a match event
- **Key Attributes:**
  - Match details and scheduling
  - Team information
  - Match status

#### 9. **PlayersSingleMatch** (`model/PlayersSingleMatch.java`)
- **Purpose:** Records individual player performance in a single match
- **Key Attributes:**
  - Player reference
  - Match results
  - Performance metrics
- **Use Case:** Detailed match statistics per player

#### 10. **TeamRole** (`model/TeamRole.java`)
- **Purpose:** Defines roles within team context (e.g., player, captain, substitute)
- **Value Object:** Role classification

---

## Repository Interfaces

Located in `repository/` package. These define contracts for data persistence without implementation details.

### Key Repositories

1. **ClubRepository**
   - `findById(UUID)`: Find club by ID
   - `findByName(String)`: Find club by name
   - `findAll()`: Retrieve all clubs
   - `save(Club)`: Persist club
   - `update(Club)`: Update existing club
   - `delete(UUID)`: Delete club

2. **PracticionerRepository**
   - `findById(UUID)`: Find practitioner
   - `findAll()`: List all practitioners
   - CRUD operations

3. **ClubMemberRepository**
   - `findById(UUID)`: Find club member
   - `findByClubId(UUID)`: Find members of a club
   - `findByPracticionerId(UUID)`: Find club memberships of a person
   - CRUD operations

4. **SeasonPlayerRepository**
   - `findById(UUID)`: Find season player record
   - `findByYearRange(String)`: Find players in a season
   - `findByClubId(UUID)`: Find club players in season
   - CRUD operations

5. **SeasonPlayerResultRepository**
   - `findById(UUID)`: Find result record
   - `findBySeasonPlayer(UUID)`: Results for a player
   - CRUD operations

6. **PlayersSingleMatchRepository**
   - `findById(UUID)`: Find match player record
   - `findByMatch(UUID)`: Find all player records in match
   - CRUD operations

---

## Domain Services

Located in `service/` package. These contain domain logic that spans multiple entities.

### 1. **SeasonPlayerResultUniqueKeyBuilder** (`service/SeasonPlayerResultUniqueKeyBuilder.java`)
- **Purpose:** Generates unique keys for season player results
- **Method:** `buildUniqueKey(SeasonPlayer, CompetitionInfo, int, String)`
- **Format:** `yearRange-competitionType-competitionCategory-matchDayNumber-competitionGroup-license-matchPlayerLetter`
- **Use Case:** Uniquely identify player results across matches and competitions
- **Example:** `2023-2024-LEAGUE-MALE-15-GROUPA-123456-A`

### 2. **SeasonRangeValidator** (`service/SeasonRangeValidator.java`)
- **Purpose:** Validates season year range format and logic
- **Validations:** Format compliance, logical date ranges
- **Use Case:** Ensure consistency of season identifiers

---

## Dependencies

### Internal Dependencies
- **commons-core** (org.albertsanso:commons-core:0.0.1-SNAPSHOT)
  - Provides base `Entity` class
  - Common utilities and abstractions
  - Referenced classes: `Entity`, base model utilities

### External Dependencies
- **Spring Boot Starter** (org.springframework.boot:spring-boot-starter)
  - Version: 3.5.8 (managed by parent POM)
  - Provides dependency injection and framework support
  - **Note:** Domain module has minimal Spring dependencies by design
  - Repository interfaces are annotated for Spring integration but don't require Spring runtime

---

## Key Concepts for Agentic Development

### 1. Entity Creation Patterns
Always use factory methods when creating domain entities:
```java
// New entity with generated UUID
Club club = Club.createNew("Club Name");

// Existing entity (from database)
Club club = Club.createExisting(id, "Club Name");
```

### 2. Repository Abstraction
- Repository interfaces define data contracts
- **DO NOT** add persistence logic to domain models
- Repository implementations are in `tt-data-league-core-repository-jpa` module
- Repositories are dependencies injected into services

### 3. Domain Services
- Encapsulate cross-cutting domain logic
- Operate on domain entities
- Stateless by design
- Used by application services (outside this module)

### 4. UUID as Identity
- All entities use `java.util.UUID` for identity
- Always generate new UUIDs for new entities: `UUID.randomUUID()`
- Persist UUIDs as VARCHAR in database

### 5. Validation Points
- **SeasonPlayerResultUniqueKeyBuilder:** Use to generate consistent unique identifiers
- **SeasonRangeValidator:** Validate year range formats before persistence
- **Repository methods:** Implement uniqueness constraints (e.g., Club.name is unique)

---

## Common Development Tasks

### Adding a New Domain Entity
1. Create entity class in `model/` extending `Entity`
2. Implement factory methods (`createNew`, `createExisting`)
3. Use UUID for identity with `java.util.UUID`
4. Add domain logic methods (avoid setters; use intent-revealing commands)
5. Create corresponding repository interface in `repository/`

### Adding Domain Logic
1. If logic spans multiple entities → Create domain service in `service/`
2. If logic is single-entity → Add to entity class itself
3. Keep services stateless
4. Use descriptive method names that reveal intent

### Working with Repositories
- Repository interfaces are contracts only
- Implementations are in `tt-data-league-core-repository-jpa`
- Use `Optional<T>` for nullable results
- List-based queries should return `List<T>`

---

## Testing Considerations

### Unit Testing
- Test domain entities for invariant enforcement
- Test factory methods for correct initialization
- Test domain services independently with mock repositories
- No database dependencies in unit tests

### Integration Testing
- Test repository contracts with actual database
- Located in `tt-data-league-core-repository-jpa` module
- Use Spring Boot test context for full integration

### Mocking Strategy
- Mock repositories in domain service tests
- Use factory methods to create test entities
- Don't rely on persistence in unit tests

---

## Extension Points

### Future Enhancements
1. **Event Sourcing:** Add domain events to track entity changes
2. **Aggregate Patterns:** Group related entities into aggregates (e.g., Club + ClubMembers)
3. **Value Objects:** Create value objects for common concepts (e.g., PlayerName, DateRange)
4. **Specifications:** Complex query logic for repositories
5. **Validation Rules:** Centralized validation for domain constraints

### Related Modules
- **tt-data-league-core-repository-jpa:** JPA implementation of repositories
- **commons-core:** Shared base classes and utilities
- Consumer modules will depend on this domain module

---

## Guidelines for Agentic Development

### ✅ DO's
- Keep domain models clean and focused on business logic
- Use intent-revealing names for methods
- Leverage factory methods for entity creation
- Delegate persistence to repositories
- Write domain services for cross-entity logic
- Use UUID for all entity identities
- Document complex business rules in code

### ❌ DON'Ts
- Don't add Spring annotations to domain entities (except base class)
- Don't mix persistence logic with domain logic
- Don't use setters; use command methods instead
- Don't add database-specific logic to models
- Don't create circular dependencies between models
- Don't make repositories concrete in this module

---

## File Organization Reference

```
src/main/java/org/cttelsamicsterrassa/data/core/domain/
├── model/
│   ├── Club.java
│   ├── Practicioner.java
│   ├── ClubMember.java
│   ├── License.java
│   ├── SeasonPlayer.java
│   ├── SeasonPlayerResult.java
│   ├── CompetitionInfo.java
│   ├── MatchInfo.java
│   ├── PlayersSingleMatch.java
│   └── TeamRole.java
├── repository/
│   ├── ClubRepository.java
│   ├── PracticionerRepository.java
│   ├── ClubMemberRepository.java
│   ├── SeasonPlayerRepository.java
│   ├── SeasonPlayerResultRepository.java
│   └── PlayersSingleMatchRepository.java
└── service/
    ├── SeasonPlayerResultUniqueKeyBuilder.java
    └── SeasonRangeValidator.java
```

---

## Quick Reference: Key Classes and Their Roles

| Class | Role | Extends | Key Method |
|-------|------|---------|-----------|
| Club | Domain Entity | Entity | `modifyName()`, `addYearRange()` |
| Practicioner | Domain Entity | Entity | `createNew(...)` |
| ClubMember | Domain Entity | UUID-based | Links practitioner to club |
| License | Value Object | - | License tracking |
| SeasonPlayer | Domain Entity | UUID-based | Links member to season |
| SeasonPlayerResult | Domain Entity | UUID-based | Performance tracking |
| CompetitionInfo | Value Object/Record | - | Competition metadata |
| ClubRepository | Interface | - | CRUD for Club |
| SeasonPlayerResultUniqueKeyBuilder | Service | - | `buildUniqueKey()` |

---

## Authentication Slice (FEAT-001)

### New Packages
- `model/auth`: `User` entity with UUID identity, email/username, bcrypt hash, created timestamp, active flag
- `repository/auth`: `UserRepository` contract for lookup, uniqueness checks, persistence, and delete methods
- `service/auth`: `AuthenticationService`, `PasswordHasher`, `BcryptPasswordHasher`, `UserValidator`, auth exceptions

### Core Flow
1. `AuthenticationService.registerUser(...)` validates input and uniqueness.
2. Password is hashed through `PasswordHasher` (bcrypt implementation provided).
3. Persisted through `UserRepository` (implemented in JPA module).
4. `authenticateUser(...)` returns `Optional<User>` only for active users with matching hash.

### Testing Surface
- `UserTest`: entity creation, password verification/change, enable/disable lifecycle
- `BcryptPasswordHasherTest`: salt/hash behavior and verification
- `AuthenticationServiceTest`: registration and authentication behavior with mocked repository
- `UserValidatorTest`: username/email/password validation rules

---

## Version Information
- **Module Version:** 0.0.1-SNAPSHOT
- **Java Version:** 21
- **Build Tool:** Maven
- **Spring Boot Version:** 3.5.8
- **Last Updated:** 2026-03-24

