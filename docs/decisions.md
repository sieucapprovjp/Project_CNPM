# Decision Log

Use this log for decisions that affect architecture, product behavior, dependencies, security, data, or team workflow.

## Decision Template

```text
## ADR-NNN: Title

- Date: YYYY-MM-DD
- Status: Proposed | Accepted | Superseded | Rejected

### Context
Why a decision is needed.

### Options
The realistic alternatives considered.

### Decision
The selected option.

### Consequences
Benefits, costs, risks, and follow-up work.
```

## ADR-001: Establish The Documentation-First Repository Structure

- Date: 2026-09-26
- Status: Accepted

### Context

Bluemoon does not yet have an approved product direction or technology stack.

### Decision

Create product, architecture, API, data, testing, setup, and workflow documents before application initialization.

### Consequences

The team has explicit places to record future choices. Placeholder content must be replaced as decisions are approved, and implementation should not assume technologies that remain undecided.

## ADR-002: Select The Final Application Stack

- Date: 2026-09-26
- Status: Accepted

### Context

Bluemoon needs a typed web frontend, a structured Java backend, relational persistence, contract documentation, automated tests, and repeatable local and CI tooling.

### Options

Individual alternatives were not recorded before the stack was selected.

### Decision

Use the following stack:

- Node.js 22 LTS, npm, React 19, TypeScript 6, Vite 8, HeroUI 3, Tailwind CSS 4, React Router 8, Axios 1, and TanStack Query 5 for the frontend.
- Java 17, Spring Boot 3.5.16, Maven, Spring Web, Spring Data JPA, Spring Validation, Spring Security, JJWT 0.13.0, and Flyway for the backend.
- PostgreSQL 18 for persistent data.
- REST, OpenAPI 3, springdoc-openapi 2.9.1, and Swagger UI for the API.
- JUnit 5, Mockito, Spring Boot Test, Vitest, and React Testing Library for tests.
- ESLint, Prettier, Docker Compose, and GitHub Actions for development tooling.
- Render Static Site, Render Web Service, and Render PostgreSQL for production deployment.

### Consequences

The repository will contain independently built frontend and backend applications connected by the OpenAPI contract. Frontend dependencies use npm and are locked by `package.json` and `package-lock.json`. Database changes require Flyway migrations. GitHub Actions must run Maven and npm checks after initialization. Major framework upgrades require a new architecture decision, and agents must not independently upgrade Java, Spring Boot, React, HeroUI, or PostgreSQL.
