# Team Workflow

## Team And Ownership

The team has five members and divides work by feature. A feature owner delivers its frontend, backend, API contract, database migrations, tests, and documentation together. There are no separate frontend and backend handoff queues.

For every task, record one owner and one reviewer in `docs/tasks.md` or the linked issue. Actual member assignments remain unassigned until the team provides names and agrees on scope.

One of the five members also coordinates integration for each milestone. This is a rotating responsibility, not a sixth position or an exclusive owner of shared code. The coordinator keeps shared-file changes and migration ordering consistent.

## Before Starting A Feature

1. Link the approved PRD requirements and define observable acceptance criteria.
2. Identify the module, owner, reviewer, and dependencies on other features.
3. Resolve only the PRD questions that block that feature. Do not infer missing fields, permissions, or payment rules.
4. Review the API request/response schemas and security requirements in `contracts/openapi.yaml`.
5. Review entity ownership and relationships in `docs/database_schema.md`; reserve a migration version if needed.
6. Agree on cross-module interfaces with the affected owner before implementation.

Contract and schema changes may be a small prerequisite PR when another feature depends on them. Frontend mocks, when useful, must match the reviewed contract; integration must eventually run against the real backend.

## Shared Changes

| Shared area | Coordination rule |
| --- | --- |
| OpenAPI and shared error/pagination schemas | Feature owner edits; reviewer checks compatibility and affected consumers |
| Database relationships and migrations | Affected owners agree on relationships; coordinator reserves versions and merge order |
| `package.json`, `package-lock.json`, `pom.xml` | Explain the dependency need; coordinate overlapping edits and rerun checks after integration |
| Router, providers, layout, security, configuration | Keep changes small; notify affected owners before changing shared behavior |
| Compose, CI, environment templates, setup scripts | Include repeatable setup instructions and have another member verify |

Do not copy another feature's entity or business logic to avoid a dependency. Use the documented module boundary. Shared folders contain reusable technical code, not a second home for feature logic.

## Branches And Pull Requests

- Use `main` as the integration baseline for this initial milestone. A separate `develop` branch is optional only if the team explicitly adopts it later.
- Use `feature/<short-name>` and `fix/<short-name>` for team branches; agent-created branches use `codex/<short-name>`.
- Prefer one coherent feature increment per PR. A PR may include both applications and its contract/migration changes.
- Keep work in progress as draft PRs. Include requirement IDs, behavior changes, verification, and any dependencies on another PR.
- Sync with current `main` before requesting final review. Avoid simultaneous dependency installation changes on different branches when practical.
- Require one reviewer other than the author and passing relevant CI checks before merging. These are team rules; repository branch protection is not configured by this document.
- Merge prerequisite contracts, schema changes, or shared infrastructure before dependent features. Recheck affected tests after conflict resolution.

## Feature Completion

- Approved acceptance criteria work through the actual frontend and backend.
- Contract and schema match the implementation; migrations work from a clean database and the supported previous schema.
- Behavior tests cover relevant success, validation, authorization, and failure paths.
- Frontend loading, empty, and error states are handled.
- Relevant checks pass; no secrets or local environment files are tracked.
- Setup changes are documented and the task is marked complete.

## Foundation Milestones

| Milestone | Output | Depends on |
| --- | --- | --- |
| 1. Shared conventions | Module layout, local configuration, API rules, migration coordination, team workflow | Existing stack decision |
| 2. Backend and database | Spring Boot, PostgreSQL Compose, Flyway wiring, `/health`, error handling, tests | Milestone 1 |
| 3. Frontend shell | Providers, router, layout, shared API client, real health request, checks | Milestone 1; real integration needs milestone 2 |
| 4. Authentication | Agreed token/account policy and tested login/protected API flow | PRD/auth decisions and milestones 2–3 |
| 5. Team handoff | Application CI, setup guide, module guide, setup verified by another member | Milestones 2–4 |

Milestone 1 defines conventions; it does not create runnable applications. Product feature allocation among the five members is a separate task after the necessary domain decisions are made.
