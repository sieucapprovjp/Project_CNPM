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
| `.github/` | CI and GitHub collaboration templates |

## Documentation Ownership

| Document | Update when |
| --- | --- |
| `prd.md` | Product scope or behavior changes |
| `architecture.md` | System boundaries or component interactions change |
| `tech_stack.md` | A technology is selected, replaced, or upgraded |
| `frontend.md` | Frontend structure or practices change |
| `backend.md` | Backend structure or practices change |
| `api.md` | API-wide conventions change |
| `database_schema.md` | Persistent data structures change |
| `decisions.md` | A significant decision is accepted or superseded |
| `setup.md` | Local setup steps or prerequisites change |

## Dependency Direction

The final module dependency rules will be documented after initialization. Source code must not create undocumented coupling between frontend and backend; shared behavior belongs in contracts, not copied implementation details.
