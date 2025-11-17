# Prototype FoundationDB OCR Review

Work-in-progress mono-repo for a collaborative OCR review MVP leveraging FoundationDB Record Layer, a Vite PWA client, a Java backend, and Kestra-managed OCR workflows.

## Active Work Item

- `./.work-items/ocr-review/user-story.md`
- Sequenced tasks:
  1. `./.work-items/ocr-review/01_infrastructure-baseline.md`
  2. `./.work-items/ocr-review/02_record-layer-schema.md`
  3. `./.work-items/ocr-review/03_kestra-orchestration.md`
  4. `./.work-items/ocr-review/04_image-ocr-pipeline.md`
  5. `./.work-items/ocr-review/05_backend-apis.md`
  6. `./.work-items/ocr-review/06_google-sso.md`
  7. `./.work-items/ocr-review/07_pwa-client.md`
  8. `./.work-items/ocr-review/08_sync-conflict-resolution.md`
  9. `./.work-items/ocr-review/09_permissions-ui.md`
  10. `./.work-items/ocr-review/10_verification-docs.md`

## Workflow for Worktree Agents

1. Create a dedicated worktree per task (example: `git worktree add ../ocr-infra-task main`).
2. Run the root `Makefile` targets (see below) inside the worktree to build/test individual services.
3. Keep commits scoped to a single task document and related implementation changes.
4. Update the corresponding `.work-items/ocr-review/0X_*.md` step with progress notes or refinements as needed.

## Makefile Orchestration

`Makefile` in the repo root orchestrates container builds and local automation via Docker toolchains. Copy `config/.env.example` to `.env` first so shared environment variables are available to Make and Compose.

- `make infra-up` / `make infra-down` — start/stop the full Docker Compose stack.
- `make backend-build`, `make backend-test` — run Gradle build/test inside the official `gradle:8.7-jdk17` container.
- `make client-build`, `make client-test` — install dependencies and run Vite/Vitest via `pnpm` in `node:20-alpine`.
- `make kestra-deploy`, `make kestra-test` — validate and inspect Kestra flows using the `kestra/kestra:4.1.0` CLI.

## Configuration Conventions

- Use `config/` for shared resources and secrets templates.
  - `config/.env.example` – enumerate shared environment variables (copy to `.env` locally).
  - `config/kestra/` – Kestra flow definitions and `secrets.yml` templates.
- Service-specific configuration remains within each project (for example, Gradle properties or PNPM `.env` files) and is invoked via Makefile targets.

## References

- FoundationDB Record Layer Getting Started — [https://foundationdb.github.io/fdb-record-layer/GettingStarted.html](https://foundationdb.github.io/fdb-record-layer/GettingStarted.html)
- FoundationDB Blob Storage Pattern — [https://apple.github.io/foundationdb/blob.html](https://apple.github.io/foundationdb/blob.html)
- PWA Caching Guide — [https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Guides/Caching](https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Guides/Caching)

## Quickstart

1. Copy shared env vars: `cp config/.env.example .env`.
2. Bring up the stack: `make infra-up` (equivalent to `docker compose up --build`).
3. Visit services:
   - Backend API / health check — `http://localhost:8080/healthz`
   - Vite preview (proxied via backend) — `http://localhost:8080/`
   - FoundationDB admin (CLI only) — `docker exec -it ocr-foundationdb fdbcli`
   - Kestra UI — `http://localhost:8081/`
4. Tear everything down: `make infra-down`.

Service-specific targets remain available for isolated development (`make backend-test`, `make client-build`, etc.).

