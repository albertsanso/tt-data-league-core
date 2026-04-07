# tt-data-league-core-domain

## FEAT-001 Authentication

This module now includes a domain-level authentication slice under:

- `org.cttelsamicsterrassa.data.core.domain.model.auth`
- `org.cttelsamicsterrassa.data.core.domain.repository.auth`
- `org.cttelsamicsterrassa.data.core.domain.service.auth`

### What is included

- `User` entity (UUID, username, email, password hash, createdAt, active)
- `PasswordHasher` contract and bcrypt implementation
- `AuthenticationService` for register/authenticate/basic account state operations
- `UserValidator` and auth-specific exceptions

### Run tests

```powershell
mvn -f "C:\git\tt-data-league-core\pom.xml" test
```

