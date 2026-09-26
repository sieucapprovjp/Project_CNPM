# Backend

## Status

The backend stack is selected but the application has not been initialized.

## Stack

- Java 17
- Spring Boot 3.5.16 with Maven Wrapper
- Spring Web
- Spring Data JPA
- Spring Validation
- Spring Security with JWT using JJWT 0.13.0
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

## Planned Layers

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

## Security

- Authentication and authorization: Spring Security with JWT bearer tokens
- Token issuance, expiration, refresh, and revocation: to be defined before implementation
- Rate limiting: to be defined
- Input validation: required at system boundaries
- Secret management: to be defined before deployment

## Verification

Use JUnit 5 and Mockito for unit tests and Spring Boot Test for framework integration tests. Maven tests must run in CI after initialization.
