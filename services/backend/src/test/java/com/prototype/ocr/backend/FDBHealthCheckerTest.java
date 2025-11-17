package com.prototype.ocr.backend;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;
import org.junit.jupiter.api.Test;

final class FDBHealthCheckerTest {
    @Test
    void setsHealthyWhenProbeSucceeds() {
        AtomicBoolean invoked = new AtomicBoolean(false);
        try (FDBHealthChecker checker =
                new FDBHealthChecker(
                        () -> invoked.set(true), Duration.ofMillis(50))) {
            checker.probeOnce();

            assertThat(invoked).isTrue();
            assertThat(checker.isHealthy()).isTrue();
        }
    }

    @Test
    void setsUnhealthyWhenProbeFails() {
        try (FDBHealthChecker checker =
                new FDBHealthChecker(
                        () -> {
                            throw new RuntimeException("boom");
                        },
                        Duration.ofMillis(50))) {
            checker.probeOnce();

            assertThat(checker.isHealthy()).isFalse();
        }
    }
}

