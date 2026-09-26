# Agent Guidelines

## Purpose

These instructions apply to automated coding agents working in this repository.

## Source Of Truth

- Product behavior: `docs/prd.md`
- System boundaries: `docs/architecture.md`
- API contract: `contracts/openapi.yaml`
- Data model: `docs/database_schema.md`
- Active work: `docs/tasks.md`
- Accepted decisions: `docs/decisions.md`

If documents conflict, stop and report the conflict instead of guessing.

## Working Rules

- Read the relevant documentation before changing code.
- Make the smallest change that completely solves the task.
- Keep backend and frontend responsibilities within their documented boundaries.
- Update contracts and documentation when behavior changes.
- Never commit credentials, tokens, private keys, or local `.env` files.
- Do not modify unrelated user changes.
- Do not add dependencies without a clear need.
- Use npm only for the frontend; do not use yarn, pnpm, or bun.
- Do not introduce another UI component library without an accepted architecture decision.
- Do not independently upgrade Java, Spring Boot, React, HeroUI, or PostgreSQL.
- Do not use Java language features newer than Java 17.
- Implement every database schema change through a Flyway migration.
- Add or update tests for behavior changes.
- Run the relevant checks before reporting completion.

## Project State

The final technology stack and version policy are recorded in `docs/tech_stack.md`, but the applications are not initialized. Frontend dependency versions are locked by `package.json` and `package-lock.json` after initialization. Major framework upgrades require an accepted architecture decision.

## Completion Checklist

- Implementation matches the approved requirement.
- API changes are reflected in `contracts/openapi.yaml`.
- Schema changes are reflected in `docs/database_schema.md`.
- Relevant tests pass.
- Setup instructions remain accurate.
- `docs/tasks.md` reflects completed work when applicable.
