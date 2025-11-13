## Objective

Build a Vite-based progressive web application that caches shell assets, image thumbnails, and OCR data for offline use following MDN caching guidance. [\[1\]](https://developer.mozilla.org/en-US/docs/Web/Progressive_web_apps/Guides/Caching)

## Acceptance Criteria

- Service worker implements precache for app shell and runtime caching for API responses per MDN best practices.
- Manifest enables install banner with appropriate icons and offline start URL.
- Local persistence (IndexedDB or equivalent) stores OCR drafts and sharing selections while offline.
- UI annotations indicate connectivity status and pending sync operations.

## Test Strategy

- Use Lighthouse PWA audit to confirm installability and offline readiness.
- Write Jest/Vitest tests for service worker cache logic using Workbox or equivalent mocks.
- Perform manual offline testing in Chrome DevTools to verify cached pages and queued edits.


