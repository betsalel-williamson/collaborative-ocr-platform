Shared configuration templates live in this directory. Copy values into local `.env` files per service.

## Security Warning

⚠️ **ALL credentials in this directory are DUMMY/EXAMPLE values for LOCAL DEVELOPMENT ONLY.**

**CRITICAL RESTRICTIONS:**
- ✅ **CAN** be used for local development on your personal machine
- ❌ **CANNOT** be used in staging environments
- ❌ **CANNOT** be used in production environments
- ❌ **CANNOT** be used in any non-locally hosted environments (cloud, remote servers, etc.)

**File Types:**
- Files with `.example` suffix contain template values that MUST be replaced with real credentials for staging/production
- Files without `.example` suffix (like `application.yaml`) contain hardcoded dummy credentials marked with `# DUMMY` comments
- **NEVER commit real production or staging credentials to version control**
- Real secret files (like `secrets.yml`, `.env`) are gitignored and should remain local-only

For staging and production, use environment variables or a secrets management system.

## Configuration Files

- `config/.env.example` — shared variables referenced by `docker compose` and the root `Makefile`. Copy to `.env` before running infrastructure commands.
- `config/kestra/application.yaml` — Kestra runtime configuration (H2-backed repository, local storage). **Contains DUMMY credentials** - see file header for warnings.
- `config/kestra/secrets.example.yml` — template for Kestra secrets. Copy to `config/kestra/secrets.yml` (gitignored) and replace all DUMMY values with real credentials.
- `config/foundationdb/fdb.cluster` — cluster file distributed to backend and Kestra containers.
- `config/kestra/` — store Kestra flow definitions (`*.yml`) and secret templates.

## Usage

1. Copy example files to create local configurations: `cp config/.env.example .env`
2. Replace all DUMMY/EXAMPLE values with real credentials
3. Verify that real secret files are in `.gitignore` before committing
4. Each worktree agent should duplicate and customize locally when running tasks

