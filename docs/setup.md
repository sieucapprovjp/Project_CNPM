# Setup

## Current State

The stack is selected, but the frontend and backend have not been initialized. Commands below describe the intended setup and become executable after application scaffolding is added.

## Repository Setup

1. Clone the repository.
2. Create a local environment file from `.env.example` if needed.
3. Do not place real secrets in tracked files.
4. Review `docs/prd.md`, `docs/tech_stack.md`, and `docs/tasks.md` before starting implementation.

## Prerequisites

| Tool | Version | Required |
| --- | --- | --- |
| Git | Current supported version | Yes |
| JDK | 17 | For backend development |
| Maven | Provided by Maven Wrapper | After backend initialization |
| Node.js | 22 LTS | For frontend development |
| npm | Supplied with the approved Node.js release | For frontend dependencies and scripts |
| Docker with Compose v2 | Current supported version | For local services |
| PostgreSQL | 18 | Through Docker Compose |

## Environment Variables

Variables and validation rules are documented in `.env.example`. Update both files whenever configuration changes.

## Application Commands

| Task | Command |
| --- | --- |
| Run backend on macOS/Linux | `./mvnw spring-boot:run` |
| Run backend on Windows | `mvnw.cmd spring-boot:run` |
| Test backend on macOS/Linux | `./mvnw test` |
| Test backend on Windows | `mvnw.cmd test` |
| Install frontend dependencies | `npm ci` from `frontend/` |
| Run frontend | `npm run dev` from `frontend/` |
| Test frontend | `npm run test` from `frontend/` |
| Lint frontend | `npm run lint` from `frontend/` |
| Build frontend | `npm run build` from `frontend/` |
| Start local services | `docker compose up -d` after `compose.yaml` is added |

## Planned Local Services

Docker Compose will provide PostgreSQL 18 for local development. Add the port, health check, volume, and safe development credentials when `compose.yaml` is created.

Do not install or use yarn, pnpm, or bun. Frontend dependency changes must update both `package.json` and `package-lock.json`.

## Deployment

Render is the deployment platform:

- Deploy `frontend/` as a Render Static Site.
- Deploy `backend/` as a Render Web Service using Java 17.
- Use Render PostgreSQL for production data.
- Configure production secrets in Render, never in tracked files.

Build and start commands will be documented after the applications are initialized.

## Troubleshooting

Add repeatable solutions here when the team encounters setup problems. Include the observed error, root cause, and verified fix.
