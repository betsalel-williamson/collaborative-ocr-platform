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
        } catch (Exception | UnsatisfiedLinkError ex) {
            healthy.set(false);
        }
    }

    @Override
    public void close() {
        scheduler.shutdownNow();
    }
}

