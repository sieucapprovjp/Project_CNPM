# API

This document records API-level rules and design notes. The machine-readable source of truth is [`contracts/openapi.yaml`](../contracts/openapi.yaml).

The API uses REST and OpenAPI 3.1. The backend exposes generated API documentation and Swagger UI through springdoc-openapi 2.9.1.

## Base Path

All application endpoints should use the `/api/v1` prefix. Health checks may remain outside the versioned API.

## Format

- Requests and responses use JSON unless an endpoint explicitly documents another media type.
- Property names use `camelCase`.
- Calendar dates use ISO 8601 `YYYY-MM-DD` without a timezone; timestamps represent instants in UTC with a `Z` suffix.
- Resource identifiers are strings at the API boundary.

## Collection Conventions

These conventions apply when an approved endpoint needs pagination; they do not add search/filter features to the PRD.

- `page` is zero-based, defaults to `0`, and must be nonnegative.
- `size` defaults to `20` and must be between `1` and `100`.
- An optional `sort` uses `field,asc` or `field,desc`. Initially accept one sort field per request; each endpoint defines its allowed fields and default order.
- Reject unsupported sorting fields or directions and out-of-range parameters with `422`. Malformed parameter types return `400`.
- Define stable ordering with an agreed unique tie-breaker for each paginated resource. Never pass unchecked client field names into a database query.
- Paginated results return `{ items, page, size, totalElements, totalPages }`. `page` and `size` echo the effective request values. `totalPages` is zero when `totalElements` is zero; a page beyond the last page returns an empty `items` array with the actual totals.
- Each operation must define the concrete `items` schema and reference the reusable parameters and `PageMetadata` in OpenAPI. Non-paginated results retain their operation-specific shape.
- Search/filter fields, defaults, and permissions remain per-feature decisions backed by the PRD.

## Validation

The backend is authoritative. Frontend validation improves feedback and should match the contract. Invalid JSON, parameter type conversion, or date syntax returns `400`; validly parsed input violating constraints returns `422`; uniqueness or current-state conflicts return `409`.

Validation errors use `VALIDATION_FAILED` with typed details as documented in [`error_handling.md`](error_handling.md). Do not expose persistence entities or database exception text.

## Authentication

Protected endpoints will use JWT bearer authentication through Spring Security. The current foundation exposes only `GET /health` by default and denies other requests (`401` for anonymous requests, `403` for authenticated callers without access). No token issuance or verification exists yet. Token issuance, claims, expiration, refresh, revocation, and frontend storage rules must be approved before authentication endpoints are added.

The `local` profile additionally exposes Swagger UI and generated API docs. They are disabled by default in other environments. `/health` reports process liveness; database connectivity is verified at startup and in integration tests rather than on every health request.

OpenAPI operations must declare their security requirements explicitly. Public endpoints must not inherit protection accidentally.

## Responses

Successful responses return the resource or result directly unless a shared envelope is approved in [`decisions.md`](decisions.md).

Errors follow the structure defined in [`error_handling.md`](error_handling.md).

## Change Process

1. Update `contracts/openapi.yaml`.
2. Review compatibility and security implications.
3. Implement backend behavior.
4. Update frontend integration.
5. Add contract and integration tests.

Breaking changes require a new API version or an accepted architecture decision.
