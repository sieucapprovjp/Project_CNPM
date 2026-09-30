# Database Schema

## Status

PostgreSQL 18 is configured through Compose and the backend. The application schema has not been defined. Flyway is enabled with an empty migration directory; no domain tables or migration versions have been created. Its internal schema-history table may be initialized on startup.

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

Flyway migrations are immutable after they have been shared. New changes require a new migration; direct manual schema changes are not part of the development workflow.

- Store migrations in `backend/src/main/resources/db/migration/` as `V<number>__<snake_case_description>.sql`, starting at `V1`.
- Before creating a migration, the integration coordinator reserves a unique increasing number in the ledger below through a small shared update. Do not independently select the next number on separate branches.
- Merge and apply migrations in increasing order. A later migration waits for earlier reservations to be merged or explicitly abandoned before it is applied in a shared environment. Do not reuse abandoned versions.
- Do not enable out-of-order execution to hide coordination problems. Never renumber or edit a migration after it has been shared; use a new corrective migration.
- The feature owner updates the entity documentation and coordinates changes to another feature's data with that owner.
- Backend initialization must configure schema validation rather than Hibernate schema creation/update. Flyway owns schema changes. Do not create a dummy domain table just to initialize Flyway.
- Verify migrations on a clean database and against the prior supported schema. A migration's PR documents dependencies and any data conversion.

### Migration Reservation Ledger

No versions are reserved yet. The coordinator adds rows before migration work starts.

| Version | Feature/task | Owner | State (reserved / merged / abandoned) |
| --- | --- | --- | --- |
