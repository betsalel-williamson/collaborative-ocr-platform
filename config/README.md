Shared configuration templates live in this directory. Copy values into local `.env` files per service.

- `config/.env.example` (create locally) — mirrors the variables documented in the repo root `README.md`.
- `config/kestra/` — store Kestra flow definitions (`*.yml`) and `secrets.example.yml`.

These files remain templates; do not commit real credentials. Each worktree agent should duplicate and customize locally when running tasks.

