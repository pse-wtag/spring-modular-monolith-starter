# Spring Modular Monolith Starter

Production-grade modular monolith starter built with **Spring Boot 4** and **Java 25**. It provides a ready-to-use foundation for a modular backend with Keycloak-based authentication/authorization, a MySQL-backed persistence layer, and a CI/CD pipeline out of the box.

**Using this as a template?** A few values in the root `pom.xml` are specific to the original repo/author and must be updated for your own project:
- `<scm>` block (`connection`, `developerConnection`, `url`) — point these at your own repository.
- `spring-boot-maven-plugin` image config — update the `BP_OCI_AUTHORS` env var (and review `BP_OCI_SOURCE`, which is derived from `project.scm.url`) to reflect your own name/org.

## Modules

The project is a Maven multi-module build (`pom.xml`) split into three modules with a clear dependency direction: `rest → service → persistence`.

- **`persistence`** — JPA entities, repositories, Flyway migrations, and audit support.
  - `entity/User.java` — the core `User` entity (firstname, lastname, age, gender, username, email, `keycloakId`, role).
  - `enumeration/` — `Role`, `Permission`, `Gender`.
  - `audit/` — `Auditable` base entity + `AuditorAwareImpl` for created/modified-by tracking.
  - `db.migration/` — Flyway SQL migrations.
- **`service`** — business logic and integration with Keycloak.
  - `KeycloakService` / `KeycloakServiceImpl` — user registration against Keycloak, token exchange.
  - `KeycloakTokenProvider`, `KeycloakMapper`, `UserMapper` — token handling and DTO/entity mapping (MapStruct).
- **`rest`** — the Spring Boot application entry point and web layer.
  - `Application.java` — main class.
  - `controller/AuthenticationController.java` — `POST /api/v1/auth/register`.
  - `configuration/` — `SecurityConfiguration`, `KeycloakConfiguration`, `AuthoritiesConverter`, `KeycloakAuthenticationConverter`, `AuditConfiguration`.
  - `advice/GlobalExceptionHandler.java` — centralized error handling.

## Tech stack

- Java 25, Spring Boot 4 (starter parent)
- Spring Security + OAuth2 Client + OAuth2 Resource Server, backed by **Keycloak**
- Spring Data JPA + Flyway, MySQL (application datasource) and PostgreSQL (Keycloak's own datastore)
- MapStruct + Lombok
- Spring Boot Actuator (health, info, metrics)
- Virtual threads enabled (`spring.threads.virtual.enabled=true`)

## Security model

Two independent security filter chains are configured (`SecurityConfiguration`):

- **Resource server chain** (`/api/v1/mono/**`) — stateless, JWT-based (bearer token issued by Keycloak), with fine-grained authorization per HTTP method using `Permission` values (`ADMIN_*` / `USER_*`).
- **Client chain** (everything else) — OAuth2 login (authorization code flow) against Keycloak, session-based, with CSRF protection and OIDC logout.

`/api/v1/auth/**`, `/error`, and a few other paths are publicly whitelisted; `/actuator/**` requires `ADMIN_READ`.

## Local infrastructure

`infra/docker-compose.yaml` spins up the supporting services:

- **MySQL** — application database
- **PostgreSQL** — Keycloak's database
- **Keycloak** (with realm auto-import from `infra/keycloak/realm-export.json`)

Copy `infra/.env.example` to `infra/.env` (or `local.env`) and fill in the required values (DB credentials, Keycloak admin credentials, issuer/client configuration), then:

```bash
cd infra
docker compose up -d
```

## Running the application

```bash
./mvnw spring-boot:run -pl rest -am -P dev
```

The `dev` Maven profile is active by default (`spring.profiles.active=dev`); a `test` profile is also available. Configuration is split across `application.properties` (common), `application-dev.properties` (DB/Hikari/Flyway/logging), and `application-test.properties`.

The app listens on port `8080` by default.

## Git hooks & code quality

`pom.xml` auto-installs the repo's git hooks (`core.hooksPath=.githook`) on `mvn initialize`:

- **`pre-commit`** — secret scanning (gitleaks + local env-value check) and Spotless auto-formatting of staged Java files.
- **`pre-push`** — runs `mvn clean test` when Java/Maven/config files are part of the push.

Spotless (Java import order, unused-import removal, POM sorting) runs automatically during `mvn validate`.

## CI/CD

GitHub Actions (`.github/workflows/ci.yml`) runs on pushes/PRs to `main`, delegating to a shared reusable pipeline (`pse-wtag/java-ci-cd-template`) that builds, tests, and signs artifacts (Java 25, Maven, cosign keyless signing, CodeQL SARIF upload).
