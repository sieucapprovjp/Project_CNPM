# API

This document records API-level rules and design notes. The machine-readable source of truth is [`contracts/openapi.yaml`](../contracts/openapi.yaml).

The API uses REST and OpenAPI 3.1. The backend exposes generated API documentation and Swagger UI through springdoc-openapi 2.9.1.

## Base Path

All application endpoints should use the `/api/v1` prefix. Health checks may remain outside the versioned API.

## Format

- Requests and responses use JSON unless an endpoint explicitly documents another media type.
- Property names use `camelCase`.
- Dates and timestamps use ISO 8601 in UTC.
- Resource identifiers are strings at the API boundary.
- Pagination rules will be defined before the first collection endpoint is implemented.

## Authentication

Protected endpoints use JWT bearer authentication through Spring Security. Token issuance, claims, expiration, refresh, revocation, and frontend storage rules must be approved before authentication endpoints are added.

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
