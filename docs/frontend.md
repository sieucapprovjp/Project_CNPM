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

Use feature-oriented modules once product features are known. Keep application bootstrapping, routing, shared UI, API access, and feature code separate. Final paths will be documented after initialization.

## State And Data

- Server-state approach: TanStack Query
- Local-state approach: to be defined
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
