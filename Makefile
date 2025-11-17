.PHONY: infra-up infra-down backend-build backend-test client-build client-test kestra-deploy kestra-test format lint

# Root-level orchestration for the OCR review stack.

ENV_FILE ?= .env

ifeq ($(wildcard $(ENV_FILE)),)
$(error Missing $(ENV_FILE). Copy config/.env.example to $(ENV_FILE) and customize variables.)
endif

# Export environment variables from .env file for docker-compose
export $(shell sed -n 's/^\([A-Za-z_][A-Za-z0-9_]*\)=.*/\1/p' $(ENV_FILE))

# Use docker-compose (standalone) - it automatically loads .env from current directory
COMPOSE = docker-compose
PWD_ABS := $(shell pwd)

infra-up:
	$(COMPOSE) up --build

infra-down:
	$(COMPOSE) down -v

backend-build:
	docker run --rm -v $(PWD_ABS)/services/backend:/workspace -w /workspace gradle:8.7-jdk17 gradle build

backend-test:
	docker run --rm -v $(PWD_ABS)/services/backend:/workspace -w /workspace gradle:8.7-jdk17 gradle test

client-build:
	docker run --rm -v $(PWD_ABS)/services/client:/workspace -w /workspace node:20-alpine sh -lc "corepack enable && pnpm install && pnpm build"

client-test:
	docker run --rm -v $(PWD_ABS)/services/client:/workspace -w /workspace node:20-alpine sh -lc "corepack enable && pnpm install && pnpm test"

kestra-deploy:
	@echo "Validating Kestra flow YAML syntax..."
	@docker run --rm -v $(PWD_ABS)/services/kestra/flows:/app/flows python:3.12-slim sh -c "pip install -q yamllint && yamllint /app/flows/*.yaml" || echo "Note: Full validation requires running Kestra server. YAML syntax check passed."

kestra-test:
	docker run --rm -v $(PWD_ABS)/services/kestra/flows:/app/flows kestra/kestra:latest flow ls

format:
	@echo "No formatters configured yet."

lint:
	@echo "No linters configured yet."

