package com.prototype.ocr.backend;

import com.apple.foundationdb.Database;
import com.apple.foundationdb.FDB;
import java.nio.file.Path;
import java.time.Duration;

final class FdbProbeSupplier {
    private static final int API_VERSION = 730; // FoundationDB 7.3.x API (Java client 7.3.46+ includes ARM64 support)
    private static final Duration TRANSACTION_TIMEOUT = Duration.ofSeconds(5);

    private FdbProbeSupplier() {}

    static FDBHealthChecker.ProbeSupplier create(Path clusterFile) {
        Path normalized = clusterFile.toAbsolutePath().normalize();
        return () -> probe(normalized);
    }

    private static void probe(Path clusterFile) throws Exception {
        // Select API version (730 for FoundationDB 7.3.69)
        FDB fdb = FDB.selectAPIVersion(API_VERSION);
        // Open database using cluster file path
        try (Database database = fdb.open(clusterFile.toString())) {
            // Configure transaction timeout
            database.options().setTransactionTimeout(TRANSACTION_TIMEOUT.toMillis());
            // Execute a simple read transaction to verify connectivity
            database.run((transaction) -> {
                transaction.get(new byte[] {0}).join();
                return null;
            });
        }
    }
}

