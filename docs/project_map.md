# Project Map

## Root

| Path | Purpose |
| --- | --- |
| `AGENTS.md` | Instructions for automated coding agents |
| `README.md` | Project entry point and documentation index |
| `.editorconfig` | Shared editor formatting defaults |
| `.env.example` | Safe environment-variable template |
| `docs/` | Product and engineering documentation |
| `contracts/` | Machine-readable service contracts |
| `backend/` | Backend source and tests |
| `frontend/` | Frontend source and tests |
| `compose.yaml` | Local PostgreSQL 18 service and persistent volume |
| `scripts/` | Local backend launch, test, and verification commands |
| `.github/` | CI and GitHub collaboration templates |

## Documentation Ownership

| Document | Update when |
| --- | --- |
| `prd.md` | Product scope or behavior changes |
| `use_cases.md` and `use_case_diagram.puml` | Actors, use cases, or release scope changes |
| `architecture.md` | System boundaries or component interactions change |
| `tech_stack.md` | A technology is selected, replaced, or upgraded |
| `frontend.md` | Frontend structure or practices change |
| `backend.md` | Backend structure or practices change |
| `api.md` | API-wide conventions change |
| `database_schema.md` | Persistent data structures change |
| `decisions.md` | A significant decision is accepted or superseded |
| `setup.md` | Local setup steps or prerequisites change |
| `team_workflow.md` | Feature ownership, shared-file coordination, or review workflow changes |

## Dependency Direction

The module boundaries are defined in `architecture.md`, with paths in `frontend.md` and `backend.md`. Frontend and backend communicate through the OpenAPI contract. Backend cross-feature calls go through documented services; frontend features do not import each other's internals. The backend foundation exists; frontend initialization is pending.
