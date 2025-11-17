package com.prototype.ocr.backend;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

final class FDBHealthChecker implements AutoCloseable {
    @FunctionalInterface
    interface ProbeSupplier {
        void check() throws Exception;
    }

    private final ProbeSupplier probeSupplier;
    private final Duration interval;
    private final ScheduledExecutorService scheduler;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final AtomicBoolean healthy = new AtomicBoolean(false);

    FDBHealthChecker(ProbeSupplier probeSupplier, Duration interval) {
        this.probeSupplier = Objects.requireNonNull(probeSupplier, "probeSupplier");
        this.interval = Objects.requireNonNull(interval, "interval");
        this.scheduler =
                Executors.newSingleThreadScheduledExecutor(
                        runnable -> {
                            Thread thread = new Thread(runnable, "fdb-health-check");
                            thread.setDaemon(true);
                            return thread;
                        });
    }

    void start() {
        if (running.compareAndSet(false, true)) {
            scheduler.scheduleWithFixedDelay(
                    this::safeProbe, 0, interval.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    boolean isHealthy() {
        return healthy.get();
    }

    void probeOnce() {
        safeProbe();
    }

    private void safeProbe() {
        try {
            probeSupplier.check();
            healthy.set(true);
            System.out.println("[FDB Health] Check passed");
        } catch (Throwable ex) {
            healthy.set(false);
            // Log failure for debugging - use both stdout and stderr
            String errorMsg = "[FDB Health] Check failed: " + ex.getClass().getSimpleName() + ": " + (ex.getMessage() != null ? ex.getMessage() : ex.toString());
            System.out.println(errorMsg);
            System.err.println(errorMsg);
            ex.printStackTrace(System.out);
            ex.printStackTrace();
        }
    }

    @Override
    public void close() {
        scheduler.shutdownNow();
    }
}

