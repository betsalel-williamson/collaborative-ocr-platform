FROM gradle:8.7-jdk17 AS build
WORKDIR /workspace

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

RUN gradle --no-daemon clean installDist

FROM eclipse-temurin:17-jre-jammy AS runtime

# Set noninteractive mode for all apt operations
ENV DEBIAN_FRONTEND=noninteractive

# Install FoundationDB client library
# For amd64: use DEB package directly
# For aarch64: convert RPM to DEB using alien
RUN apt-get update \
    && apt-get install -y --no-install-recommends curl wget ca-certificates \
    && ARCH=$(dpkg --print-architecture) \
    && if [ "$ARCH" = "amd64" ]; then \
        wget -O /tmp/foundationdb-clients.deb https://github.com/apple/foundationdb/releases/download/7.3.69/foundationdb-clients_7.3.69-1_amd64.deb \
        && dpkg -i /tmp/foundationdb-clients.deb || apt-get install -y -f; \
    elif [ "$ARCH" = "arm64" ]; then \
        apt-get install -y --no-install-recommends alien \
        && wget -O /tmp/foundationdb-clients.rpm https://github.com/apple/foundationdb/releases/download/7.3.69/foundationdb-clients-7.3.69-1.el9.aarch64.rpm \
        && cd /tmp && alien -d foundationdb-clients.rpm \
        && dpkg -i /tmp/foundationdb-clients*.deb || apt-get install -y -f; \
    else \
        echo "Unsupported architecture: $ARCH" && exit 1; \
    fi \
    && rm -rf /var/lib/apt/lists/* /tmp/foundationdb-clients.*

# Create non-root user for running the application
RUN groupadd -r appuser && useradd -r -g appuser appuser

WORKDIR /app
COPY --from=build /workspace/build/install/backend /app

# Set ownership of application files to non-root user
RUN chown -R appuser:appuser /app

# Ensure FoundationDB native libraries are in the library path
ENV LD_LIBRARY_PATH=/usr/lib64:${LD_LIBRARY_PATH}

ENV BACKEND_PORT=8080
EXPOSE 8080

# Switch to non-root user
USER appuser

HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD curl -fsS http://localhost:${BACKEND_PORT}/healthz || exit 1

CMD ["/app/bin/backend"]

