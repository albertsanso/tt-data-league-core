# tt-data-league-core-repository-jpa Module - Agent Guide

## Module Overview

**Module Name:** `tt-data-league-core-repository-jpa`  
**Module Type:** Data Access Layer / Repository Implementation  
**Package Root:** `org.cttelsamicsterrassa.data.core.repository`  
**Version:** 0.0.1-SNAPSHOT  
**Java Target:** Java 21  
**Framework:** Spring Boot 3.5.8 with Spring Data JPA  

This module provides JPA-based implementations of the repository interfaces defined in `tt-data-league-core-domain`. It handles all database persistence logic using Hibernate/JPA, entity mapping, and query specifications.

---

## Architecture & Design Patterns

### Layer Structure
```
repository/jpa/
├── club/
│   ├── impl/           # Repository implementations
│   ├── mapper/         # Domain ↔ JPA entity mappers
│   └── model/          # JPA/Hibernate entities
├── club_member/
│   ├── impl/
│   ├── mapper/
│   └── model/
├── season_player/
│   ├── impl/
│   ├── mapper/
│   └── model/
├── match/
│   ├── impl/
│   ├── mapper/
│   └── model/
├── practicioner/
│   ├── impl/
│   ├── mapper/
│   └── model/
└── shared/             # Common utilities
    ├── SpecificationBuilder.java
    └── StringListConverter.java
```

### Design Patterns
- **Repository Pattern:** Each domain entity has a repository implementation
- **Mapper Pattern:** Separate mappers for domain ↔ JPA entity conversions
- **Helper Pattern:** Repository helpers manage Spring Data JPA repositories
- **Specification Pattern:** Dynamic query building with Specifications
- **Converter Pattern:** Custom Hibernate converters for complex types
- **Lazy Loading:** JPA relationships configured with FetchType.LAZY for performance
- **Cascade Deletes:** Configured via @OnDelete(action = OnDeleteAction.CASCADE)

### Separation of Concerns
- **Domain Models:** Pure Java objects in `tt-data-league-core-domain` (no JPA annotations)
- **JPA Models:** Database-specific entities in `repository/jpa/*/model` with Hibernate annotations
- **Mappers:** Bidirectional conversion logic (JPA → Domain and Domain → JPA)
- **Repositories:** Implementation of domain interfaces with @Transactional support

---

## Core Components

### 1. JPA Entity Models (model/)

Each JPA entity corresponds to a domain model. Key characteristics:
- Located in `repository/jpa/{entity}/model/`
- Annotated with Jakarta Persistence annotations (JPA 3.1+)
- Uses Lombok for boilerplate reduction (@Getter, @Setter, @RequiredArgsConstructor)
- UUID fields stored as VARCHAR in database
- Includes database indexes for performance
- Lazy-loaded relationships for efficiency

#### Key JPA Entities

**ClubJPA**
```
Table: Club
Key Fields: id (UUID), name (VARCHAR, unique), yearRanges (ElementCollection)
Indexes: idx_name on name (unique)
Relationships: One-to-many with ClubMemberJPA (cascade delete)
```

**ClubMemberJPA**
```
Table: ClubMember
Key Fields: id, practicionerId (FK), clubId (FK), memberNumber, entryDate
Relationships: Many-to-one with PracticionerJPA and ClubJPA
```

**PracticionerJPA**
```
Table: Practicioner
Key Fields: id, firstName, secondName, fullName, birthDate
Relationships: One-to-many with ClubMemberJPA (cascade delete)
```

**SeasonPlayerJPA**
```
Table: SeasonPlayer
Key Fields: id, clubMemberId (FK), licenseTag, licenseRef, yearRange
Relationships: Many-to-one with ClubMemberJPA (cascade delete)
Indexes: idx_club_member_id
```

**SeasonPlayerResultJPA**
```
Table: SeasonPlayerResult
Key Fields: id, seasonPlayerId (FK), uniqueKey, competitionInfo
Relationships: Many-to-one with SeasonPlayerJPA
Purpose: Tracks player performance in competitions
```

### 2. Mappers (mapper/)

Bidirectional mappers convert between domain and JPA entities.

#### Naming Convention
- `{Domain}To{JPA}Mapper`: Domain → JPA (for persistence)
- `{JPA}To{Domain}Mapper`: JPA → Domain (for retrieval)

#### Mapper Responsibilities
- Type conversion (UUID, enums, embedded objects)
- Relationship mapping (handling foreign keys)
- Null safety and validation
- Preserving business logic during conversion

Example:
```java
ClubJPAToClubMapper           // JPA→Domain
ClubToClubJPAMapper           // Domain→JPA
SeasonPlayerJPAToSeasonPlayerMapper
SeasonPlayerToSeasonPlayerJPAMapper
```

### 3. Repository Implementations (impl/)

Implement domain repository interfaces with JPA/database logic.

#### Repository Helper Pattern
Each repository has a corresponding "Helper" class:
- `ClubRepositoryHelper` → `ClubRepositoryImpl`
- `SeasonPlayerRepositoryHelper` → `SeasonPlayerRepositoryImpl`

The helper manages Spring Data JPA repositories, while the implementation:
1. Executes business logic
2. Maps JPA entities to domain entities
3. Handles transactional boundaries
4. Enforces repository contracts

#### Key Implementation Details
- `@Transactional` annotation on class level
- `@Component` registration with Spring
- Constructor injection of dependencies
- Methods delegate to helpers for queries
- Mappers used in transformation pipelines

#### Repository Operations

**ClubRepositoryImpl**
- `findById(UUID)`: Single club lookup
- `findByName(String)`: Find by unique name
- `findAll()`: Retrieve all clubs
- `save(Club)`: Create new club
- `update(Club)`: Modify existing club
- `delete(UUID)`: Remove club (cascades to club members)

**SeasonPlayerRepositoryImpl** (Extended functionality)
- `findById(UUID)`
- `findByPracticionerIdClubIdSeason(...)`: Complex multi-field search
- `findByPracticionerNameAndClubNameAndSeason(...)`: Natural name-based queries
- Dynamic specification-based queries

**SeasonPlayerResultRepositoryImpl**
- `findById(UUID)`
- `findBySeasonPlayer(UUID)`: Results for a player
- `findByUniqueKey(String)`: Lookup by generated unique key

### 4. Shared Utilities (shared/)

#### SpecificationBuilder<T>
Dynamic query builder using Spring Data Specifications.

**Purpose:** Build complex JPA queries without hardcoding JPQL/SQL

**Methods:**
- `equalIfPresent(String field, Object value)`: Equality with null check
- `likeIfPresent(String field, String value)`: Pattern matching with blank check
- `build()`: Compose specifications with AND logic

**Usage:**
```java
Specification<SeasonPlayerJPA> spec = new SpecificationBuilder<SeasonPlayerJPA>()
    .equalIfPresent("yearRange", "2023-2024")
    .likeIfPresent("name", "pattern")
    .build();
```

#### StringListConverter
Custom Hibernate converter for `List<String>` persistence.

**Purpose:** Convert between Java List<String> and database representation (JSON/CSV)

**Use Cases:**
- Club year ranges storage
- Flexible list attributes in JPA entities

---

## Data Flow Architecture

### Create Flow (Domain → Database)
```
Service → Domain Entity (Club)
    ↓
Repository Interface (ClubRepository.save())
    ↓
ClubRepositoryImpl.save()
    ↓
ClubToClubJPAMapper (Domain → JPA)
    ↓
ClubJPA (Hibernate entity)
    ↓
ClubRepositoryHelper (Spring Data)
    ↓
Database
```

### Read Flow (Database → Domain)
```
Database
    ↓
Spring Data JPA (ClubRepositoryHelper)
    ↓
ClubJPA (Hibernate entity)
    ↓
ClubJPAToClubMapper (JPA → Domain)
    ↓
Club (Domain Entity)
    ↓
Repository Interface returns Optional<Club>
    ↓
Service Layer
```

### Update Flow
```
Service → Domain Entity (Club) with modifications
    ↓
Repository Interface (ClubRepository.update())
    ↓
ClubRepositoryImpl.update()
    ↓
Merge existing JPA entity with updated values
    ↓
ClubToClubJPAMapper (partial mapping)
    ↓
Flush to database
```

---

## Dependencies

### Internal Dependencies
- **tt-data-league-core-domain** (org.cttelsamicsterrassa:tt-data-league-core-domain)
  - Domain models and repository interfaces
  - Services and utilities
  - Imported as compile dependency (required for implementation)

### Spring Boot Framework
- **spring-boot-starter-data-jpa** (3.5.8)
  - Spring Data JPA, Hibernate, Jakarta Persistence
  - Provides repository abstraction and entity management
  - Transaction management

- **spring-boot-starter-jdbc** (3.5.8)
  - JDBC DataSource abstraction
  - Connection pooling configuration

### Database Connectivity
- **HikariCP** (latest from Spring Boot)
  - High-performance JDBC connection pool
  - Automatic configuration in Spring Boot

### Utilities
- **Project Lombok** (1.18.42)
  - `@Getter`, `@Setter` for JPA entities
  - `@RequiredArgsConstructor` for constructors
  - Reduces boilerplate code in entity classes

### Managed Dependencies
All versions inherited from Spring Boot parent (3.5.8):
- jakarta.persistence (JPA 3.1)
- org.hibernate (6.x)
- Spring Data Commons
- Spring Framework 6.x

---

## Database Schema Design

### Key Design Decisions

#### UUID as Primary Key
- All entities use `UUID` (java.util.UUID)
- Stored as `VARCHAR(36)` in database
- Generated client-side: `UUID.randomUUID()`
- Hibernate annotation: `@JdbcTypeCode(SqlTypes.VARCHAR)`

#### Relationships
- Foreign keys use cascade delete: `@OnDelete(action = OnDeleteAction.CASCADE)`
- Lazy loading (`FetchType.LAZY`) for performance
- Join columns explicitly named for clarity

#### Indexing Strategy
- Unique fields indexed (e.g., Club.name)
- Foreign key columns indexed
- Sparse indexes for optional unique constraints

#### Type Storage
- Lists stored via `@ElementCollection` (separate table)
- String lists converted via `StringListConverter`
- Enums stored as VARCHAR

### Example: Club to ClubMember Relationship
```
Club (1) ──── (M) ClubMember
- One club can have many club members
- Delete club → cascades to delete club members
- ClubMember has @ManyToOne reference to Club
- Foreign key: club_id in ClubMember table
```

---

## Transaction Management

### Transaction Boundaries
- Repository implementations marked with `@Transactional`
- Transactions span the entire repository method
- Automatic rollback on exceptions
- Spring manages commit/rollback

### Transaction Isolation
- Default isolation level: Database-dependent (usually READ_COMMITTED)
- No explicit isolation configured (uses Spring defaults)
- Consider adding for concurrent write scenarios

### Lazy Loading Caveats
- Related entities loaded outside transaction may cause LazyInitializationException
- Use FetchType.EAGER or explicit joins for cross-transaction access
- Keep transactions active during entity navigation

---

## Key Concepts for Agentic Development

### 1. Adding a New Repository Implementation

**Steps:**
1. JPA Entity Model exists in `jpa/{entity}/model/{Entity}JPA.java`
2. Create mappers in `jpa/{entity}/mapper/`:
   - `{Entity}To{Entity}JPAMapper`
   - `{Entity}JPA To{Entity}Mapper`
3. Create helper in `jpa/{entity}/impl/{Entity}RepositoryHelper`
   - Extends Spring Data CrudRepository or JpaRepository
4. Create implementation in `jpa/{entity}/impl/{Entity}RepositoryImpl`
   - Implements domain repository interface
   - Marked with `@Transactional` and `@Component`
5. Register with Spring dependency injection

### 2. Mapper Implementation Pattern
```java
// Domain → JPA
@Component
public class ClubToClubJPAMapper {
    public ClubJPA map(Club domain) {
        ClubJPA jpa = new ClubJPA();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setYearRanges(new ArrayList<>(domain.getYearRanges()));
        return jpa;
    }
}

// JPA → Domain
@Component
public class ClubJPAToClubMapper {
    public Club map(ClubJPA jpa) {
        return Club.createExisting(jpa.getId(), jpa.getName());
    }
}
```

### 3. Dynamic Queries with Specifications
```java
// Build specification for reusable queries
Specification<SeasonPlayerJPA> spec = new SpecificationBuilder<SeasonPlayerJPA>()
    .equalIfPresent("yearRange", yearRange)
    .likeIfPresent("clubMember.club.name", clubName)
    .build();

List<SeasonPlayerJPA> results = helper.findAll(spec);
```

### 4. Complex Multi-Entity Queries
Use method names with underscores for JPA query method generation:
```java
Optional<SeasonPlayerJPA> findByClubMember_Practicioner_IdAndClubMember_Club_IdAndYearRange(
    UUID practicionerId, UUID clubId, String yearRange
);
```

### 5. Cascade Delete Behavior
- Child entities automatically deleted when parent deleted
- Configure with `@OnDelete(action = OnDeleteAction.CASCADE)`
- Be careful not to cascade in wrong direction
- Database enforces referential integrity

### 6. Collection Persistence
```java
@ElementCollection
@CollectionTable(name="club_year_ranges", joinColumns=@JoinColumn(name="club_id"))
@Column(name="year_range")
private List<String> yearRanges;
```

---

## Common Development Tasks

### Adding a Query Method to Repository
1. Add method to domain repository interface in `tt-data-league-core-domain`
2. Add corresponding Spring Data method to helper
3. Implement in repository implementation class
4. Map results using appropriate mapper
5. Return domain entity through Optional or List

### Handling Relationships in Mappers
- **One-to-Many:** Don't map child collections in parent mapper (prevents infinite loops)
- **Many-to-One:** Map foreign key and entity reference carefully
- **Lazy Loading:** Be aware that accessing unmapped relationships outside transaction throws exception

### Filtering/Searching Features
1. Use `SpecificationBuilder` for flexible search criteria
2. Build specification in helper or implementation
3. Call `findAll(Specification)` from JPA repository
4. Map results to domain entities
5. Return through repository interface

### Performance Optimization
1. Use `FetchType.LAZY` by default (load only when needed)
2. Create indexes on frequently queried fields
3. Use projections for read-only queries (if needed)
4. Batch load related entities when possible
5. Monitor N+1 query problems with Hibernate logging

---

## Testing Strategy

### Unit Testing
- Mock repository implementations in service layer tests
- Don't test mappers extensively (they're straightforward)
- Focus on business logic, not persistence

### Integration Testing
- Use Spring Boot test context with embedded database (H2)
- Test repository implementations with actual database
- Test mapper correctness with database round-trips
- Verify cascade delete behavior
- Test unique constraints and indexes

### Test Data Setup
```java
Club club = Club.createNew("Test Club");
repositoryImpl.save(club);

Optional<Club> found = repositoryImpl.findByName("Test Club");
assertThat(found).isPresent();
```

---

## Extension Points

### Future Enhancements

1. **Query by Example (QBE):** Use Spring Data's Example API for flexible searching
2. **Pagination & Sorting:** Add Spring's Pageable support to repository methods
3. **Auditing:** Add @CreationTimestamp, @LastModifiedTimestamp to entities
4. **Soft Deletes:** Implement logical deletion instead of physical deletion
5. **Entity Versioning:** Add @Version for optimistic locking
6. **Custom Converters:** Additional Hibernate converters for complex types
7. **Batch Operations:** Implement batch insert/update for performance

### Related Modules
- **tt-data-league-core-domain:** Domain interfaces and models
- Consumer applications will use these repositories
- Could extend with service layer for business orchestration

---

## Guidelines for Agentic Development

### ✅ DO's
- Keep JPA entities database-focused, domain entities business-focused
- Use constructor injection for all dependencies
- Leverage Specifications for complex queries
- Mark methods with `@Transactional` at repository level
- Use Lombok annotations to reduce boilerplate
- Create separate mappers for each direction
- Use UUID for all identifiers
- Configure indexes on frequently searched columns

### ❌ DON'Ts
- Don't let JPA annotations leak into domain entities
- Don't add business logic to JPA entities
- Don't use eager loading unless absolutely necessary
- Don't forget to implement both mapper directions
- Don't mix query logic in repository implementations (use helpers)
- Don't expose JPA entities from repository methods
- Don't hardcode JPQL; use Specifications or named queries
- Don't forget cascade delete configurations
- Don't access unmapped relationships outside transaction context

---

## Database Configuration

### Datasource Configuration
- Managed by Spring Boot autoconfiguration
- Configure in `application.properties` or `application.yml`
- Uses HikariCP for connection pooling
- Connection pool optimizations inherited from Spring Boot

### Hibernate Configuration
- JPA provider: Hibernate 6.x
- Dialect: Auto-detected by Spring Boot
- DDL: Create-drop or update (configure via spring.jpa.hibernate.ddl-auto)
- Lazy initialization proxy: ByteBuddy (default in Hibernate 6)

### Transaction Management
- Platform: JpaTransactionManager (auto-configured)
- Isolation level: Default (usually READ_COMMITTED)
- Propagation: REQUIRED (default for @Transactional)

---

## File Organization Reference

```
src/main/java/org/cttelsamicsterrassa/data/core/repository/
├── shared/
│   ├── SpecificationBuilder.java
│   └── StringListConverter.java
└── jpa/
    ├── club/
    │   ├── impl/
    │   │   ├── ClubRepositoryHelper.java
    │   │   └── ClubRepositoryImpl.java
    │   ├── mapper/
    │   │   ├── ClubJPAToClubMapper.java
    │   │   └── ClubToClubJPAMapper.java
    │   └── model/
    │       └── ClubJPA.java
    ├── club_member/
    │   ├── impl/
    │   ├── mapper/
    │   └── model/
    ├── season_player/
    │   ├── impl/
    │   │   ├── SeasonPlayerRepositoryHelper.java
    │   │   ├── SeasonPlayerRepositoryImpl.java
    │   │   ├── SeasonPlayerResultRepositoryHelper.java
    │   │   └── SeasonPlayerResultRepositoryImpl.java
    │   ├── mapper/
    │   └── model/
    ├── match/
    ├── practicioner/
    └── (other entity repositories)
```

---

## Quick Reference: Repository Implementation Pattern

```
Domain Interface (tt-data-league-core-domain)
    ↓
JPA Implementation (@Component, @Transactional)
    ├─→ RepositoryHelper (Spring Data interface)
    ├─→ JPAToPropertyMapper (Conversion)
    ├─→ PropertyToJPAMapper (Conversion)
    └─→ PropertyJPA (@Entity with JPA annotations)
```

---

## Troubleshooting Guide

| Issue | Cause | Solution |
|-------|-------|----------|
| LazyInitializationException | Accessing lazy relationship outside transaction | Keep transaction open or use FetchType.EAGER |
| Duplicate key exception | Violating unique constraint | Check unique indexes (e.g., Club.name) |
| No entity found | Invalid UUID or deleted entity | Verify UUID format and existence |
| Mapper returns null | Missing mapper bean or conversion error | Check mapper implementation and autowiring |
| Cascade delete fails | Circular dependencies or missing configuration | Verify @OnDelete annotations |
| Specification returns no results | Incorrect field names or types | Check JPA entity field names match specification |

---

## Version Information
- **Module Version:** 0.0.1-SNAPSHOT
- **Java Version:** 21
- **Spring Boot Version:** 3.5.8
- **Spring Data JPA:** Part of Spring Boot 3.5.8
- **Hibernate Version:** 6.x (via Spring Boot)
- **Jakarta Persistence:** 3.1
- **Lombok Version:** 1.18.42
- **Last Updated:** 2026-03-24

