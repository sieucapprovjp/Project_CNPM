# Backend

## Status

The backend foundation is initialized. It provides a health endpoint, shared HTTP errors, request IDs, a default-deny security guard, local CORS, and PostgreSQL/Flyway configuration. No product entities or authentication flows are implemented.

## Stack

- Java 17
- Spring Boot 3.5.16 with Maven Wrapper
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security; JJWT 0.13.0 will be added when token behavior is approved and implemented
- Flyway
- PostgreSQL 18
- springdoc-openapi 2.9.1 with Swagger UI
- JUnit 5, Mockito, and Spring Boot Test

## Responsibilities

- Enforce business rules and authorization.
- Validate all external input.
- Provide APIs matching `contracts/openapi.yaml`.
- Own database access and transactional consistency.
- Emit useful operational logs without exposing secrets.

## Structure And Layers

The base Java package is `vn.bluemoon`. The shared packages and `system/controller/HealthController` exist; `<feature>` illustrates the structure for future approved work:

```text
backend/
  src/main/java/vn/bluemoon/
    BluemoonApplication.java
    config/
    common/exception/
    system/controller/
    <feature>/
      controller/
      dto/
      service/
      repository/
      entity/
  src/main/resources/
    application.yaml
    application-local.yaml
    db/migration/
  src/test/java/vn/bluemoon/
```

Tests mirror the package under test. Create feature folders only when approved work needs them. Shared code must be technical and reusable; feature rules stay inside the owning feature.

Use only the layers justified by the application. The initial request flow is:

```text
Controller -> Service -> Repository -> PostgreSQL
```

Controllers own HTTP mapping and request validation. Services own business rules and transaction boundaries. Repositories own persistence access. API DTOs must not expose JPA entities directly.

All code must compile for Java 17 and must not use newer Java language features. Spring Boot must not be upgraded to 4.x without an accepted architecture decision.

## Configuration

- Read runtime configuration from environment variables.
- Fail clearly when required values are missing.
- Never commit production credentials.
- Maven Wrapper 3.3.4 downloads Maven 3.9.11 with a SHA-256 check. The build targets Java 17.
- Root launch scripts load the permitted backend settings from `.env` as literal strings. Existing process variables take precedence.
- Hibernate validates the schema; only Flyway changes it. The migration folder is intentionally empty until the first domain schema is approved.

## Security

- Current guard: stateless Spring Security, with form login, HTTP Basic, sessions, and logout disabled. Only `GET /health` is public by default; all other requests are denied. No generated development user or password is created.
- `local` enables public Swagger UI and generated OpenAPI documentation. Both are disabled by default outside that profile. Do not use the `local` profile in production.
- CSRF is disabled for the planned bearer-token API; CORS accepts the exact `FRONTEND_ORIGIN` and does not allow credentialed cookies. Revisit this configuration if the later auth decision uses cookies.
- Authentication target: JWT bearer tokens, pending the token policy below.
- Token issuance, expiration, refresh, and revocation: to be defined before implementation
- Rate limiting: to be defined
- Input validation: required at system boundaries
- Secret management: to be defined before deployment

## Verification

Use JUnit 5 and Mockito for unit tests and Spring Boot Test for framework integration tests. Maven tests must run in CI after initialization.

`mvnw test` runs MVC/security/error tests without a database. `mvnw verify` also runs `FoundationIT` against a configured PostgreSQL 18 database and packages the application; it intentionally fails when the database is unavailable. GitHub Actions supplies an isolated PostgreSQL 18 service for this check.

`/health` is a liveness response, not a continuous database-readiness probe. Startup and integration tests verify database access and Flyway wiring. Future migrations must add their own schema verification tests.

### Verification Record (2026-09-26)

Maven Wrapper `verify`, invoked through the Windows launcher on JDK 17, passed 12 MVC/security/error tests and 1 integration test using a temporary native PostgreSQL 18.6 instance. Packaging produced the executable JAR. Compose configuration was validated, but container startup could not be exercised because the host Docker engine was unavailable. The GitHub Actions workflow is configured but has not been run remotely in this task.

The Boot-managed Flyway release emits a PostgreSQL 18 compatibility warning (its tested maximum is PostgreSQL 17). Connection, history-table creation, validation, and application startup passed. This does not validate future domain migrations; check compatibility and test those migrations before merging them. Keep the approved dependency policy rather than silently overriding the managed version.
