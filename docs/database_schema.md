# Database Schema

## Status

PostgreSQL 18 is the database. The application schema has not been defined.

## Conventions

- Every persistent entity must have a documented identifier.
- Nullability, uniqueness, defaults, and relationships must be explicit.
- Every schema change must use a Flyway migration.
- Destructive migrations require a rollback or data migration plan.

## Entities

| Entity | Purpose | Owner |
| --- | --- | --- |
| To be defined | To be defined | To be defined |

## Entity Template

### Entity Name

| Field | Type | Constraints | Description |
| --- | --- | --- | --- |
| `id` | To be defined | Primary key | Stable identifier |

Relationships: to be defined.

Indexes: to be defined from actual query patterns.

## Migration Policy

Flyway migrations are immutable after they have been shared. New changes require a new migration; direct manual schema changes are not part of the development workflow. The naming convention and deployment sequence will be documented when the backend is initialized.
