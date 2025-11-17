# Security Guidelines

## Dummy Credentials Policy

All credentials in this repository are **DUMMY/EXAMPLE values** for **LOCAL DEVELOPMENT ONLY**.

⚠️ **CRITICAL**: These dummy credentials:
- ✅ **CAN** be used for local development on your machine
- ❌ **CANNOT** be used in staging environments
- ❌ **CANNOT** be used in any non-locally hosted environments
- ❌ **CANNOT** be used in production

They are clearly marked to prevent accidental use in any non-local environment.

### How to Identify Dummy Credentials

1. **Files with `.example` suffix**: These are templates that MUST be copied and customized
   - `config/.env.example`
   - `config/kestra/secrets.example.yml`

2. **Files with inline `# DUMMY` comments**: Hardcoded dummy values marked with comments
   - `config/kestra/application.yaml` - Contains `# DUMMY CREDENTIALS` comments

3. **Security warnings in file headers**: Files containing credentials have explicit warnings at the top

### Gitignore Protection

The following patterns are gitignored to prevent committing real secrets:

- `**/secrets.yml` and `**/secrets.yaml`
- `**/*secret*.yml` and `**/*secret*.yaml`
- `.env`, `.env.local`, `.env.*.local`, `*.env`
- `config/kestra/secrets.yml` (explicitly ignored)

### Before Committing

✅ **DO:**
- Commit `.example` template files
- Commit files with `# DUMMY` comments (they're clearly marked)
- Verify `.gitignore` excludes real secret files

❌ **DON'T:**
- Commit files named `secrets.yml` (without `.example`)
- Commit `.env` files
- Remove `# DUMMY` comments from configuration files
- Use dummy credentials in production

### Staging and Production Deployment

**IMPORTANT**: Dummy credentials are **ONLY** for local development. For staging and production:

1. **Staging environments**: Use environment variables or a secrets management system (e.g., HashiCorp Vault, AWS Secrets Manager). Do NOT use dummy credentials.
2. **Production environments**: Use environment variables or a secrets management system. Do NOT use dummy credentials.
3. **Non-locally hosted environments**: All remote environments (cloud, staging servers, etc.) must use real credentials from secure storage.
4. Mount real `secrets.yml` files from secure storage (not from git)
5. Replace all `# DUMMY` values in configuration files
6. Never commit production or staging credentials to version control

### Environment Classification

- **Local Development**: Your personal machine running Docker locally ✅ (dummy credentials OK)
- **Staging**: Any shared or remote staging environment ❌ (dummy credentials NOT allowed)
- **Production**: Production servers ❌ (dummy credentials NOT allowed)
- **Cloud/Remote**: Any non-localhost environment ❌ (dummy credentials NOT allowed)

## Security Checklist

- [x] All dummy credentials are clearly marked with `# DUMMY` or in `.example` files
- [x] `.gitignore` excludes all secret file patterns
- [x] Configuration files contain security warnings
- [x] Docker Compose comments warn about example secrets usage

