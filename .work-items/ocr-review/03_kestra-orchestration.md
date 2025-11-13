## Objective

Introduce Kestra as the workflow engine orchestrating OCR tasks, ensuring flows interact safely with FoundationDB Record Layer and provide observability for ingest pipelines.

## Acceptance Criteria

- Kestra namespace configured with secrets for Google Cloud Vision and FoundationDB access.
- Flows defined for image upload processing, OCR invocation, and post-processing callbacks with retry, timeout, and compensation strategies.
- Kestra tasks interact with Record Layer through transactional APIs, emitting events for status changes consumable by the backend.
- Makefile targets deploy and update Kestra flows without manual UI steps in development.

## Test Strategy

- Unit tests for Kestra task plugins using mocked dependencies to validate Record Layer transactions.
- Integration test executes flows end-to-end against local containers, verifying retries and failure paths.
- Manual QA through Kestra UI confirms observability (logs, metrics, task history) for at least one OCR flow execution.


