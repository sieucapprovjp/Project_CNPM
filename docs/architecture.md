# Architecture

## Status

The initial technology architecture is accepted. Product-specific modules and external integrations remain undefined until the PRD is approved.

## Context

Describe Bluemoon, its users, external systems, and trust boundaries.

## System Shape

```text
User
  |
React single-page application
  |
Spring Boot REST API
  |
PostgreSQL
```

The frontend and backend are independently built applications. They communicate through the REST contract in `contracts/openapi.yaml`.

## Components

| Component | Responsibility | Owns data |
| --- | --- | --- |
| React frontend | User interface, routing, client validation, and server-state presentation | Browser-local UI state only |
| Spring Boot backend | Business rules, validation, security, transactions, and REST API | Application behavior |
| PostgreSQL | Durable relational data | Application records |
| Flyway | Versioned database migrations | Schema history |

## Data Flow

1. React Router selects the client view.
2. TanStack Query coordinates server state and calls the API through Axios.
3. Spring Security authenticates JWT bearer tokens through JJWT 0.13.0 and authorizes requests.
4. Spring Web validates and delegates requests to application services.
5. Spring Data JPA reads or writes PostgreSQL within backend-managed transactions.
6. The API returns JSON matching the OpenAPI contract.

Token issuance, refresh, revocation, and storage rules remain to be defined before authentication is implemented.

## Quality Attributes

- Security: Spring Security with JWT; detailed threat model and token lifecycle to be defined
- Availability: to be defined
- Performance: to be defined
- Scalability: to be defined
- Observability: application logging is required; metrics and tracing are to be defined

## Deployment

Docker Compose runs PostgreSQL 18 for local development. Production deployment uses Render:

- The React application is deployed as a Render Static Site.
- The Spring Boot application is deployed as a Render Web Service.
- Persistent data is hosted on Render PostgreSQL.

## Architecture Decisions

Record durable decisions in [`decisions.md`](decisions.md), including alternatives and consequences.
