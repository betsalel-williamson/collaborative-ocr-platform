## Objective

Spin up a reproducible mono-repo environment with Docker Compose that launches FoundationDB, the Java Record Layer backend scaffold, the Vite PWA client, and a Kestra orchestration node, coordinating builds through root-level Makefiles while individual projects retain their Gradle/PNPM tooling.

## Acceptance Criteria

- Compose brings up all services (FoundationDB, backend, client, Kestra) with one `docker compose up` and exits cleanly with `docker compose down`.
- Root `Makefile` exposes targets to build, test, and run each service while delegating to service-specific Gradle/PNPM commands and Kestra CLI interactions.
- Java backend connects to FoundationDB using default cluster file and reports readiness.
- Kestra UI and API become reachable for defining flows, with persistent storage wired to the mono-repo volumes.
- Vite client container serves the app at a stable port proxied through the backend.
- README quickstart snippet documents prerequisites, Makefile targets, service URLs, and commands.
- `config/` directory contains shared templates (`README.md`, `.env.example`, `kestra/secrets.example.yml`) referenced by Makefile targets.

## Test Strategy

- Execute `docker compose up --build` to verify containers build and start without errors.
- Run `make backend-build`, `make client-build`, and `make kestra-deploy` (or equivalent) to confirm orchestration delegates to Gradle, PNPM, and Kestra without conflicts.
- Use `curl` or browser to confirm client, backend, and Kestra health endpoints resolve.
- Run `docker compose down -v` to ensure volumes/network teardown succeeds.
- Verify each agent copies `config/.env.example` to local `.env` before executing related Makefile targets.

