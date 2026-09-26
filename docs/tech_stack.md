# Technology Stack

## Status

This is the final approved technology stack. Application dependency versions must also be locked in the build manifests created during initialization.

## Selection Criteria

- Meets product and course requirements
- Familiar enough for the team to deliver safely
- Actively maintained and documented
- Supports automated testing and deployment
- Avoids unnecessary operational complexity

## Frontend

| Technology | Version | Purpose |
| --- | --- | --- |
| Node.js | 22 LTS | Frontend runtime and tooling |
| npm | Version supplied with the approved Node.js release | Package manager |
| React | 19 | UI framework |
| TypeScript | 6 | Statically typed frontend language |
| Vite | 8 | Development server and build tool |
| HeroUI | 3 | UI component library |
| Tailwind CSS | 4 | Utility-first styling |
| React Router | 8 | Client-side routing |
| Axios | 1 | HTTP client |
| TanStack Query | 5 | Server-state fetching and caching |

Exact frontend dependency versions are defined by `frontend/package.json` and `frontend/package-lock.json` after initialization. npm is mandatory; do not use yarn, pnpm, or bun.

## Backend

| Technology | Version | Purpose |
| --- | --- | --- |
| Java | 17 | Backend language and runtime |
| Spring Boot | 3.5.16 | Application framework |
| Maven | Version locked by Maven Wrapper | Build and dependency management |
| Spring Web | Managed by Spring Boot | REST API layer |
| Spring Data JPA | Managed by Spring Boot | Persistence abstraction |
| Spring Validation | Managed by Spring Boot | Request and model validation |
| Spring Security | Managed by Spring Boot | Authentication and authorization |
| JJWT | 0.13.0 | JWT creation and verification |
| Flyway | Managed by Spring Boot | Database migrations |

Spring Boot must remain on the 3.x line unless an architecture decision approves a major upgrade. Backend code must not use Java features newer than Java 17.

## Data And API

| Technology | Version | Purpose |
| --- | --- | --- |
| PostgreSQL | 18 | Relational database |
| REST | N/A | API architectural style |
| OpenAPI | 3.1 | Machine-readable API contract |
| springdoc-openapi | 2.9.1 | OpenAPI generation and Swagger UI integration |

## Testing

| Technology | Version | Scope |
| --- | --- | --- |
| JUnit 5 | Managed by Spring Boot | Backend unit tests |
| Mockito | Managed by Spring Boot | Backend test doubles |
| Spring Boot Test | Managed by Spring Boot | Backend integration tests |
| Vitest | Compatible release locked in `package-lock.json` | Frontend unit and integration tests |
| React Testing Library | Compatible release locked in `package-lock.json` | Frontend component tests |

## Tooling

| Technology | Version | Purpose |
| --- | --- | --- |
| ESLint | Compatible release locked in `package-lock.json` | Frontend static analysis |
| Prettier | Compatible release locked in `package-lock.json` | Frontend formatting |
| Docker | Current compatible release | Container runtime for local development |
| Docker Compose | Compose v2 | Local multi-service environment |
| GitHub Actions | Hosted service | Continuous integration |

## UI Policy

HeroUI v3 and Tailwind CSS v4 are the approved UI tools. Do not introduce Material UI, Ant Design, Bootstrap, or another component library without an explicit architecture decision.

## Local Development

PostgreSQL 18 runs through Docker Compose. Application processes may run directly on the host during development.

## Deployment

| Component | Render service |
| --- | --- |
| Frontend | Static Site |
| Backend | Web Service |
| PostgreSQL | Render PostgreSQL |

## Version Policy

- Major framework upgrades require an accepted architecture decision.
- Agents must not independently upgrade Java, Spring Boot, React, HeroUI, or PostgreSQL.
- Spring Boot must not be upgraded to 4.x without an accepted architecture decision.
- Java code must remain compatible with Java 17.
- Patch and minor dependency updates must preserve compatibility and update the relevant lockfile or build manifest.
- Frontend dependency changes must update both `package.json` and `package-lock.json`.
