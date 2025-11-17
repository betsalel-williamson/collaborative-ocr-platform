FROM gradle:8.7-jdk17 AS build
WORKDIR /workspace

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

RUN gradle --no-daemon clean installDist

FROM eclipse-temurin:17-jre-jammy AS runtime

# Install FoundationDB client library
# For amd64: use DEB package directly
# For aarch64: convert RPM to DEB using alien
RUN DEBIAN_FRONTEND=noninteractive apt-get update \
    && DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends curl wget ca-certificates \
    && ARCH=$(dpkg --print-architecture) \
    && if [ "$ARCH" = "amd64" ]; then \
        wget -O /tmp/foundationdb-clients.deb https://github.com/apple/foundationdb/releases/download/7.3.69/foundationdb-clients_7.3.69-1_amd64.deb \
        && DEBIAN_FRONTEND=noninteractive dpkg -i /tmp/foundationdb-clients.deb || DEBIAN_FRONTEND=noninteractive apt-get install -y -f; \
    elif [ "$ARCH" = "arm64" ]; then \
        DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends alien \
        && wget -O /tmp/foundationdb-clients.rpm https://github.com/apple/foundationdb/releases/download/7.3.69/foundationdb-clients-7.3.69-1.el9.aarch64.rpm \
        && cd /tmp && alien -d foundationdb-clients.rpm \
        && DEBIAN_FRONTEND=noninteractive dpkg -i /tmp/foundationdb-clients*.deb || DEBIAN_FRONTEND=noninteractive apt-get install -y -f; \
    else \
        echo "Unsupported architecture: $ARCH" && exit 1; \
    fi \
    && rm -rf /var/lib/apt/lists/* /tmp/foundationdb-clients.*

WORKDIR /app
COPY --from=build /workspace/build/install/backend /app

# Ensure FoundationDB native libraries are in the library path
ENV LD_LIBRARY_PATH=/usr/lib64:${LD_LIBRARY_PATH}

ENV BACKEND_PORT=8080
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD curl -fsS http://localhost:${BACKEND_PORT}/healthz || exit 1

CMD ["/app/bin/backend"]

