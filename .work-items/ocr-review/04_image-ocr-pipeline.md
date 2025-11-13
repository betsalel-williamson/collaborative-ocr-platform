## Objective

Implement the ingestion workflow that accepts image uploads, stores binary blobs in chunked subspaces, invokes Google Cloud Vision OCR via Kestra flows, and persists extracted text revisions in Record Layer.

## Acceptance Criteria

- Upload endpoint streams files directly into FoundationDB using chunk sizes aligned with blob pattern recommendations. [\[2\]](https://apple.github.io/foundationdb/blob.html)
- Kestra-triggered tasks call Google Cloud Vision APIs, capture language hints, and record structured text blocks with source metadata.
- Flow state transitions (`pending`, `processing`, `completed`, `failed`) propagate back to backend and client via Record Layer records.
- Resulting OCR revisions stored as transactional records linked to image blobs and ready for collaborative editing.

## Test Strategy

- Contract tests mock Google Cloud Vision responses to validate mapping into Record Layer entities.
- Load tests upload representative image sizes to ensure chunk storage remains within FoundationDB limits.
- Integration test covers end-to-end upload → Kestra flow execution → OCR → stored revision lifecycle with cleanup of temporary artifacts.


