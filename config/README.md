Shared configuration templates live in this directory. Copy values into local `.env` files per service.

- `config/.env.example` — shared variables referenced by `docker compose` and the root `Makefile`. Copy to `.env` before running infrastructure commands.
- `config/kestra/application.yaml` — Kestra runtime configuration (H2-backed repository, local storage).
- `config/kestra/secrets.example.yml` — template for Kestra secrets. Copy to `config/kestra/secrets.yml` and fill in credentials locally.
- `config/foundationdb/fdb.cluster` — cluster file distributed to backend and Kestra containers.
- `config/kestra/` — store Kestra flow definitions (`*.yml`) and secret templates.

These files remain templates; do not commit real credentials. Each worktree agent should duplicate and customize locally when running tasks.

