# FEAT-003 — Practitioner Repository Search by Space-Separated Name Fragments

## Build Plan
1. Confirm and freeze search contract in `tt-data-league-core-domain`:
   - Keep a single repository entry point for name search (e.g., `searchBySimilarName(String queryName)`).
   - Define `queryName` as user input that may contain one or multiple fragments separated by spaces.
   - Keep return type `List<Practicioner>` and existing `Practicioner` naming style.
2. Define parsing and matching semantics explicitly before coding:
   - Normalize `queryName` using `trim()` and split fragments with `\\s+`.
   - Discard empty fragments after split.
   - Use case-insensitive matching against `practicioner.fullName`.
   - Apply `AND` semantics: all fragments must be present in `fullName`.
3. Implement validation at repository implementation boundary:
   - Reject `null` `queryName`.
   - Reject blank `queryName`.
   - Throw `IllegalArgumentException` with a clear, stable message before querying.
4. Implement query composition in `tt-data-league-core-repository-jpa`:
   - Extend `PracticionerRepositoryHelper` with `JpaSpecificationExecutor<PracticionerJPA>`.
   - Build a JPA `Specification` in `PracticionerRepositoryImpl` that combines predicates:
     - `lower(fullName) like %fragment1%`
     - `AND lower(fullName) like %fragment2%`
     - ...for each normalized fragment.
   - Keep query parameterized through criteria APIs (no string-concatenated JPQL/SQL).
5. Keep mapping and output behavior unchanged:
   - Reuse `PracticionerJPAToPracticionerMapper` for all query results.
   - Return mapped list directly; return empty list when no matches are found.
6. Add/adjust repository integration tests in `tt-data-league-core-repository-jpa`:
   - Single-fragment contains-ignore-case match.
   - Multi-fragment query with extra spaces (`"  john   smi  "`) matches expected rows.
   - Multi-fragment query enforces `AND` behavior (rows missing one fragment are excluded).
   - No-result query returns empty list.
   - `null` and blank `queryName` throw `IllegalArgumentException`.
7. Run quality checks after implementation:
   - Run new practitioner repository tests.
   - Run full module tests for `tt-data-league-core-domain` and `tt-data-league-core-repository-jpa` to catch regressions.
8. Update feature tracking after implementation:
   - Keep `FEAT-003` as `planned` while this rebuilt plan is under review.
   - Move to `ready` only after plan approval.
   - Move to `in-progress` during coding and to `done` once tests are green and acceptance criteria are checked.

## Implementation Guidelines
### Code Style & Patterns
- Keep repository contract changes in `tt-data-league-core-domain` and JPA query behavior in `tt-data-league-core-repository-jpa`.
- Follow the existing helper + implementation + mapper structure already used by `ClubRepositoryImpl`, `SeasonPlayerRepositoryImpl`, and `PlayersSingleMatchRepositoryImpl`.
- Prefer JPA `Specification` composition for this feature because it now requires dynamic multi-fragment `AND` matching.
- Avoid unrelated cleanup while implementing the feature; keep the change set focused on the new repository use case.

### Security Considerations
- Validate and normalize user-provided `queryName` before query execution to prevent ambiguous search behavior and inconsistent error handling.
- Keep the query parameterized through Spring Data JPA; do not concatenate raw SQL or JPQL strings.
- Avoid logging raw search fragments unless needed for debugging and permitted by application logging standards.

### Testing Approach
- Add a repository-focused Spring Boot test in `tt-data-league-core-repository-jpa` following the style of `PlayersSingleMatchRepositoryImplTest`.
- Persist explicit `PracticionerJPA` fixtures with distinct `fullName` values to verify contains-ignore-case behavior against the real JPA query.
- Cover single-fragment and multi-fragment search scenarios, including extra whitespace normalization.
- Assert `AND` semantics across fragments so only full matches are returned.
- Assert validation failures for `null` and blank `queryName` directly against the repository implementation.

## Notes
- Existing code already uses both `searchBySimilarName` and `findBySimilarName` naming styles in other repositories; choose one explicitly for practitioner search and keep the plan/implementation consistent.
- The current feature text in `FEATURES.md` says “unit tests,” but repository integration-style tests in the JPA module are the most meaningful verification for this persistence behavior.
- Multi-fragment `contains` queries on `fullName` may require a future indexing/search optimization if practitioner volume grows significantly.

