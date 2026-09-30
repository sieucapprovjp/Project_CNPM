# Setup

## Current State

The backend and PostgreSQL Compose configuration are initialized. The frontend remains planned; its commands below are not executable yet.

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
| Maven | 3.9.11, downloaded by Maven Wrapper 3.3.4 | No global Maven installation needed |
| Node.js | 22 LTS | For frontend development |
| npm | Supplied with the approved Node.js release | For frontend dependencies and scripts |
| Docker with Compose v2 | Current supported version | For local services |
| PostgreSQL | 18 | Through Docker Compose |

## Environment Variables

The root `.env.example` defines the local environment. Copy it to an untracked root `.env` and fill the two password values with the same local-only value. Use plain, unquoted `KEY=value` lines; launch scripts do not interpret shell substitutions, quotes, or multiline values. A locally generated alphanumeric password avoids Compose interpolation differences. Update both this document and the template whenever configuration changes.

| Variable | Local value / rule | Consumer |
| --- | --- | --- |
| `SPRING_PROFILES_ACTIVE` | `local` | Backend profile |
| `SERVER_PORT` | `8080`; valid TCP port | Backend HTTP server |
| `FRONTEND_ORIGIN` | `http://localhost:5173`; exact origin, no trailing slash | Backend CORS allowlist |
| `FRONTEND_PORT` | `5173`; valid TCP port | Vite development server |
| `VITE_API_ORIGIN` | `http://localhost:8080`; origin only, no `/api/v1` or trailing slash | Frontend Axios base URL |
| `POSTGRES_PORT` | `5432`; host port for container port `5432` | Compose |
| `POSTGRES_DB` | `bluemoon` | Compose |
| `POSTGRES_USER` | `bluemoon`; local development user only | Compose |
| `POSTGRES_PASSWORD` | Required, nonempty, local-only value | Compose |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://localhost:5432/bluemoon` | Backend running on host |
| `SPRING_DATASOURCE_USERNAME` | Match `POSTGRES_USER` locally | Backend |
| `SPRING_DATASOURCE_PASSWORD` | Match `POSTGRES_PASSWORD` locally | Backend |

### Loading And Port Rules

- The default local topology runs both applications on the host and PostgreSQL in Compose, with a persistent volume and database health check. Each member uses their own local database.
- Compose reads the root `.env` for interpolation. Spring Boot does not automatically load this file: use `scripts/backend.ps1` or `scripts/backend.sh`, which load only the backend variables and preserve existing process environment overrides. When launching directly from an IDE or Maven, set these values in the process environment yourself.
- Frontend initialization must configure Vite's `envDir` to the repository root and read `FRONTEND_PORT` in its server configuration. Retain the default `VITE_` client exposure prefix; backend/database secrets must not be exposed to browser code.
- Use a strict frontend port so a port collision fails clearly instead of silently changing the CORS origin. If changing a port, update the corresponding origin or JDBC URL as well.
- The shared Axios base URL is an origin. Application calls use `/api/v1/...`; the health call uses `/health`.
- Production configuration comes from Render environment settings; local `.env` files are not deployed. JWT variables will be added after the authentication policy is agreed.

Compose and backend launchers exist. Frontend port handling and Vite configuration remain requirements for the frontend milestone.

## First Backend Run

1. Install JDK 17 and set `JAVA_HOME` to its installation directory. Confirm `java -version` reports Java 17. Start Docker Desktop (Linux containers on Windows) or the Docker daemon.
2. Copy `.env.example` to `.env` at the repository root. Set both database password values to the same local-only value.
3. From the repository root, run `docker compose up -d --wait`. This starts only PostgreSQL and waits for its health check.
4. Run `powershell -File scripts/backend.ps1 run` on Windows, or `bash scripts/backend.sh run` on macOS/Linux.
5. Check `http://localhost:8080/health`. Expected response: `{"status":"ok"}`.
6. With `SPRING_PROFILES_ACTIVE=local`, open `http://localhost:8080/swagger-ui/index.html`. Generated docs are at `/v3/api-docs`; the reviewed source of truth remains `contracts/openapi.yaml`.

Stop the backend with Ctrl+C. `docker compose down` stops local PostgreSQL and preserves its named volume. Do not remove that volume unless intentionally discarding local data.

The first Maven run downloads Maven and dependencies and requires network access. The `local` profile exposes API documentation; omit it in deployed environments.

## Application Commands

| Task | Command |
| --- | --- |
| Run backend on macOS/Linux | `bash scripts/backend.sh run` from repository root |
| Run backend on Windows | `powershell -File scripts/backend.ps1 run` from repository root |
| Test backend without DB on macOS/Linux | `bash scripts/backend.sh test` |
| Test backend without DB on Windows | `powershell -File scripts/backend.ps1 test` |
| Verify backend with PostgreSQL on macOS/Linux | `bash scripts/backend.sh verify` |
| Verify backend with PostgreSQL on Windows | `powershell -File scripts/backend.ps1 verify` |
| Direct Maven invocation | From `backend/`: `bash mvnw verify` or `.\mvnw.cmd verify`; supply environment variables first |
| Install frontend dependencies | `npm ci` from `frontend/` |
| Run frontend | `npm run dev` from `frontend/` |
| Test frontend | `npm run test` from `frontend/` |
| Lint frontend | `npm run lint` from `frontend/` |
| Build frontend | `npm run build` from `frontend/` |
| Start local services | `docker compose up -d --wait` from repository root |

## Local Services

Compose runs PostgreSQL 18, bound only to `127.0.0.1`, with a health check and named volume mounted at `/var/lib/postgresql`, as required by the [official PostgreSQL 18 image](https://hub.docker.com/_/postgres). Database credentials and initialization variables apply when the volume is first created; editing `.env` does not change an existing database user's password.

Do not install or use yarn, pnpm, or bun. Frontend dependency changes must update both `package.json` and `package-lock.json`.

## Deployment

Render is the deployment platform:

- Deploy `frontend/` as a Render Static Site.
- Deploy `backend/` as a Render Web Service using Java 17.
- Use Render PostgreSQL for production data.
- Configure production secrets in Render, never in tracked files.

Backend build: `bash mvnw --batch-mode verify` in `backend/` (requires a configured test database), or `bash mvnw --batch-mode package` to package after the database-independent tests. Artifact: `backend/target/bluemoon-backend-0.1.0-SNAPSHOT.jar`. Start with `java -jar target/bluemoon-backend-0.1.0-SNAPSHOT.jar` from `backend/`, supplying the documented database/CORS variables and `SERVER_PORT` through Render. Do not set the `local` profile there. Render service provisioning and frontend deployment remain future work.

## Troubleshooting

- Docker engine connection errors: start Docker Desktop/the daemon and verify `docker version` reports a server before running Compose.
- Missing environment variable: use a root launcher, or set the corresponding IDE/process environment variable. Copying `.env` alone does not configure direct Maven runs.
- Database authentication failure after editing `.env`: existing volume credentials are unchanged; restore the matching local values or intentionally change the database user's password.
- A port conflict: update the service port and its corresponding origin/JDBC URL together, as listed above.
- Flyway reports no migrations: expected at this foundation stage; the domain schema is not approved yet. Do not create a dummy table to silence the warning.
- Flyway reports PostgreSQL 18 is newer than its tested support range: this is the version managed by the approved Spring Boot release. The foundation integration test passes, but the first domain migration must include a compatibility review and migration tests; see `docs/backend.md`.
