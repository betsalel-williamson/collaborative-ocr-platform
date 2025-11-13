## Objective

Expose authenticated endpoints for auth callbacks, image/OCR CRUD, sharing management, and synchronization hooks leveraging FoundationDB transactions, Record Layer queries, and Kestra workflow coordination.

## Acceptance Criteria

- REST or gRPC API surface documented with contracts covering upload orchestration, status polling, Kestra job triggers, OCR revision submission, and sharing updates.
- Each write path wraps logic in `FDBDatabase.run` with retry-safe operations chained to the ingestion/vision pipeline and Kestra flow triggers.
- Role and scope checks enforce `private`, `protected`, and `public` access rules before returning image or OCR payloads.
- Conflict detection surfaces revision metadata (timestamps, authors, vector clocks) for clients to reconcile.

## Test Strategy

- Unit tests exercise service methods with mocked request contexts and in-memory FDB test harness where possible.
- Integration tests run against dockerized FoundationDB and Kestra verifying transactional semantics and job dispatch.
- API contract tests (e.g., using REST Assured) confirm expected status codes and payloads for success and failure paths.


