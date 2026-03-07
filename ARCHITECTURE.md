# tt-data-league-core — Architecture & Implementation Details

This document contains the technical/implementation-level analysis that was previously part of `spec.md`. It documents modules, domain model details, JPA models, repository implementations, existing code snippets, and the concrete Specification-based solutions for tokenized name search.

---

## 1. High-level architecture

- Multi-module Maven project with at least two modules:
  - `tt-data-league-core-domain` — pure domain model (POJOs) and domain repository interfaces.
  - `tt-data-league-core-repository-jpa` — Spring Data JPA adapters: JPA entities (JPA suffix), repository helpers (JpaRepository + JpaSpecificationExecutor), mappers between domain and JPA objects, and repository implementations.

Responsibilities:
- Domain module: domain models (SeasonPlayer, Practicioner, ClubMember, SeasonPlayerResult, License, etc.) and domain-level repository interfaces (e.g., `SeasonPlayerRepository`).
- JPA repository module: concrete persistence layer, including `*JPA` entities, Spring Data repository interfaces (named `*RepositoryHelper`), mappers `XJPAToXMapper` and `XToXJPAMapper`, and `*RepositoryImpl` classes that adapt the JPA layer to the domain repository interfaces.

---

## 2. Domain model (detailed)

Domain classes (selected):

- `SeasonPlayer` (domain)
  - Fields: `UUID id`, `ClubMember clubMember`, `License license`, `String yearRange`.
  - Factory creation methods: `createNew`, `createExisting`.

- `ClubMember` (domain)
  - Fields: `UUID id`, `Club club`, `Practicioner practicioner`, `List<String> yearRanges`.
  - Mutators: addYearRange, removeYearRange, clearYearRanges, updateAllRanges.

- `Practicioner` (domain)
  - Fields: `UUID id`, `String firstName`, `String secondName`, `String fullName`, `Date birthDate`.

- `SeasonPlayerResult` (domain)
  - Fields include `UUID id`, `String season`, `SeasonPlayer seasonPlayer`, `CompetitionInfo`, `MatchInfo`.

- `PlayersSingleMatch` (domain)
  - Composite object built from two `SeasonPlayerResult` instances and competition metadata.

JPA counterparts and key mappings:

- `PracticionerJPA`
  - Annotated entity with columns: `id` (BINARY(16)), `first_name`, `second_name`, `full_name` (indexed), `birth_date`.

- `ClubMemberJPA`
  - `id`, `club` (ManyToOne ClubJPA), `practicioner` (ManyToOne PracticionerJPA), `yearRanges` (ElementCollection with order_index).

- `SeasonPlayerJPA`
  - `id`, `clubMember` (ManyToOne ClubMemberJPA, fetch=LAZY), `license_tag`, `license_ref`, `season` (yearRange).

Relationships (explicit):
- SeasonPlayer -> ClubMember (Many-to-one from SeasonPlayer perspective)
- ClubMember -> Practicioner (Many-to-one)

---

## 3. Repository layer (detailed)

Domain repository interface example: `SeasonPlayerRepository` declares multiple finders including single- and multi-token search methods.

JPA helper: `SeasonPlayerRepositoryHelper extends JpaRepository<SeasonPlayerJPA, UUID>, JpaSpecificationExecutor<SeasonPlayerJPA>` exposes derived queries:
- `findByClubMember_Practicioner_IdAndClubMember_Club_IdAndYearRange`
- `findByClubMember_Practicioner_FullNameAndClubMember_Club_NameAndYearRange`
- `findByClubMember_Practicioner_FullNameContainingIgnoreCase`
- `findByLicenseRefAndLicenseTag`
- `findByClubMember_Practicioner_Id`

Repository implementation: `SeasonPlayerRepositoryImpl` is annotated `@Transactional` and `@Component`, adapters JPA helpers and mappers, and implements `findBySimilarNames(List<String>)` using a Specification and deduplication step.

Key snippet from `SeasonPlayerRepositoryImpl.findBySimilarNames`:

```java
Specification<SeasonPlayerJPA> spec = (root, query, cb) -> {
    List<Predicate> predicates = nameFragments.stream()
            .filter(f -> f != null && !f.isBlank())
            .map(f -> cb.like(cb.lower(root.get("clubMember").get("practicioner").get("fullName")), "%" + f.toLowerCase() + "%"))
            .toList();

    if (predicates.isEmpty()) return cb.conjunction();

    return cb.and(predicates.toArray(new Predicate[0]));
};

List<SeasonPlayer> mapped = seasonPlayerRepositoryHelper.findAll(spec)
        .stream()
        .map(seasonPlayerJPAToSeasonPlayerMapper)
        .toList();

// Deduplicate by id preserving order
List<SeasonPlayer> results = new ArrayList<>();
Set<UUID> seen = new HashSet<>();
for (SeasonPlayer sp : mapped) {
    if (sp == null || sp.getId() == null) continue;
    if (seen.add(sp.getId())) results.add(sp);
}

return results;
```

Observation: The code uses `cb.and(...)` which means ALL non-blank fragments must match; a comment claims OR. Decide desired semantics and change accordingly.

---

## 4. Searching approaches compared

1. Spring Data derived queries — good for simple single-field searches (e.g., `findByClubMember_Practicioner_FullNameContainingIgnoreCase`).
2. JPQL with fixed arity parameters — inflexible for variable token counts.
3. Criteria / Specification — recommended for variable length token lists; supports both AND and OR semantics and keeps queries type-safe.

---

## 5. Specification examples (code)

(OR and AND variants)

```java
public final class SeasonPlayerSpecifications {
    public static Specification<SeasonPlayerJPA> practicionerFullNameContainsAny(List<String> tokens) {
        return (root, query, cb) -> {
            if (tokens == null || tokens.isEmpty()) return cb.conjunction();
            List<String> cleaned = tokens.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            if (cleaned.isEmpty()) return cb.conjunction();

            List<Predicate> predicates = cleaned.stream()
                    .map(token -> cb.like(cb.lower(root.get("clubMember").get("practicioner").get("fullName")), "%" + token + "%"))
                    .collect(Collectors.toList());

            return cb.or(predicates.toArray(new Predicate[0]));
        };
    }

    public static Specification<SeasonPlayerJPA> practicionerFullNameContainsAll(List<String> tokens) {
        return (root, query, cb) -> {
            if (tokens == null || tokens.isEmpty()) return cb.conjunction();
            List<String> cleaned = tokens.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(String::toLowerCase)
                    .collect(Collectors.toList());
            if (cleaned.isEmpty()) return cb.conjunction();

            List<Predicate> predicates = cleaned.stream()
                    .map(token -> cb.like(cb.lower(root.get("clubMember").get("practicioner").get("fullName")), "%" + token + "%"))
                    .collect(Collectors.toList());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
```

Usage in repository impl:

```java
Specification<SeasonPlayerJPA> spec = SeasonPlayerSpecifications.practicionerFullNameContainsAny(tokens);
List<SeasonPlayer> mapped = seasonPlayerRepositoryHelper.findAll(spec)
        .stream()
        .map(seasonPlayerJPAToSeasonPlayerMapper)
        .toList();
```

Note: maintain deduplication if required.

---

## 6. Observed files & mapping patterns

- Domain classes are mapped to JPA entities via dedicated mappers `XJPAToXMapper` and `XToXJPAMapper`.
- Repository implementations use a `*RepositoryHelper` Spring Data repository and mapper functions to convert between JPA and domain types.
- Transactionality is defined at repository implementation level (`@Transactional` on `SeasonPlayerRepositoryImpl`).

---

## 7. Tests and validation hints

- Add integration tests using H2 or Testcontainers to validate token search semantics (AND vs OR), deduplication, and paging.
- Validate mapper correctness by round-trip mapping in unit tests: domain -> JPA -> domain.

---

## 8. Recommendations (technical)

- Choose token semantics (AND vs OR) and document the behavior in API/Service contracts.
- Add pagination to public search endpoints and use Specification + Pageable.
- For production-scale contains searches, adopt database-level full-text search or trigram indexes.
- Keep deduplication logic where JPA queries join across collections or use `distinct` in the query when appropriate.

---

### Appendix: Quick links to classes found

- `tt-data-league-core-domain/src/main/java/org/cttelsamicsterrassa/data/core/domain/model/SeasonPlayer.java`
- `tt-data-league-core-domain/src/main/java/org/cttelsamicsterrassa/data/core/domain/model/Practicioner.java`
- `tt-data-league-core-repository-jpa/src/main/java/org/cttelsamicsterrassa/data/core/repository/jpa/season_player/impl/SeasonPlayerRepositoryImpl.java`
- `tt-data-league-core-repository-jpa/src/main/java/org/cttelsamicsterrassa/data/core/repository/jpa/season_player/impl/SeasonPlayerRepositoryHelper.java`
- `tt-data-league-core-repository-jpa/src/main/java/org/cttelsamicsterrassa/data/core/repository/jpa/practicioner/model/PracticionerJPA.java`


---

If you want, I will now implement one of the actionable items:
- Add a `SeasonPlayerSpecifications` class and update `SeasonPlayerRepositoryImpl` to use it (and make token semantics configurable).
- Add an H2 integration test for token search.
- Create paginated REST endpoints and DTO mappers for the example API.

Which should I implement next?
