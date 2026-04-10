# FEAT-002 — Query Match Results by Practitioner Name

## Build Plan
1. Confirm query contract and response model in the domain module:
   - Reuse `PlayersSingleMatchRepository#findBySeasonAndCompetitionAndMatchDayNumber(...)` as the single retrieval entry point.
   - Keep `List<PlayersSingleMatch>` as output because it already carries match result plus associated match details.
2. Implement/adjust domain service behavior in `MatchQueryService`:
   - Validate `practicionerName` is not `null` and not blank.
   - Throw `IllegalArgumentException` with a clear message for invalid fragments.
   - Delegate valid requests to the repository with all filters.
3. Implement practitioner-name filtering in JPA repository implementation:
   - Update `PlayersSingleMatchRepositoryImpl#buildPracticionerNameSpec(...)`.
   - Use case-insensitive partial matching (`lower(...) like %fragment%`) for both local and visitor practitioner full names.
4. Ensure full filter composition in repository query:
   - Season filter.
   - CompetitionInfo filter (all relevant fields used by current model).
   - Match day number filter.
   - Practitioner name fragment filter.
5. Add domain unit tests (`tt-data-league-core-domain`):
   - Returns repository results when input is valid.
   - Returns empty list when repository has no matches.
   - Throws `IllegalArgumentException` for `null` fragment.
   - Throws `IllegalArgumentException` for blank fragment.
   - Verifies repository invocation with expected parameters.
6. Add repository query tests (`tt-data-league-core-repository-jpa`):
   - Case-insensitive matching succeeds.
   - Partial matching succeeds.
   - Combined filtering by season + competitionInfo + matchDayNumber narrows correctly.
   - No matches returns empty list.
7. Validate quality gates:
   - Run module tests for `domain` and `repository-jpa`.
   - Check no regressions in existing auth/match tests.
8. Update feature tracking docs after implementation:
   - Mark FEAT-002 acceptance criteria as completed.
   - Move feature status only when implementation and tests are green.

## Implementation Guidelines
### Code Style & Patterns
- Keep domain logic in `tt-data-league-core-domain` and persistence query details in `tt-data-league-core-repository-jpa`.
- Preserve existing naming and package conventions already used for match query services/repositories.
- Keep service methods small and explicit: validate early, delegate once, return directly.
- Prefer composition of JPA `Specification` fragments over large monolithic predicates.
### Security Considerations
- Validate user-provided practitioner fragment before query execution to avoid ambiguous behavior.
- Keep query parameterized through JPA criteria/specifications (no string-concatenated JPQL/SQL).
- Avoid logging raw user-input fragments in error logs unless required for diagnostics.
### Testing Approach
- Domain tests: mock repository and assert behavioral contract (validation + delegation).
- Repository tests: use persisted fixtures to verify actual query semantics end-to-end for filtering behavior.
- Cover edge cases for case-insensitivity and partial matches (`"smith"`, `"Smi"`, mixed case).
- Keep tests deterministic with explicit test data for season/competition/day collisions.


## Notes
- Known trade-off: `lower(fullName) like '%fragment%'` may not use indexes efficiently on large datasets.
- If performance degrades, consider a normalized searchable column or full-text strategy in a future feature.
- Open question: whether `season`, `competitionInfo`, and `matchDayNumber` should also be validated for nulls at service level (current FEAT-002 acceptance criteria only mandates practitioner-fragment validation).

