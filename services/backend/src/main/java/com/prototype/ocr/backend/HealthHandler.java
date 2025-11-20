package com.prototype.ocr.backend;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

final class HealthHandler implements HttpHandler {
    private final FDBHealthChecker healthChecker;

    HealthHandler(FDBHealthChecker healthChecker) {
        this.healthChecker = healthChecker;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        boolean healthy = healthChecker.isHealthy();
        byte[] payload =
                ("{\"foundationdb\":" + healthy + "}").getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(healthy ? 200 : 503, payload.length);
        try (OutputStream outputStream = exchange.getResponseBody()) {
            outputStream.write(payload);
        }
    }
}

