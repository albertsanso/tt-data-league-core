# Root AGENTS.md Draft — Proposed Generic Project Guide

> Draft only. This file is intended for human review and manual promotion into the protected root `AGENTS.md` if approved.

# Project Agent Guide

## 0. File Governance

This guide is the root coordination document for the repository.

- Treat the root guide as the authoritative contract for cross-module rules.
- Keep module-specific implementation rules in each module-local `AGENTS.md`.
- Do not duplicate low-level implementation details here when they belong to a single module.
- Changes to the protected root guide must go through human review.

---

## 1. Repository Purpose

This repository is a **multi-module Maven codebase** structured around **layered domain-driven design**.

At a high level, the repository separates:

1. **Business concepts and contracts**
2. **Infrastructure implementations of those contracts**
3. **Shared project-level build and dependency management**

The root guide exists to help agents work safely across modules without breaking architectural boundaries.

---

## 2. Repository Shape

The repository follows a stable top-level structure:

```text
<repo>/
├── pom.xml                         root aggregator and dependency management
├── AGENTS.md                       project-wide agent contract
├── <domain-module>/                pure domain logic and repository ports
├── <infrastructure-module>/        persistence implementation of domain ports
├── docs/                           design notes, architecture, feature docs
└── prompts/                        agent prompt material and drafting support
```

### Structural rule

The root POM is an **aggregator only**.

It should:
- define modules
- centralize shared versions and plugin management
- avoid owning business code

Concrete source code belongs in child modules, never in the root project.

---

## 3. Architectural Boundaries

### 3.1 Layering model

The repository follows this dependency direction:

```text
Consumers / applications
        ↓
Infrastructure implementation module(s)
        ↓
Domain module
        ↓
Shared libraries / framework base dependencies
```

### 3.2 Allowed dependency flow

- Infrastructure modules may depend on the domain module.
- The domain module must not depend on infrastructure modules.
- The root project must not become a source-code module.
- Cross-module coupling should happen through **interfaces, domain types, and mappers**, not through framework-specific classes.

### 3.3 Boundary rules

- Business rules belong in the domain layer.
- Persistence details belong in infrastructure modules.
- Mapping logic belongs at the boundary between domain and infrastructure.
- Build orchestration belongs in the root POM.
- Documentation and prompts must not become runtime dependencies.

---

## 4. Module Responsibilities

### 4.1 Domain module

The domain module is responsible for:
- entities and aggregate state
- value objects and enums
- repository contracts / ports
- domain services
- validation rules
- domain-specific exceptions
- business-oriented mutation methods

The domain module must avoid:
- JPA annotations
- ORM-specific classes
- infrastructure transaction concerns
- persistence implementation details

### 4.2 Infrastructure persistence module

The infrastructure module is responsible for:
- JPA entities
- Spring Data helper interfaces
- repository implementations
- domain ↔ persistence mapping
- query construction utilities
- persistence converters and shared technical helpers
- transactional persistence boundaries

The infrastructure module must avoid:
- redefining domain concepts already modeled in the domain module
- exposing persistence entities through public repository APIs
- embedding business decisions that belong to the domain

### 4.3 Root project

The root project is responsible for:
- module aggregation
- dependency management
- plugin management
- repository-wide conventions
- project-level documentation entry points

---

## 5. Package and Folder Patterns

### 5.1 Domain-side pattern

```text
src/main/java/.../domain/
├── model/
│   └── <subdomain>/          optional bounded subdomain grouping
├── repository/
│   └── <subdomain>/
└── service/
    └── <subdomain>/
```

### 5.2 Infrastructure-side pattern

```text
src/main/java/.../repository/
├── jpa/
│   └── <aggregate>/
│       ├── model/
│       ├── mapper/
│       └── impl/
└── shared/
```

### 5.3 Grouping rule

If a concern forms a bounded subdomain, group it consistently across all related layers rather than scattering files across unrelated packages.

---

## 6. Naming Patterns

The codebase follows a strong suffix-based naming convention. New code should preserve those naming signals.

### 6.1 Domain naming

| Artefact | Pattern |
|---|---|
| Entity | `<Concept>` |
| Value object record | `<Concept>` or `<Concept>Info` |
| Enum | `<Concept>` |
| Repository port | `<Concept>Repository` |
| Domain service | `<Concept>Service` |
| Validator | `<Concept>Validator` |
| Builder / utility | `<Concept><Action>` |
| Domain exception | `<Context>Exception` |
| Capability port | `<Capability>` |

### 6.2 Infrastructure naming

| Artefact | Pattern |
|---|---|
| JPA entity | `<Concept>JPA` |
| Domain → JPA mapper | `<Concept>To<Concept>JPAMapper` |
| JPA → Domain mapper | `<Concept>JPATo<Concept>Mapper` |
| Spring Data helper | `<Concept>RepositoryHelper` |
| Repository implementation | `<Concept>RepositoryImpl` |
| Shared technical helper | `<Purpose>Builder` / `<Purpose>Converter` |

### 6.3 Test naming

| Artefact | Pattern |
|---|---|
| Test class | `<ClassUnderTest>Test` |
| Test method | `<method><ExpectedBehaviourOrScenario>` |

Naming should describe **responsibility**, not implementation trivia.

---

## 7. Design Patterns Used Across the Repository

### 7.1 Domain-driven design

The repository is organized around domain concepts first, not around database tables or controllers.

Implications:
- domain types define the core language
- repositories are ports / contracts
- infrastructure implements domain contracts
- business rules are modeled close to the domain state they govern

### 7.2 Repository pattern

Repository interfaces live in the domain layer and represent persistence contracts in domain terms.

Repository implementations live in infrastructure and translate those contracts into actual storage operations.

### 7.3 Mapper pattern

The repository uses explicit boundary mapping rather than reusing persistence entities as domain objects.

Implications:
- one mapper per direction
- mapping classes remain stateless where possible
- public APIs return domain types, not persistence types

### 7.4 Port-and-adapter pattern

When the domain requires an external capability, the domain defines an abstraction and infrastructure provides the implementation.

Examples of such capabilities include:
- hashing
- persistence
- time or token generation
- external system integration

The root rule is: **the domain owns the contract, infrastructure owns the adapter**.

### 7.5 Factory method pattern

Entities are created through named static factories instead of public constructors.

This allows the model to distinguish between:
- new instances created inside the domain
- existing instances reconstructed from persistence

### 7.6 Command-method mutation style

Domain state changes should happen through methods named after business actions rather than generic setters.

This keeps mutation intentional and is the preferred way to preserve invariants.

### 7.7 Specification / query composition pattern

Dynamic persistence queries should be composed through reusable specification-building helpers or extracted query lambdas rather than large monolithic methods.

---

## 8. Cross-Module Coding Rules

### 8.1 Domain rules

- Prefer immutable identity fields.
- Use private constructors with static factories.
- Avoid setters on domain entities.
- Return defensive read-only views for mutable collections.
- Express state changes through business verbs.
- Keep validation close to the domain or its services.
- Use records for identity-free value objects.
- Use enums with a stable external string value when interoperability matters.

### 8.2 Infrastructure rules

- Keep persistence annotations and ORM concerns inside infrastructure modules only.
- Use repository implementations as the only public bridge from persistence to domain repositories.
- Keep Spring Data helper interfaces internal in intent, even if technically public.
- Use mappers for every domain ↔ persistence transition.
- Prefer constructor injection.
- Keep shared technical helpers under a dedicated `shared/` package.

### 8.3 Root-level rules

- Prefer adding versions in the root `pom.xml` dependency management section.
- Keep child modules focused on module-specific dependencies.
- Avoid architectural leakage across modules.
- Document exceptions to conventions explicitly rather than silently normalizing them.

---

## 9. Entity and Model Patterns

### 9.1 Entities

Expected characteristics of entity classes:
- explicit identity
- private constructor
- `createNew(...)` style factory
- `createExisting(...)` style factory
- domain methods for mutation
- getters only for state exposure
- no persistence-specific annotations in the domain layer

### 9.2 Value objects

Use value objects when:
- identity is irrelevant
- equality should be structural
- immutability is desirable
- the concept groups related fields as one meaning-bearing unit

### 9.3 Collections

When an entity owns a collection:
- initialize internally
- expose read-only access
- provide explicit add / remove / clear / replace operations
- guard against duplicate or invalid entries if the domain requires it

---

## 10. Repository and Query Patterns

### 10.1 Domain repository contracts

Repository contracts should use:
- `Optional<T>` for singular lookups that may miss
- `List<T>` for collections
- `boolean` for existence checks
- `void` for save / delete style commands unless the domain specifically requires a return value

Method names should clearly reflect:
- exact lookup (`findBy...`)
- similarity / search behaviour (`searchBySimilar...`, `findBy...Containing...`)
- existence checks (`existsBy...`)

### 10.2 Infrastructure repository implementations

Repository implementations should:
- implement domain ports directly
- map persistence models to domain models on the way out
- map domain models to persistence models on the way in
- keep transactional boundaries at the implementation layer
- extract complex query logic into private methods or shared builders

### 10.3 Dynamic queries

When a query has optional filters or nested conditions:
- prefer specification builders or composable specifications
- keep query composition null-safe
- extract repeated predicate logic into helper methods
- avoid giant derived method names when query complexity becomes hard to read

---

## 11. Validation and Exceptions

### 11.1 Validation pattern

Validation is modeled explicitly rather than being scattered implicitly.

Preferred forms:
- validator classes returning lists of human-readable errors
- aggregate validation methods that throw a domain exception when needed
- guard clauses at service entry points for null / blank inputs

### 11.2 Exception pattern

Domain exceptions should:
- extend `RuntimeException`
- be named by domain context, not framework mechanics
- live close to the service or subdomain that uses them
- communicate domain meaning clearly

Do not convert domain exceptions into infrastructure exceptions inside the domain module.

---

## 12. Dependency Management Rules

### 12.1 Root POM responsibilities

The root `pom.xml` should be the place for:
- module registration
- shared Java version
- shared encoding
- dependency version governance
- plugin management

### 12.2 Child module responsibilities

Child POM files should:
- inherit from the root project
- declare only the dependencies needed by that module
- avoid redefining versions already managed by the root where possible

### 12.3 Dependency hygiene

- prefer version alignment through `dependencyManagement`
- minimize unnecessary transitive dependencies
- keep framework dependencies out of the domain layer unless they are truly lightweight and non-invasive
- review new dependencies for security, maintenance, and architectural fit

---

## 13. Testing Strategy

The repository follows a layered testing model.

### 13.1 Domain tests

Domain tests should focus on:
- factory behaviour
- invariant enforcement
- command-method state changes
- validation behaviour
- service orchestration with mocks

Expected tools:
- JUnit 5
- Mockito

### 13.2 Infrastructure tests

Infrastructure tests should focus on:
- persistence round-trips
- repository query behaviour
- mapping correctness
- transactional behaviour
- database-backed integration through a lightweight test setup

### 13.3 Test design rules

- isolate test data per test
- clear repositories before each test when persistence is involved
- persist parent dependencies before children
- cover happy path, empty result, invalid input, and boundary scenarios
- verify exception messages when they form part of the contract

### 13.4 Test naming rules

Test names should state behaviour, not implementation mechanics.

Good pattern:
- `<method>Returns...`
- `<method>Throws...`
- `<method>Rejects...`
- `<method>Applies...`

---

## 14. Build and Execution Practices

### 14.1 Standard build expectations

From the repository root, standard Maven lifecycle commands should work across modules.

Typical operations include:
- full build
- compilation only
- test execution
- module-scoped test runs
- local install

### 14.2 Change discipline

Before considering a change complete:
- compile the affected module(s)
- run relevant tests
- run broader tests if shared contracts changed
- check whether cross-module mappings or repository contracts need coordinated updates

### 14.3 Safe change rule

If a change touches a public contract in the domain module, assume at least one infrastructure implementation must be reviewed and possibly updated.

---

## 15. Documentation Strategy

The repository has multiple layers of guidance.

### 15.1 Root guide

Use the root guide for:
- architecture boundaries
- naming patterns
- repository-wide rules
- cross-module workflows

### 15.2 Module guides

Use module-specific guides for:
- module-local structural contracts
- implementation templates
- module-specific quirks and known issues
- technology-specific details

### 15.3 Supporting docs

Use `docs/` for:
- architecture descriptions
- feature notes
- decision records
- workflow and planning material

Use `prompts/` for:
- prompt templates
- agent drafting instructions
- repeatable generation workflows

---

## 16. What Agents Must Not Do

- Do not move business logic from the domain layer into infrastructure for convenience.
- Do not expose persistence entities in public repository APIs.
- Do not introduce module cycles.
- Do not add source code to the root aggregator project.
- Do not bypass mappers by returning persistence objects directly.
- Do not replace business-semantic domain methods with generic setters.
- Do not silently normalize naming or contract quirks that may be part of an established API.
- Do not introduce new structural patterns in one module that contradict the conventions of the repository.

---

## 17. Workflow for Adding a New Domain Capability

### Step 1 — Model the domain
- add or extend entity / value object / enum types
- add business methods and validation rules
- keep persistence concerns out

### Step 2 — Define contracts
- add or update repository ports
- add service-level capability ports if external behaviour is required

### Step 3 — Implement infrastructure
- add JPA model(s)
- add bidirectional mappers
- add helper interface(s)
- add repository implementation(s)
- add shared query or conversion utilities if needed

### Step 4 — Test both layers
- add domain unit tests
- add persistence integration tests
- verify cross-layer mapping assumptions

### Step 5 — Review impact
- check root dependency management
- check module guides if a new reusable pattern emerged
- document intentional exceptions and non-obvious decisions

---

## 18. Review Checklist

- [ ] The change respects module boundaries.
- [ ] Domain code remains persistence-agnostic.
- [ ] Infrastructure code returns domain types only.
- [ ] New names follow repository suffix and package patterns.
- [ ] Mapping exists for every new persistence/domain boundary.
- [ ] Tests cover happy path and failure path.
- [ ] Root POM changes are limited to shared build concerns.
- [ ] Documentation is updated when a reusable convention or known exception is introduced.

---

## 19. Quick Reference

```text
Domain module            → business language, repository ports, validators, services
Infrastructure module    → JPA entities, helpers, mappers, repository implementations
Root project             → aggregation, dependency management, project-wide guidance
Docs                     → architecture and feature documentation
Prompts                  → agent drafting and generation support
```

---

## 20. Last Updated

**Date:** 2026-04-22  
**Status:** Draft for human review

