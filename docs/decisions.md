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

## ADR-003: Establish The Shared Foundation For A Five-Member Feature Team

- Date: 2026-09-26
- Status: Accepted

### Context

The project owner requested implementation of the shared-conventions milestone and confirmed that five members divide work by feature rather than by frontend/backend. The applications remain uninitialized; product questions in the PRD are still open.

### Decision

- Each feature has one owner responsible for both applications, contract, schema changes, tests, and documentation, plus one reviewer. An integration coordinator is selected from the same five members for each milestone.
- Organize both applications by feature, with the technical shared boundaries and planned paths documented in `architecture.md`, `backend.md`, and `frontend.md`. Use `vn.bluemoon` as the backend root package.
- Integrate small reviewed PRs into `main`; coordinate changes to dependencies, shared configuration, contracts, and migrations according to `team_workflow.md`.
- Run applications on the host and PostgreSQL in Compose for local development. Default ports are frontend `5173`, backend `8080`, and PostgreSQL `5432`. `setup.md` defines the environment loading requirements to implement during scaffolding.
- Adopt the API conventions in `api.md`: zero-based optional pagination with a default size of 20 and maximum of 100, an operation-defined sort allowlist, and typed validation details. Paginated collection results contain `items`, `page`, `size`, `totalElements`, and `totalPages`; other successful responses keep their operation-specific shape.
- Reserve sequential Flyway versions centrally before parallel feature work; merge and apply them in order, retaining immutability once shared.

### Consequences

Members can develop complete features within consistent boundaries. The foundation convention documents and shared OpenAPI components are ready for application initialization. They do not approve new product endpoints, database entities, search capabilities, account policies, or authentication-token behavior. Those still require the corresponding PRD decisions.

Actual owner/reviewer assignments are pending. Branch protection, CI enforcement, launchers, and runtime configuration must be implemented in later foundation milestones; this decision does not claim those controls are already active.

## ADR-004: Provision Internal Staff Accounts Through An Administrator

- Date: 2026-09-29
- Status: Accepted

### Context

The source requires account registration while also referring to provided Management Board accounts. The project leader, who confirms product requirements, selected option A: an internal system with an initial administrator who issues staff accounts.

### Options

- A: Internal administrator-issued staff accounts, followed by staff first-use activation/password setup.
- B: Resident self-registration alongside separately provisioned staff accounts, requiring expanded product scope.

### Decision

- Adopt option A. Management Board staff remain the product's users; residents do not receive login accounts.
- Establish an initial account administrator; ADR-006 later specifies its bootstrap mechanism.
- Only the account administrator may issue staff accounts. Ordinary staff cannot issue accounts, and public self-registration is unavailable.
- Staff complete first-use activation/password setup before normal access; the delivery and activation mechanism is still to be specified.
- Interpret the registration requirement as this internal provisioning flow and retain its traceability through US-AUTH-004 and FR-AUTH-004 through FR-AUTH-006.

### Consequences

The provisioning direction is approved, not implemented. At the time of this decision, account fields, login identifier, bootstrap, credential delivery, activation details, business permissions beyond account issuance, and token lifecycle remained open. This decision does not authorize account deletion, locking, password recovery, or granting administrator privileges to additional users. Define those behaviors if needed before implementation. API endpoints and database entities will be specified after the relevant details are agreed; this documentation change adds neither.

Follow-up: ADR-005 resolves activation and delivery as an expiring, single-use link sent by email. The remaining details listed there are still open.

## ADR-005: Activate Staff Accounts Through Emailed Links

- Date: 2026-09-30
- Status: Accepted

### Context

After approving administrator-issued internal accounts, the requirement decision-maker selected activation links instead of temporary passwords and requested account delivery by email.

### Options

- Temporary password, followed by a mandatory first-use password change.
- Expiring, single-use activation link, through which the staff member sets their own password; delivered by email.

### Decision

- Adopt email-delivered activation links for staff accounts issued under ADR-004.
- The administrator supplies the staff recipient's email address. The system sends access instructions and the activation link; it does not generate or email a temporary password for this flow.
- The account awaits activation and cannot log in normally until the staff member successfully sets their password through a valid link.
- The link expires and supports one successful password setup only. Opening it alone does not activate the account or consume the link. Invalid, expired, and consumed links cannot set or change a password.
- The staff email is the unique login identifier for the account.
- Each activation link is valid for 24 hours from issuance.
- An account administrator may send a replacement activation link while the account remains unactivated. Issuing a replacement invalidates every earlier activation link for that account, and the replacement receives its own 24-hour validity period.
- After activation, the staff member logs in with their chosen password.
- The backend owns activation and email dispatch. An email delivery integration is approved; its provider is not yet selected.

### Consequences

This refines ADR-004 and adds FR-AUTH-007 through FR-AUTH-009 for activation-email delivery, email login identity, and replacement links. PRD section 9.2 records acceptance criteria. It is a requirements decision, not an implemented feature; no emails have been sent as part of recording it.

Email is required for delivery and is the unique login identifier. Link lifetime and replacement behavior are decided above. Email canonicalization, password policy, email provider/sender, send-failure feedback and recovery, remaining account fields, detailed business permissions, and login-session token lifecycle remain open. Do not treat email-provider acceptance as proof of inbox delivery. Plan a controlled delivery check alongside activation tests when implementing the feature. API/schema design and mail configuration follow once the relevant details are agreed.

Follow-up: ADR-006 resolves the initial administrator bootstrap mechanism. Other open items in this consequence remain open.

## ADR-006: Bootstrap One Initial Administrator From Runtime Configuration

- Date: 2026-09-30
- Status: Accepted

### Context

Internal account provisioning requires an initial administrator before any administrator exists to issue accounts. The project leader agreed that the system should establish one initial administrator, while credentials must not be hard-coded or committed.

### Options

- Commit a fixed administrator email and password in source code or a Flyway data migration.
- Create the initial administrator from explicit backend runtime configuration only when the account store is empty, then use the normal email activation flow.
- Require a developer or operator to insert the first account directly into PostgreSQL.

### Decision

- Use an explicit, backend-only runtime setting to enable bootstrap and provide the initial administrator email. Bootstrap is disabled by default.
- After the approved account schema exists, application startup checks the account store inside a transaction. When it is empty, the backend creates exactly one pending account with the `ACCOUNT_ADMIN` role and the configured email.
- The backend creates no preset password. After the transaction commits, it sends the normal single-use activation link defined by ADR-005, valid for 24 hours.
- If any account already exists, bootstrap performs no account creation, reset, role change, email change, or automatic token rotation. Keeping the configuration present must therefore be harmless, although operators remove or disable it after initialization.
- Database uniqueness constraints plus a transactional implementation must prevent duplicate bootstrap accounts during concurrent starts.
- The bootstrap is application behavior, not a Flyway data seed, and it exposes no public HTTP endpoint.
- If the initial account remains pending because its link expired or email delivery failed, an explicit operator-only recovery command may rotate its activation token and send a replacement. Recovery is permitted only while no activated account administrator exists. Its exact invocation will be documented with the implementation and deployment configuration.
- Logs may identify whether bootstrap was skipped, created, or failed and may include the target email. They must never expose passwords, raw activation tokens, or mail-provider credentials.

### Implementation Outline

1. Define backend configuration properties for bootstrap enablement and administrator email. Do not use a `VITE_` variable or expose them to the frontend.
2. Add the approved account and activation-token schema through a reserved Flyway migration. Store only a cryptographic hash of each random activation token.
3. In the account feature, implement a transactional bootstrap service invoked after migrations complete. Repositories enforce the empty-store check and uniqueness constraints.
4. Publish an internal event only after the account and token transaction commits. The email adapter builds a frontend activation URL containing the raw token and sends it; the raw value is never logged or persisted.
5. The activation endpoint hashes the presented token, verifies pending status, 24-hour expiry, invalidation and single use, hashes the chosen password, activates the account, and consumes the token atomically.
6. Implement the recovery operation as an operator command rather than an unauthenticated web endpoint. It uses the same replacement-token rules and refuses to run after any administrator becomes active.
7. Verify empty and nonempty stores, concurrent startup, repeated startup, successful activation, expired/used token rejection, email failure, recovery, and secret-free logs.

### Consequences

The repository contains no default administrator password, and the initial administrator follows the same activation security rules as later staff. Implementation depends on approval of the account schema, email canonicalization, password policy, mail provider/failure behavior, and activation API. Planned runtime property names belong in setup documentation and `.env.example` only when the code consumes them.
