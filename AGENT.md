# tt-data-league-core Project - Agent Guide

## Project Overview

**Project Name:** `tt-data-league-core`  
**Organization:** `org.cttelsamicsterrassa`  
**Version:** 0.0.1-SNAPSHOT  
**Project Type:** Multi-module Maven project  
**Java Version:** 21  
**Framework:** Spring Boot 3.5.8  
**Build Tool:** Apache Maven  

A modular, domain-driven design implementation for table tennis league data management. The project is structured as a core data module for managing clubs, practitioners, players, match results, and competition information.

---

## Project Structure

```
tt-data-league-core/
├── pom.xml                              (Root POM - Module aggregator)
├── AGENT.md                             (This file)
├── LICENSE
├── prompts/                             (AI/Agent prompts for development)
│
├── tt-data-league-core-domain/          (Domain layer - Business logic)
│   ├── pom.xml
│   ├── AGENT.md                         (Module-specific guide)
│   ├── src/main/java/org/.../
│   │   └── data/core/domain/
│   │       ├── model/                   (Domain entities)
│   │       ├── repository/              (Repository interfaces)
│   │       └── service/                 (Domain services)
│   ├── src/test/java/
│   └── target/                          (Build artifacts)
│
└── tt-data-league-core-repository-jpa/  (Data access layer - JPA implementation)
    ├── pom.xml
    ├── AGENT.md                         (Module-specific guide)
    ├── src/main/java/org/.../
    │   └── data/core/repository/
    │       ├── jpa/
    │       │   ├── club/                (Club repository implementation)
    │       │   ├── club_member/
    │       │   ├── match/
    │       │   ├── practicioner/
    │       │   ├── season_player/       (Complex repository)
    │       │   └── {other entities}
    │       └── shared/                  (Reusable utilities)
    ├── src/test/java/
    └── target/                          (Build artifacts)
```

---

## Module Dependencies

### Dependency Graph
```
Application Layer (consumers)
    ↓
tt-data-league-core-repository-jpa (Data Access - JPA)
    ↓
tt-data-league-core-domain (Domain Logic - Pure Java)
    ↓
commons-core (Shared base classes)
    ↓
Spring Boot Framework & External Libraries
```

### Dependency Matrix

| Module | Role | Dependencies |
|--------|------|--------------|
| **tt-data-league-core-domain** | Domain Models & Interfaces | commons-core, Spring Boot Starter |
| **tt-data-league-core-repository-jpa** | JPA Implementation | tt-data-league-core-domain, Spring Data JPA, Lombok, HikariCP |
| **Root POM** | Aggregator & Dependency Management | Maven, Spring Boot BOM |

### External Dependencies Overview

#### Spring Boot (3.5.8)
- **spring-boot-starter:** Core framework (logging, configuration)
- **spring-boot-starter-data-jpa:** Database ORM and repository support
- **spring-boot-starter-jdbc:** JDBC abstraction
- **spring-boot-dependencies (BOM):** Managed versions for all Spring dependencies

#### Database & ORM
- **Hibernate 6.x:** JPA provider (included via Spring Data JPA)
- **HikariCP:** Connection pooling
- **Jakarta Persistence 3.1:** JPA standard implementation

#### Utilities
- **Lombok (1.18.42):** Code generation for boilerplate
- **commons-core (0.0.1-SNAPSHOT):** Base Entity class and shared utilities

#### Build Tools
- **Maven 3.x:** Dependency management and build orchestration
- **Compiler:** Java 21 compiler with source/target 21

---

## Module Responsibilities

### `tt-data-league-core-domain`
**Type:** Core Business Logic Module  
**Role:** Define domain models, contracts, and business rules  
**Responsibility:**
- Domain entities (Club, Practicioner, SeasonPlayer, etc.)
- Repository interfaces (contracts only)
- Domain services (cross-entity business logic)
- Business rules and validations
- **NO** persistence implementation
- **NO** Spring infrastructure details

**Key Characteristics:**
- Pure Java classes
- Minimal external dependencies
- Highly testable
- Framework-agnostic design

**Consumer:** `tt-data-league-core-repository-jpa` (and future modules)

---

### `tt-data-league-core-repository-jpa`
**Type:** Data Access Implementation Module  
**Role:** Implement repository interfaces using JPA/Hibernate  
**Responsibility:**
- JPA entity models with Hibernate annotations
- Mapper classes (Domain ↔ JPA conversion)
- Repository implementations (Spring Components)
- Query specifications and dynamic query builders
- Transaction management
- Database schema representation

**Key Characteristics:**
- JPA-specific implementation
- Spring Framework integration
- Transactional boundaries
- Database abstraction

**Dependencies:** `tt-data-league-core-domain` (implements its interfaces)

**Used By:** Application services, REST controllers, etc. (outside this project)

---

## Core Domain Concepts

### Entities & Their Relationships

```
Club (1)
├── (1:M) → ClubMember (M)
│   ├── → Practicioner
│   └── → Club
│       └── (1:M) → SeasonPlayer (M)
│           ├── → License
│           ├── → ClubMember
│           └── (1:M) → SeasonPlayerResult (M)
│               ├── → CompetitionInfo
│               └── → MatchInfo
│
MatchInfo (1)
└── (1:M) → PlayersSingleMatch (M)
    └── → SeasonPlayer
```

### Entity Descriptions

| Entity | Purpose | Identity |
|--------|---------|----------|
| **Club** | Table tennis club organization | UUID |
| **Practicioner** | Person involved in table tennis | UUID |
| **ClubMember** | Association: Practicioner ↔ Club | UUID |
| **License** | Federation/membership license | String identifier |
| **SeasonPlayer** | Player registered for a season | UUID |
| **SeasonPlayerResult** | Match result record for player | UUID |
| **CompetitionInfo** | Metadata about competition | Value Object |
| **MatchInfo** | Match event information | UUID |
| **PlayersSingleMatch** | Individual performance in match | UUID |
| **TeamRole** | Role assignment in team context | Enum/Value Object |

---

## Development Workflows

### Adding a New Entity Type

#### Step 1: Define Domain Model
**File:** `tt-data-league-core-domain/src/main/java/.../model/{Entity}.java`
```
1. Create domain entity extending Entity
2. Add UUID identity field
3. Implement factory methods (createNew, createExisting)
4. Add business logic methods (command pattern)
5. Use immutable style (no setters)
```

#### Step 2: Define Repository Interface
**File:** `tt-data-league-core-domain/src/main/java/.../repository/{Entity}Repository.java`
```
1. Define CRUD contract methods
2. Add domain-specific query methods
3. Return Optional<T> for single results
4. Return List<T> for collections
```

#### Step 3: Create JPA Entity
**File:** `tt-data-league-core-repository-jpa/src/main/java/.../jpa/{entity}/model/{Entity}JPA.java`
```
1. Create @Entity with @Table annotation
2. Add @Id field matching domain UUID
3. Map relationships with @ManyToOne, @OneToMany
4. Configure cascade behavior
5. Add indexes for performance
6. Use Lombok annotations
```

#### Step 4: Create Mappers
**Files:** 
- `tt-data-league-core-repository-jpa/.../jpa/{entity}/mapper/{Entity}To{Entity}JPAMapper.java`
- `tt-data-league-core-repository-jpa/.../jpa/{entity}/mapper/{Entity}JPATo{Entity}Mapper.java`

```
1. Domain → JPA: Convert domain entity to JPA entity
2. JPA → Domain: Reconstruct domain entity from JPA
3. Handle relationship conversions
4. Mark with @Component for Spring
```

#### Step 5: Create Repository Helper
**File:** `tt-data-league-core-repository-jpa/.../jpa/{entity}/impl/{Entity}RepositoryHelper.java`
```
1. Extend Spring Data Repository interface
2. Define Spring Data query methods
3. Add Specification-based methods for complex queries
4. Use method naming conventions or @Query
```

#### Step 6: Create Repository Implementation
**File:** `tt-data-league-core-repository-jpa/.../jpa/{entity}/impl/{Entity}RepositoryImpl.java`
```
1. Implement domain repository interface
2. Mark with @Component and @Transactional
3. Inject helper and mappers
4. Implement all interface methods
5. Orchestrate mapping and query execution
```

#### Step 7: Test Coverage
```
1. Domain entity tests (business logic)
2. Mapper tests (conversion correctness)
3. Repository integration tests (persistence)
4. Query tests (specification correctness)
```

---

## Build & Deployment

### Building the Project

```powershell
# From project root directory
mvn clean install

# Clean: Remove target directories
mvn clean

# Package: Build all modules
mvn package

# Install: Install to local Maven repository
mvn install

# Compile only
mvn compile

# Run tests only
mvn test
```

### Build Artifacts
- **Domain Module JAR:** `tt-data-league-core-domain/target/tt-data-league-core-domain-0.0.1-SNAPSHOT.jar`
- **Repository Module JAR:** `tt-data-league-core-repository-jpa/target/tt-data-league-core-repository-jpa-0.0.1-SNAPSHOT.jar`

### Maven Modules
```xml
<modules>
    <module>tt-data-league-core-domain</module>
    <module>tt-data-league-core-repository-jpa</module>
</modules>
```

### Dependency Management Strategy
- Parent POM manages all versions
- Child modules inherit versions
- Spring Boot BOM provides transitive dependency management
- Override versions if needed in child pom.xml

---

## Testing Strategy

### Test Organization

#### Domain Module Tests
- **Location:** `tt-data-league-core-domain/src/test/java/`
- **Focus:** Entity business logic, domain services
- **Type:** Unit tests (no database)
- **Mocks:** Repository mocks

#### Repository Module Tests
- **Location:** `tt-data-league-core-repository-jpa/src/test/java/`
- **Focus:** Repository implementations, mappers, JPA entities
- **Type:** Integration tests (with database)
- **Database:** Embedded H2 for testing

### Test Pyramid
```
      /\
     /  \      End-to-End Tests
    /____\    (Integration scenarios)
    /    \
   /      \   Integration Tests
  /________\  (Repository + JPA)
  /        \
 /          \ Unit Tests
/____________\(Entity logic, services)
```

### Running Tests
```powershell
# All tests
mvn test

# Specific module
mvn -pl tt-data-league-core-domain test

# Specific test class
mvn test -Dtest=ClubRepositoryImplTest

# Skip tests
mvn install -DskipTests
```

---

## Configuration & Customization

### Application Properties
Configure in consumer application's `application.properties` or `application.yml`:

```properties
# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect

# Datasource
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
```

### Customization Points

1. **Database Dialect:** Configure Hibernate dialect for target database
2. **DDL Strategy:** Choose create-drop/update/validate
3. **Connection Pooling:** HikariCP configuration for performance
4. **Transaction Isolation:** Configure transaction isolation level
5. **Lazy Loading:** Configure eager vs lazy loading strategies
6. **Custom Converters:** Add Hibernate converters for custom types

---

## Key Architectural Decisions

### 1. Domain-Driven Design (DDD)
- Domain module contains pure business logic
- Repository pattern abstracts persistence
- Separation allows domain to be persistence-agnostic

### 2. Mapper Pattern
- Bidirectional conversion between domains and JPA entities
- Keeps domain entities clean
- Facilitates testing in isolation

### 3. UUID Identities
- Generated client-side
- Suitable for distributed systems
- Avoids database sequence dependencies

### 4. Repository Helpers
- Separate concern: Spring Data JPA interface
- Allows repository implementation to focus on business logic
- Testability: Easier to mock helper

### 5. Lazy Loading by Default
- Performance optimization
- Reduces unnecessary data loading
- Requires careful transaction management

### 6. Cascade Deletes
- Maintain referential integrity
- Automatic cleanup of child records
- Configured at database level

---

## Security Considerations

### Current State (Pre-Production)
- No authentication/authorization implemented
- No data validation at repository layer
- No encryption of sensitive data
- Consider for future phases:

### Future Security Enhancements
1. **Input Validation:** Add bean validation (@NotNull, @Size, etc.)
2. **Auditing:** Track who changed what and when
3. **Soft Deletes:** Implement logical deletion
4. **Row-Level Security:** Restrict data access by user
5. **Encryption:** Encrypt sensitive fields
6. **Access Control:** Spring Security integration

---

## Performance Optimization

### Current Optimizations
- Lazy loading configured for relationships
- Database indexes on frequently queried columns
- Connection pooling via HikariCP
- Specification-based queries for efficient filtering

### Potential Improvements
1. **Query Caching:** Implement Hibernate second-level cache
2. **Batch Processing:** Batch inserts/updates
3. **Projections:** Return DTOs instead of full entities
4. **Pagination:** Implement pageable queries
5. **Monitoring:** Add Hibernate query logging and analysis
6. **Database Optimization:** Analyze slow queries

---

## Dependency Management

### Adding New Dependencies

#### In Domain Module
```xml
<!-- In tt-data-league-core-domain/pom.xml -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>library</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### In Repository Module
```xml
<!-- In tt-data-league-core-repository-jpa/pom.xml -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>library</artifactId>
    <version>1.0.0</version>
</dependency>
```

#### Managed Versions (Root POM)
```xml
<!-- In root pom.xml <dependencyManagement> -->
<dependency>
    <groupId>org.example</groupId>
    <artifactId>library</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Dependency Best Practices
- Define versions in parent POM
- Use `dependencyManagement` for version control
- Minimize transitive dependencies
- Regularly update for security patches
- Use exclusions for unwanted transitive deps

---

## Troubleshooting

### Build Issues

| Problem | Solution |
|---------|----------|
| Compilation error | Verify Java 21 installed, check import statements |
| Missing dependencies | Run `mvn dependency:resolve`, check Maven cache |
| JAR not found | Run `mvn install` on dependency module first |
| Circular dependency | Review module dependencies, refactor as needed |

### Runtime Issues

| Problem | Solution |
|---------|----------|
| LazyInitializationException | Keep transaction open or eagerly load |
| No bean found | Check @Component annotation, Spring scanning |
| Database connection failed | Verify datasource configuration |
| Unique constraint violation | Check unique indexes, handle duplicates |

### Testing Issues

| Problem | Solution |
|---------|----------|
| Test fails with null | Check test setup, entity initialization |
| Transaction rollback | Verify @Transactional annotation, test isolation |
| Database not created | Check ddl-auto setting, schema creation |

---

## Contributing Guidelines

### Code Style
- Java 21 language features acceptable
- Follow Spring Boot conventions
- Use meaningful variable/method names
- Keep methods focused and small
- Document complex business logic

### Commit Strategy
- Commit per logical change
- Include descriptive messages
- Reference issue numbers when applicable
- Keep commits atomic

### Testing Requirements
- Add unit tests for new domain logic
- Add integration tests for repositories
- Maintain test coverage > 70%
- Run full test suite before committing

### Review Checklist
- [ ] Code follows project conventions
- [ ] Tests added and passing
- [ ] No breaking API changes
- [ ] Documentation updated
- [ ] Dependencies security checked

---

## Documentation Files

### Module-Specific Guides
- **tt-data-league-core-domain/AGENT.md:** Domain layer architecture and patterns
- **tt-data-league-core-repository-jpa/AGENT.md:** JPA implementation details
- **tt-data-league-core/AGENT.md:** This file (project overview)

### Additional Resources
- **pom.xml files:** Dependency declarations and build configuration
- **LICENSE:** Project license terms
- **prompts/:** AI-assisted development guides

---

## Quick Reference: Common Tasks

### 1. Clone and Build
```powershell
git clone <repository>
cd tt-data-league-core
mvn clean install
```

### 2. Run Domain Tests
```powershell
mvn -pl tt-data-league-core-domain test
```

### 3. Run Repository Tests
```powershell
mvn -pl tt-data-league-core-repository-jpa test
```

### 4. Build for Distribution
```powershell
mvn clean package
```

### 5. Install to Local Maven Repo
```powershell
mvn clean install
```

### 6. View Dependencies
```powershell
mvn dependency:tree
```

### 7. Check for Vulnerabilities
```powershell
mvn org.apache.maven.plugins:maven-dependency-plugin:check-updates
```

---

## Future Roadmap

### Phase 1 (Current)
- ✅ Core domain entities defined
- ✅ Repository interfaces established
- ✅ JPA implementation layer
- ✅ Basic CRUD operations
- Current: Building out domain services

### Phase 2 (Planned)
- [ ] REST API layer
- [ ] Event sourcing for domain events
- [ ] Advanced querying (pagination, filtering)
- [ ] Audit logging
- [ ] Integration tests

### Phase 3 (Future)
- [ ] Microservices decomposition
- [ ] Event-driven architecture
- [ ] CQRS pattern implementation
- [ ] Performance optimization (caching, indexing)
- [ ] Security implementation

---

## Version Information

### Project Versions
- **Project Version:** 0.0.1-SNAPSHOT
- **Java Target:** Java 21
- **Spring Boot:** 3.5.8
- **Maven:** 3.x
- **Git:** Latest stable

### Dependency Versions
| Dependency | Version | Scope |
|------------|---------|-------|
| Spring Boot BOM | 3.5.8 | import |
| Hibernate | 6.x | transitive |
| Jakarta Persistence | 3.1 | transitive |
| Lombok | 1.18.42 | compile |
| HikariCP | Latest (via Spring) | compile |
| commons-core | 0.0.1-SNAPSHOT | compile |
| H2 Database | Latest (via Spring) | test |

---

## Contact & Support

### Documentation
- Review module-specific AGENT.md files for detailed guidance
- Check pom.xml files for dependency information
- Examine source code for implementation details

### Development
- Follow guidelines in section "Contributing Guidelines"
- Reference "Common Development Tasks" in module guides
- Use troubleshooting guide for common issues

---

## Key Concepts Summary

| Concept | Location | Purpose |
|---------|----------|---------|
| **Domain Entities** | tt-data-league-core-domain/model | Business logic |
| **Repository Interfaces** | tt-data-league-core-domain/repository | Persistence contracts |
| **Domain Services** | tt-data-league-core-domain/service | Cross-entity logic |
| **JPA Entities** | tt-data-league-core-repository-jpa/jpa/*/model | Database mapping |
| **Mappers** | tt-data-league-core-repository-jpa/jpa/*/mapper | Domain ↔ JPA conversion |
| **Repository Impl** | tt-data-league-core-repository-jpa/jpa/*/impl | Persistence implementation |
| **Specifications** | tt-data-league-core-repository-jpa/shared | Dynamic query building |

---

## Last Updated
**Date:** 2026-03-24  
**Version:** 0.0.1-SNAPSHOT  
**Status:** Active Development

