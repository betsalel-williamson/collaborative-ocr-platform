.PHONY: infra-up infra-down backend-build backend-test client-build client-test kestra-deploy kestra-test format lint

# Root-level orchestration for worktree agents.
# Replace `echo` placeholders with actual Gradle/PNPM/Kestra commands as services are implemented.

infra-up:
	@echo "[stub] docker compose up --build"

infra-down:
	@echo "[stub] docker compose down -v"

backend-build:
	@echo "[stub] (cd services/backend && ./gradlew build)"

backend-test:
	@echo "[stub] (cd services/backend && ./gradlew test)"

client-build:
	@echo "[stub] (cd services/client && pnpm install && pnpm build)"

client-test:
	@echo "[stub] (cd services/client && pnpm test)"

kestra-deploy:
	@echo "[stub] (cd services/kestra && kestra flow deploy flows/)"

kestra-test:
	@echo "[stub] (cd services/kestra && kestra flow executions list)"

format:
	@echo "[stub] run repo-wide formatting"

lint:
	@echo "[stub] run repo-wide linting"

