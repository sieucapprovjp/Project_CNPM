# Conventions

## General

- Follow `.editorconfig` for whitespace and line endings.
- Prefer clear, domain-specific names over abbreviations.
- Keep changes focused; avoid unrelated refactoring.
- Add comments only when intent is not evident from the code.
- Do not commit generated output unless the selected tool requires it.

## Branches

- `main`: stable branch
- `develop`: integration branch when the team needs it
- `feature/<short-name>`: feature work
- `fix/<short-name>`: bug fixes

## Commits

Use concise conventional commit messages:

```text
feat: add account registration
fix: reject expired reset tokens
docs: define API error format
test: cover duplicate account creation
```

## Pull Requests

- Explain the problem and solution.
- Link the requirement or issue.
- Include verification steps.
- Call out contract, schema, configuration, or security changes.
- Keep the pull request small enough to review reliably.

## Language-Specific Rules

### Frontend

- Use TypeScript for application code.
- Use Node.js 22 LTS and npm only; do not use yarn, pnpm, or bun.
- Use ESLint for static analysis and Prettier for formatting.
- Keep `package.json` and `package-lock.json` synchronized for every dependency change.
- Use HeroUI v3 and Tailwind CSS v4. Another component library requires an accepted architecture decision.
- Keep API calls behind shared or feature-level API modules rather than calling Axios directly from presentation components.
- Use TanStack Query for remote server state; do not duplicate it into local state without a concrete need.

### Backend

- Target Java 17 and build with the Maven Wrapper; do not use newer Java language features.
- Keep Spring Boot on 3.5.16 unless an accepted architecture decision changes it; do not independently upgrade to 4.x.
- Use Spring Validation at request boundaries and enforce business invariants in services.
- Do not return JPA entities from controllers.
- Use Flyway for every database schema change.

### Version Changes

- Major framework upgrades require an accepted architecture decision.
- Do not independently upgrade Java, Spring Boot, React, HeroUI, or PostgreSQL.
- Patch and minor updates must preserve compatibility and update the relevant lockfile or build manifest.

Automated tooling should enforce these rules when practical. Detailed configurations will be added during application initialization.
