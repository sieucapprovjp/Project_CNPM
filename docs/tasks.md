# Tasks

This file tracks repository-level work until the team adopts an issue tracker. Keep tasks small and link them to PRD requirements where possible.

## Backlog

- [ ] Assign the five members to feature tasks, reviewers, and a milestone integration coordinator using `team_workflow.md`.
- [ ] Agree on the first-release scope and acceptance criteria.
- [ ] Initialize the frontend application.
- [ ] Add frontend application checks to CI after frontend initialization.
- [ ] Verify Compose container startup on a Docker-enabled machine (configuration validation passed; the current host's Docker engine could not start).
- [ ] Check the Boot-managed Flyway version's PostgreSQL 18 compatibility when implementing the first domain migration; startup/history initialization currently pass with a compatibility warning.
- [ ] Define the first API resources in `contracts/openapi.yaml`.
- [ ] Define the initial database entities.
- [ ] Specify remaining account fields, email canonicalization, password policy, detailed business permissions, and login-session token lifecycle before implementing authentication (ADR-004 through ADR-006; US-AUTH-004).
- [ ] Select the email provider/sender and define email-send failure feedback and recovery, local testing, and deployment configuration for staff activation (FR-AUTH-007).
- [ ] Complete the remaining policy-dependent acceptance criteria in PRD sections 9.2–9.3, then specify the account provisioning/email activation API contract and database model (FR-AUTH-004 through FR-AUTH-009).
- [ ] Implement the idempotent, concurrency-safe initial administrator bootstrap after the account schema and email delivery are approved: backend-only enable/email settings, empty-store guard, pending `ACCOUNT_ADMIN`, post-commit activation email, safe logging, and explicit pending-admin recovery (ADR-006; FR-AUTH-006).
- [ ] Implement and verify administrator-issued staff accounts, activation email, password setup, and subsequent login after the outstanding decisions are resolved; cover invalid/expired/reused links, concurrent activation, pending-account access denial, and email failures (PRD section 9.2).
- [ ] Verify the completed foundation setup on another team member's machine.

## In Progress

- [ ] Review the initial repository and documentation structure.

## Done

- [x] Approve an explicit, configuration-driven initial administrator bootstrap that only runs against an empty account store and uses email activation without a preset password (ADR-006).
- [x] Approve expiring, single-use staff activation links delivered by email, with staff choosing their own password; record flow and acceptance criteria (ADR-005). Implementation and remaining policies stay in the backlog.
- [x] Approve email as the unique login identifier, a 24-hour activation-link lifetime, and administrator-issued replacement links that invalidate all prior links (ADR-005).
- [x] Approve internal account provisioning: initial administrator, administrator-issued staff accounts, and staff first-use activation/password setup; exclude public/resident registration (ADR-004). Implementation details remain in the backlog.
- [x] Create the initial project documentation scaffold.
- [x] Select the frontend, backend, database, API, test, and tooling stack.
- [x] Record the stack decision in `decisions.md`.
- [x] Finalize framework versions, npm usage, authentication libraries, and Render deployment.
- [x] Define the product problem, target users, and draft requirements in `prd.md`.
- [x] Create the v1.0 use case model with deferred v2.0 scope.
- [x] Define foundation milestone 1: feature-oriented structure, local configuration, shared API conventions, migration coordination, and the five-member team workflow (ADR-003).
- [x] Initialize the Java 17 / Spring Boot backend with Maven Wrapper, health endpoint, shared errors, request IDs, CORS, and default-deny security.
- [x] Add PostgreSQL 18 Compose configuration, Flyway wiring, and schema validation without unapproved domain tables.
- [x] Add backend verification to CI and document Windows/macOS/Linux local commands.
- [x] Verify milestone 2: Maven `verify` passed on Java 17 with 12 web tests and 1 integration test against a temporary PostgreSQL 18.6 instance; executable JAR built.
