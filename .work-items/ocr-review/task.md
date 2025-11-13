## Objective

Deliver an MVP offline-capable OCR review application backed by FoundationDB Record Layer, accessible through a Node.js/Vite PWA client and a Java service, with Google SSO, per-image sharing controls, and workflow automation for OCR processing.

## Requirements Traceability

- R1: Role-based sharing for images (`private`, `protected`, `public`) — User Story AC 1 & 2.
- R2: Collaborative OCR editing with conflict visibility — User Story AC 3 & 5.
- R3: Offline-first PWA client with background sync — User Story AC 4 & 5.
- R4: Google SSO onboarding — User Story AC 6.
- R5: Automated OCR ingestion pipeline and resilient image blob storage — User Story AC 1 & 4.
- R6: Monorepo orchestration via root Makefiles delegating to project-specific build tools.
- R7: Workflow orchestration for OCR processing using Kestra, integrated with transactional Record Layer storage.

## Task Breakdown (Sequenced, <4h each)

1. **T1 Infrastructure Baseline** — Scaffold Docker Compose stack with FoundationDB, Java backend, Node.js/Vite client, and Kestra orchestrator containers; establish root Makefiles coordinating Gradle/PNPM/Kestra workflows; verify local startup. (R1-R7 foundation)
2. **T2 Domain Schema & Record Layer Setup** — Define protobuf metadata, blob chunk subspaces, indexes, and migrations for images, OCR entries, and sharing policies using Record Layer getting started flow. (R1-R2, R5)
3. **T3 Kestra Workflow Orchestration** — Configure Kestra namespace, flows, and task definitions to trigger OCR jobs, enforce retries, and interact with Record Layer transactions. (R5, R7)
4. **T4 Image Ingestion & OCR Pipeline** — Integrate Google Cloud Vision OCR, handle image uploads, chunk storage per FoundationDB blob guidance, and persist OCR outputs into Record Layer via Kestra-triggered executions. (R1-R3, R5, R7)
5. **T5 Backend Service APIs** — Implement Java service endpoints for auth callbacks, image/OCR CRUD, sharing assignments, and conflict resolution hooks; integrate with Kestra job orchestration and FoundationDB transactions. (R1-R3, R5, R7)
6. **T6 Google SSO Integration** — Configure OAuth2 login, session issuance, and user provisioning pipeline. (R4)
7. **T7 Offline-First PWA Client** — Build Vite frontend with caching strategy per MDN guide, local persistence, and background sync flows. (R2-R3)
8. **T8 Sync & Conflict Resolution** — Implement delta sync, merge strategies, and user-facing conflict notifications between client store and Record Layer. (R2-R3, R5)
9. **T9 Permissions UI & Enforcement** — Surface access controls in UI, ensure backend guards endpoints, add tests. (R1)
10. **T10 End-to-End Verification & Documentation** — Automated tests, TDD audit, runbook covering offline usage, Kestra-driven OCR flows, and recovery steps. (R1-R7)

Each task follows Red → Green → Refactor with tests validating acceptance criteria from the user story.

