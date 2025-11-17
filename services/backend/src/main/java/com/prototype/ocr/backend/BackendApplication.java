package com.prototype.ocr.backend;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.Executors;

public final class BackendApplication {
    private BackendApplication() {}

    public static void main(String[] args) throws IOException, URISyntaxException {
        Path clusterFile = resolveClusterFile();
        int port = resolvePort();
        URI clientBaseUri = resolveClientBaseUrl();

        FDBHealthChecker healthChecker =
                new FDBHealthChecker(FdbProbeSupplier.create(clusterFile), Duration.ofSeconds(5));
        healthChecker.start();

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext healthContext = server.createContext("/healthz", new HealthHandler(healthChecker));
        healthContext.getFilters().add(new LoggingFilter("healthz"));

        HttpContext proxyContext = server.createContext("/", new ClientProxyHandler(clientBaseUri));
        proxyContext.getFilters().add(new LoggingFilter("proxy"));

        server.setExecutor(Executors.newFixedThreadPool(8));
        server.start();

        Runtime.getRuntime()
                .addShutdownHook(
                        new Thread(
                                () -> {
                                    healthChecker.close();
                                    server.stop(0);
                                }));

        System.out.printf(
                "Backend ready on port %d (proxy base: %s, cluster file: %s)%n",
                port, clientBaseUri, clusterFile);
    }

    private static Path resolveClusterFile() {
        String candidate =
                Optional.ofNullable(System.getenv("FDB_CLUSTER_FILE"))
                        .orElse("/app/config/fdb.cluster");
        Path cluster = Path.of(candidate);
        if (!Files.exists(cluster)) {
            throw new IllegalStateException("Missing FoundationDB cluster file at " + candidate);
        }
        return cluster;
    }

    private static int resolvePort() {
        return Integer.parseInt(Optional.ofNullable(System.getenv("BACKEND_PORT")).orElse("8080"));
    }

    private static URI resolveClientBaseUrl() throws URISyntaxException {
        String raw =
                Optional.ofNullable(System.getenv("CLIENT_BASE_URL"))
                        .orElse("http://client:4173");
        URI uri = new URI(raw);
        if (!uri.isAbsolute()) {
            throw new IllegalStateException("CLIENT_BASE_URL must be absolute: " + raw);
        }
        return uri;
    }
}

