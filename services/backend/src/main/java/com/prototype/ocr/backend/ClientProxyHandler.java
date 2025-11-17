package com.prototype.ocr.backend;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

final class ClientProxyHandler implements HttpHandler {
    private final HttpClient httpClient;
    private final URI clientBaseUri;

    ClientProxyHandler(URI clientBaseUri) {
        this.clientBaseUri = clientBaseUri;
        this.httpClient =
                HttpClient.newBuilder()
                        .followRedirects(HttpClient.Redirect.NORMAL)
                        .connectTimeout(Duration.ofSeconds(5))
                        .build();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (!"GET".equalsIgnoreCase(exchange.getRequestMethod())
                && !"HEAD".equalsIgnoreCase(exchange.getRequestMethod())) {
            exchange.sendResponseHeaders(405, -1);
            return;
        }

        try {
            URI target = resolveTarget(exchange);
            HttpRequest request =
                    HttpRequest.newBuilder(target)
                            .method(
                                    exchange.getRequestMethod(),
                                    mapBody(exchange.getRequestMethod(), exchange.getRequestBody()))
                            .build();

            HttpResponse<InputStream> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            copyHeaders(response.headers(), exchange.getResponseHeaders());
            long contentLength =
                    response.headers().firstValueAsLong("Content-Length").orElse(-1);
            exchange.sendResponseHeaders(response.statusCode(), contentLength);
            if (!"HEAD".equalsIgnoreCase(exchange.getRequestMethod())) {
                try (InputStream body = response.body();
                        OutputStream responseBody = exchange.getResponseBody()) {
                    if (body != null) {
                        body.transferTo(responseBody);
                    }
                }
            }
        } catch (Exception ex) {
            byte[] message =
                    ("Proxy error: " + ex.getMessage()).getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(502, message.length);
            try (OutputStream responseBody = exchange.getResponseBody()) {
                responseBody.write(message);
            }
        }
    }

    private URI resolveTarget(HttpExchange exchange) {
        String path = exchange.getRequestURI().getRawPath();
        String query = exchange.getRequestURI().getRawQuery();
        String suffix = query == null ? path : path + "?" + query;
        return clientBaseUri.resolve(suffix);
    }

    private static HttpRequest.BodyPublisher mapBody(String method, InputStream body)
            throws IOException {
        if ("HEAD".equalsIgnoreCase(method)) {
            return HttpRequest.BodyPublishers.noBody();
        }
        byte[] payload = body.readAllBytes();
        if (payload.length == 0) {
            return HttpRequest.BodyPublishers.noBody();
        }
        return HttpRequest.BodyPublishers.ofByteArray(payload);
    }

    private static void copyHeaders(
            java.net.http.HttpHeaders source, Headers destination) {
        for (String key : source.map().keySet()) {
            List<String> values = source.map().get(key);
            if (values != null) {
                destination.put(key, values);
            }
        }
    }
}

