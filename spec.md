# tt-data-league-core — Functional Specification

This document describes the high-level, functional view of the project: its primary use cases, domain capabilities, public-facing operations, and example API/interaction flows. Implementation and technical details have been moved to `ARCHITECTURE.md` (see the "Implementation & Technical Details" section below).

Goal: help product owners, backend developers, and integrators understand what the system does, its main operations, and how to interact with it without diving into implementation specifics.

---

## 1. Overview — What this module offers

The `tt-data-league-core` project provides domain-level management and querying of table-tennis competition data. Core responsibilities include:

- Managing practitioners (players) and their association to clubs across seasons.
- Tracking season-specific player entries (`SeasonPlayer`) including licenses and season ranges.
- Recording match-level results (`SeasonPlayerResult`) and composing single-match pairings (`PlayersSingleMatch`).
- Searching and retrieving player-season records by identifiers, names, licenses, and flexible name-matching tokens.

Primary consumers:
- Backend services that expose REST/GraphQL APIs.
- Batch processors importing competition and results data.
- Reporting and analytics components that query season/player/result data.

---

## 2. Key domain capabilities (user-facing use cases)

Below are the inferred, high-level use cases derived from the domain model and repository methods.

1. Register a Player (Practicioner) and assign them to a Club for a Season
   - Input: practitioner personal data (firstName, secondName, fullName, birthDate), club identifier, season/yearRange, license details.
   - Outcome: create or update `Practicioner`, create `ClubMember` linking practitioner to Club, add `SeasonPlayer` record for the season with license info.
   - Who: data importers, club administrators.

2. Find a SeasonPlayer by identifiers
   - Input: SeasonPlayer id OR (practicionerId, clubId, season) OR (practicionerName, clubName, season).
   - Outcome: return the SeasonPlayer domain object (with clubMember, license, and inferred season info).
   - Who: UI components showing player's season detail, match processors.

3. Search SeasonPlayers by practitioner full name (single-term)
   - Input: single string fragment (e.g., "Smith" or "John")
   - Outcome: case-insensitive, partial-match list of SeasonPlayers where practitioner's fullName contains the fragment.
   - Who: quick-search UIs, autocomplete, admin search.

4. Search SeasonPlayers by practitioner name tokens (multi-term)
   - Input: list of fragments/tokens (e.g., ["john", "smi"])
   - Outcome: return SeasonPlayers that match either ALL tokens (AND semantics) or ANY token (OR semantics) depending on caller choice. This covers searches like "John Smith" split into tokens.
   - Who: advanced search UIs, importer deduplication.

5. Query SeasonPlayers by License
   - Input: licenseRef and licenseTag
   - Outcome: list SeasonPlayers matching the exact license reference and tag.
   - Who: license validation, transfer checks.

6. Record and query season match results
   - Input: SeasonPlayerResult (season, competition info, match info, game points)
   - Outcome: persist match result, enable derivation of per-player match history and pairings (PlayersSingleMatch).
   - Who: match ingestion, standings computation, per-player history pages.

7. Compose matches from results
   - Input: SeasonPlayerResult records for a match (two players)
   - Outcome: build `PlayersSingleMatch` objects used for match listing and analytics.
   - Who: schedule/fixture viewers, reports.

---

## 3. Example API contracts (conceptual)

These are example service-level endpoints / methods that correspond to the domain capabilities. They are intentionally framework-agnostic — adapt to REST controllers or GraphQL resolvers.

1) GET /seasonPlayers/{id}
- Returns: SeasonPlayer
- Behavior: fetch by id, 404 if not found

2) GET /seasonPlayers/search?name=smith&page=0&size=20
- Returns: paginated SeasonPlayer list
- Behavior: case-insensitive partial match on practitioner's fullName

3) POST /seasonPlayers/search-tokens
- Body: { tokens: ["john", "smi"], mode: "AND" | "OR", page, size }
- Returns: paginated SeasonPlayer list
- Behavior: use tokenized Specification search; mode selects AND or OR semantics

4) POST /seasonPlayers (create or update)
- Body: SeasonPlayer DTO (with embedded ClubMember and Practicioner data)
- Returns: created/updated SeasonPlayer id
- Behavior: Upsert-friendly; uses mappers to convert domain -> JPA and persist.

5) POST /seasonPlayerResults
- Body: SeasonPlayerResult DTO
- Returns: created result id
- Behavior: persist match result and optionally emit events for standings recalculation.

---

## 4. Interaction flows (happy paths)

Flow A — Importing a player's season and first match result
1. Importer provides Practicioner details and club mapping.
2. System checks if Practicioner exists (by id or fullName). If not, creates Practicioner.
3. Create ClubMember linking practitioner to club; update yearRanges.
4. Create SeasonPlayer for the given season with license.
5. Persist SeasonPlayerResult for matches as they arrive; build PlayersSingleMatch if needed.

Flow B — Searching for player during registration to avoid duplicates
1. Administrator types "john smi" into a search field.
2. Frontend splits into tokens and calls `POST /seasonPlayers/search-tokens` with `mode=AND` to prefer tight matches.
3. Backend runs tokenized spec and returns matching SeasonPlayers; UI presents possible duplicates.

Flow C — Retrieving player's historical matches
1. Client requests SeasonPlayer by ID
2. Backend returns SeasonPlayer and (optionally) associated SeasonPlayerResult list or provides a paged endpoint for results.

---

## 5. Non-functional concerns and recommendations (user-facing)

- Performance: tokenized contains-search (`%token%`) is expensive for large datasets; prefer full-text search or trigram indexes for production workloads.
- Pagination: surface paginated endpoints for all list queries.
- Duplicate prevention: provide a deduplication mode in searches (first-match priority) and show similar results in the UI.
- Data integrity: license uniqueness should be enforced by business rules (not necessarily DB constraints) depending on local needs.

---

## 6. Implementation & Technical Details

Implementation details, repository structure, JPA models, and concrete code examples (Specifications, repository helper methods, and an analysis of `SeasonPlayerRepositoryImpl.findBySimilarNames`) are available in `ARCHITECTURE.md`.

---

## 7. Next steps I can implement for you

- Create `SeasonPlayerSpecifications` (utility class) and wire OR/AND semantics into `SeasonPlayerRepositoryImpl`.
- Add an H2-based integration test demonstrating AND vs OR token search behaviors.
- Add paginated endpoints and DTO mappers for the example APIs above.

Tell me which follow-up you'd like and I'll implement it.


---

(Reference: technical analysis previously placed in `spec.md` has been moved to `ARCHITECTURE.md`.)
