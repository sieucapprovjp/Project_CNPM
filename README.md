# Bluemoon

Bluemoon is a software project currently in the discovery and planning phase. This repository contains product documentation, architecture decisions, API contracts, backend code, and frontend code.

## Status

- Product scope: not finalized
- Technology stack: finalized
- Backend: foundation initialized (health, security guard, error handling, PostgreSQL/Flyway configuration)
- Frontend: not initialized

## Repository

```text
bluemoon/
|-- docs/          Project and engineering documentation
|-- contracts/     Machine-readable API contracts
|-- backend/       Backend application
|-- frontend/      Frontend application
`-- .github/       CI and collaboration templates
```

See [`docs/project_map.md`](docs/project_map.md) for the complete map.

## Start Here

1. Define the problem, users, scope, and success criteria in [`docs/prd.md`](docs/prd.md).
2. Review the selected technology stack in [`docs/tech_stack.md`](docs/tech_stack.md).
3. Describe the initial system design in [`docs/architecture.md`](docs/architecture.md).
4. Follow [`docs/setup.md`](docs/setup.md) when initializing the applications.
5. Track implementation work in [`docs/tasks.md`](docs/tasks.md).

## Documentation Index

- [Product requirements](docs/prd.md)
- [Use case model](docs/use_cases.md)
- [Architecture](docs/architecture.md)
- [Project map](docs/project_map.md)
- [Technology stack](docs/tech_stack.md)
- [Frontend](docs/frontend.md)
- [Backend](docs/backend.md)
- [API](docs/api.md)
- [Database schema](docs/database_schema.md)
- [Conventions](docs/conventions.md)
- [Team workflow](docs/team_workflow.md)
- [Error handling](docs/error_handling.md)
- [Testing strategy](docs/testing_strategy.md)
- [Tasks](docs/tasks.md)
- [Decisions](docs/decisions.md)
- [Setup](docs/setup.md)

## Development

For backend development, install JDK 17 and Docker Compose, then follow [`docs/setup.md`](docs/setup.md). Copy `.env.example` to an untracked `.env`, configure the local database password, run `docker compose up -d --wait`, and start the backend with `powershell -File scripts/backend.ps1 run` (Windows) or `bash scripts/backend.sh run` (macOS/Linux).

`GET http://localhost:8080/health` returns `{"status":"ok"}`. Product APIs remain closed until authentication and feature permissions are implemented. The frontend has not been initialized.
