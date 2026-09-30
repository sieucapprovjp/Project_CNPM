# Frontend

## Status

The frontend stack is selected but the application has not been initialized.

## Stack

- Node.js 22 LTS and npm
- React 19 with TypeScript 6
- Vite 8
- HeroUI 3 and Tailwind CSS 4
- React Router 8
- Axios 1
- TanStack Query 5
- Vitest and React Testing Library
- ESLint and Prettier

Exact dependency versions are locked by `package.json` and `package-lock.json`. Do not add yarn, pnpm, or bun lockfiles.

## Responsibilities

- Present accessible user interfaces.
- Validate input for usability while treating the backend as authoritative.
- Integrate only through documented contracts.
- Handle loading, empty, success, and error states.

## Planned Structure

These are the agreed paths for initialization, not existing application files:

```text
frontend/src/
  app/
    providers/
    router/
  layouts/
  components/
  lib/api/
  features/
    <feature>/
      api/
      components/
      pages/
      types/
  styles/
```

Colocate component and hook tests as `*.test.ts` or `*.test.tsx`. Create folders only as needed. Feature owners expose their route definitions for composition by `app/router`; the application shell and providers remain shared. `components` contains reusable UI, while business-specific UI stays in the feature.

## State And Data

- Server-state approach: TanStack Query
- Local-state approach: React component state; use context for genuinely shared UI state when needed
- Form handling: to be defined
- HTTP client: a configured Axios instance at the API boundary
- Routing: React Router
- Authentication state and token storage: to be defined before implementation

## User Experience Requirements

- Support the target devices defined in the PRD.
- Meet the approved accessibility target.
- Avoid exposing internal error details.
- Preserve clear feedback for asynchronous actions.

## UI Policy

HeroUI is the component library and Tailwind CSS is the styling system. Material UI, Ant Design, Bootstrap, and other component libraries require an accepted architecture decision before adoption.

## Verification

Use Vitest and React Testing Library for frontend behavior. npm scripts for ESLint, Prettier, tests, TypeScript checking, and the Vite production build must run in CI after initialization.
