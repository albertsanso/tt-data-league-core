# FEATURES.md — Feature Registry & Build Plans

This file is the single source of truth for planned, in-progress, and completed features.

**For humans:** Add new features under `## Backlog` using the template below.
**For agents:** Only work on features marked `status: ready`. Update status as you progress. Never modify features marked `status: done` or `status: in-progress` unless explicitly asked.

---

## Status Legend

| Status | Meaning |
|-|-|
| `idea` | Captured but not planned yet — no build plan written |
| `planned` | Build plan written, not yet ready to implement |
| `ready` | Build plan approved, agent can start |
| `in-progress` | Currently being implemented |
| `done` | Shipped |
| `blocked` | Waiting on a dependency or decision |

---

## Template

Copy this block to add a new feature:

```
### [FEAT-000] Feature Name
- **Status:** idea
- **Priority:** low | medium | high
- **Effort:** small (< 2h) | medium (2–8h) | large (> 8h)
- **Depends on:** —

#### Goal
One sentence: what problem does this solve for the user?

#### Acceptance Criteria
- [ ] Criterion 1
- [ ] Criterion 2

#### Feature Details
→ See [FEAT-000-DETAILS.md](./FEAT-000-DETAILS.md) for a detailed breakdown of the feature, build plan, and implementation steps.
```

### Feature Details file format
```
# Build Plan
> Fill this in when status moves to `planned`.

1. Step 1
2. Step 2
...

# Implementation Guidelines
## Code Style & Patterns
## Security Considerations
## Testing Approach

# Notes
Any open questions, design decisions, or links.
```

## In Progress


## Backlog

### [FEAT-005] Query for the seasons a player has participated in, given practitioner names fragments
- **Status:** idea
- **Priority:** medium
- **Effort:** medium
- **Depends on:** FEAT-003

#### Goal
Implement a query that retrieves the seasons a player has participated in based on practitioner name fragments, allowing users to easily find relevant season information for specific practitioners.

#### Acceptance Criteria
- [ ] Given valid practitioner name fragments, the service returns a list of seasons that the player has participated in, along with relevant details (e.g., season name, year).
- [ ] The search for practitioner names is case-insensitive and supports partial matches (e.g., searching for "Smith" should return seasons involving "John Smith" and "Jane Smith").
- [ ] If no seasons are found for the given practitioner name fragments, the service returns an empty list.
- [ ] If the practitioner name fragments are `null` or blank, the service throws an `IllegalArgumentException` with an appropriate error message.
- [ ] The implementation includes unit tests covering the service method's behavior for valid inputs, edge cases (e.g., no seasons found), and invalid inputs (e.g., `null` or blank practitioner name fragments).
- [ ] The implementation follows the existing code style and patterns used in the project, ensuring consistency and maintainability.
- [ ] The implementation is efficient and does not introduce significant performance issues, even when dealing with large datasets of seasons and practitioners.
- [ ] The implementation reuses existing repository methods where possible to avoid code duplication and maintain a clean architecture.
- [ ] The implementation is designed to be easily extendable in the future, allowing for additional filtering criteria (e.g., by competitionInfo or matchDayNumber) without requiring significant refactoring.

#### Feature Details
→ See [FEAT-005-DETAILS.md](./FEAT-005-DETAILS.md) for a detailed breakdown of the feature, build plan, and implementation steps.

---

## Done

---

### [FEAT-004] Add Roles and Permissions to User Model
- **Status:** done
- **Priority:** medium
- **Effort:** medium
- **Depends on:** FEAT-001

#### Goal
Extend the user model to include roles and permissions, allowing for role-based access control (RBAC) in the application.

#### Acceptance Criteria
- [x] User model extended to include roles and permissions.
- [x] Role and Permission entities defined with appropriate relationships.
- [x] Implementation of role-based access control (RBAC) in the application.
- [x] Unit tests for the new user model, role, and permission entities, as well as tests for RBAC functionality.
- [x] The implementation follows the existing code style and patterns used in the project, ensuring consistency and maintainability.
- [x] The implementation is efficient and does not introduce significant performance issues, even when dealing with a large number of users, roles, and permissions.

#### Feature Details
→ See [FEAT-004-DETAILS.md](./FEAT-004-DETAILS.md) for a detailed breakdown of the feature, build plan, and implementation steps (12 phases covering domain source restoration, infrastructure JPA layer, and full test suite).


---

### [FEAT-003] Add Practicioner repository use case for finding practicioners by name fragments.
- **Status:** done
- **Priority:** medium
- **Effort:** medium
- **Depends on:** —

#### Goal
Implement a repository use case that allows finding practitioners by name fragments, enabling users to search for practitioners even if they only remember part of the name.
Support `query.name` as a space-separated list of fragments instead of exact full-name matching.

#### Acceptance Criteria
- [x] Given a valid `query.name`, the repository splits it into fragments by whitespace and performs case-insensitive matching against practitioner `fullName`.
- [x] Given multiple fragments (e.g., `"john smi"`), the repository returns practitioners whose `fullName` contains all provided fragments, regardless of order and case.
- [x] If no practitioners are found for the given name fragment, the repository returns an empty list.
- [x] If `query.name` is `null` or blank, the repository throws an `IllegalArgumentException` with an appropriate error message.
- [x] The implementation includes unit tests covering single-fragment and multi-fragment queries, no-result behavior, and invalid inputs (`null` or blank `query.name`).
- [x] The implementation follows the existing code style and patterns used in the project, ensuring consistency and maintainability.

#### Feature Details
→ See [FEAT-003-DETAILS.md](./FEAT-003-DETAILS.md) for a detailed breakdown of the feature, build plan, and implementation steps.

---

### [FEAT-002] Query for matches results and their associated match details for a given Practicioner name like, season, competitionInfo, and matchDayNumber
- **Status:** done
- **Priority:** high
- **Effort:** large
- **Depends on:** —

#### Goal
Find matches results and their associated match details for a given Practitioner name, season, competitionInfo, and matchDayNumber,
allowing users to easily access relevant information about matches involving specific practitioners.

#### Acceptance Criteria
- [x] Given a valid practitioner name fragment, season, competitionInfo, and matchDayNumber, the service returns a list of matches results with associated match details that match the criteria.
- [x] The search for practitioner names is case-insensitive and supports partial matches (e.g., searching for "Smith" should return matches involving "John Smith" and "Jane Smith").
- [x] If no matches are found for the given criteria, the service returns an empty list.
- [x] If the practitioner name fragment is `null` or blank, the service throws an `IllegalArgumentException` with an appropriate error message.
- [x] The implementation includes unit tests covering the service method's behavior for valid inputs, edge cases (e.g., no matches found), and invalid inputs (e.g., `null` or blank practitioner name fragment).
- [x] The implementation follows the existing code style and patterns used in the project, ensuring consistency and maintainability.
- [x] The implementation is efficient and does not introduce significant performance issues, even when dealing with large datasets of matches and practitioners.

#### Feature Details
→ See [FEAT-002-DETAILS.md](./FEAT-002-DETAILS.md) for a detailed breakdown of the feature, build plan, and implementation steps.

---

### FEAT-001: User Authentication
- **Status:** done
- **Priority:** high
- **Effort:** medium
- **Depends on:** —

#### Goal
Define domain and persistence for user authentication, including password hashing.

#### Acceptance Criteria
- [x] User model defined with necessary fields (e.g., username, email, password hash)
- [x] Password hashing implemented using a secure algorithm (e.g., bcrypt)
- [x] Persistence layer set up for storing user data
- [x] Unit tests for user model and password hashing


#### Feature Details
→ See [FEAT-001-DETAILS.md](./FEAT-001-DETAILS.md) for a detailed breakdown of the feature, build plan, and implementation steps.
---
