# Architecture

## Status

The initial technology architecture and feature-oriented code organization are accepted. Product-specific module contents, data relationships, and external integrations remain subject to PRD approval.

## Context

BlueMoon serves authenticated Management Board staff. The browser is untrusted: the backend validates input, enforces authorization, and owns database access. Residents and households are managed data subjects, not v1.0 application users. Transactional email for staff activation is approved by ADR-005; utility-provider and other external integrations remain outside the approved v1.0 scope.

ADR-004 establishes internal account provisioning: an initial account administrator issues staff accounts. ADR-005 makes each unique staff email the login identifier and selects single-use activation links delivered by email, valid for 24 hours, through which staff set their own password before normal login. Administrators may send a replacement link for an unactivated account; issuing it invalidates every prior link. ADR-006 establishes an explicit, backend-configured, empty-store-only bootstrap that creates one pending initial administrator and sends that same activation flow without a preset password. Public self-registration is excluded. Email canonicalization, password policy, send-failure handling, email provider, and detailed business permissions remain to be defined; this policy is not yet implemented.

The backend owns activation validation, password setup, and email dispatch through the future email integration; the frontend presents the activation form. Mail-provider credentials belong only in backend configuration. No email provider or additional dependency is selected by this decision.

The bootstrap belongs in the account feature, after Flyway has established the approved account schema. It must be idempotent and concurrency-safe through database constraints and a transaction. It is not a Flyway data seed and exposes no public bootstrap endpoint. A successful database commit triggers email dispatch without logging the raw activation token. Explicit operator recovery may rotate the pending initial administrator's activation token only while no activated account administrator exists.

## Module Boundaries

- The five-person team owns work by feature across both applications, contracts, migrations, and tests.
- Backend code is grouped by feature under `vn.bluemoon`; each feature contains only the controller, DTO, service, repository, and entity classes it needs.
- Frontend code is grouped under `src/features/<feature>`, with shared bootstrapping in `app`, layout in `layouts`, UI in `components`, and API infrastructure in `lib/api`.
- Backend services may call another feature's documented service interface. Do not access another feature's repository directly or introduce cyclic service dependencies. Entity relationships across features require agreement in the database schema first.
- Frontend features do not import another feature's internal pages, components, or API implementation. Shared contracts belong in agreed public types; reusable technical code belongs in shared folders.
- Do not create generic CRUD base controllers/services or empty business modules in advance.

See [`backend.md`](backend.md), [`frontend.md`](frontend.md), and [`team_workflow.md`](team_workflow.md) for paths and coordination rules.

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

The following describes the target feature request flow. The current backend foundation has a health endpoint and denies unimplemented APIs; JWT authentication and frontend integration remain future milestones.

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
