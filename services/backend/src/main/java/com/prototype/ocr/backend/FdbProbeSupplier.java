package com.prototype.ocr.backend;

import com.apple.foundationdb.Database;
import com.apple.foundationdb.FDB;
import java.nio.file.Path;
import java.time.Duration;

final class FdbProbeSupplier {
    private static final int API_VERSION = 730;
    private static final Duration TRANSACTION_TIMEOUT = Duration.ofSeconds(5);

    private FdbProbeSupplier() {}

    static FDBHealthChecker.ProbeSupplier create(Path clusterFile) {
        Path normalized = clusterFile.toAbsolutePath().normalize();
        return () -> probe(normalized);
    }

    private static void probe(Path clusterFile) throws Exception {
        FDB fdb = FDB.selectAPIVersion(API_VERSION);
        try (Database database = fdb.open(clusterFile.toString())) {
            database.options().setTransactionTimeout(TRANSACTION_TIMEOUT.toMillis());
            database.readAsync(
                            transaction -> {
                                transaction.get(new byte[] {0}).join();
                                return null;
                            })
                    .join();
        }
    }
}

