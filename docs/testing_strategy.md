# Testing Strategy

## Goals

Tests should protect important product behavior, contracts, data integrity, and security boundaries without duplicating implementation details.

## Test Levels

| Level | Scope | Expected use |
| --- | --- | --- |
| Unit | Isolated business logic | Fast feedback for rules and edge cases |
| Integration | Database, framework, or service boundaries | Verify real component interaction |
| Contract | OpenAPI and client/server compatibility | Prevent interface drift |
| End-to-end | Critical user journeys | Validate deployable system behavior |

## Backend

- JUnit 5 is the test framework.
- Mockito provides test doubles for isolated unit tests.
- Spring Boot Test covers framework wiring, security, persistence, and HTTP integration.
- PostgreSQL integration tests must exercise database-specific behavior when an in-memory substitute would differ.

## Frontend

- Vitest is the test runner.
- React Testing Library verifies components through user-observable behavior.
- Network behavior should be tested at the API boundary rather than by mocking TanStack Query internals.
- Critical routing, loading, empty, success, and error states require coverage.

## Required Coverage

- Acceptance criteria for each implemented user story
- Authorization and validation failures
- Important database constraints and transactions
- API responses documented in OpenAPI
- Critical frontend loading, empty, error, and success states

## Test Data

- Use deterministic fixtures or factories.
- Do not use production data or real credentials.
- Isolate tests so order does not affect results.

## Continuous Integration

GitHub Actions validates repository structure and the OpenAPI file, and runs backend `verify` on Java 17 with a PostgreSQL 18 service. The frontend checks below will be added when that application is initialized:

- Backend Maven test and package
- Frontend ESLint and Prettier checks
- Frontend TypeScript checking
- Frontend Vitest tests
- Frontend Vite production build

## Commands

| Area | Command |
| --- | --- |
| Repository validation | Defined in `.github/workflows/ci.yml` |
| Backend tests (no DB) | `bash mvnw test` or `.\mvnw.cmd test` from `backend/` |
| Backend build and integration tests | `bash mvnw verify` or `.\mvnw.cmd verify` from `backend/`, with PostgreSQL environment variables |
| Frontend tests | `npm run test` from `frontend/` |
| Frontend lint | `npm run lint` from `frontend/` |
| Frontend build | `npm run build` from `frontend/` |
