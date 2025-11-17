FROM gradle:8.7-jdk17 AS build
WORKDIR /workspace

COPY build.gradle.kts settings.gradle.kts ./
COPY src ./src

RUN gradle --no-daemon clean installDist

FROM eclipse-temurin:17-jre-jammy AS runtime

RUN apt-get update \
    && apt-get install -y --no-install-recommends curl wget ca-certificates \
    && wget -O /tmp/foundationdb-clients.deb https://github.com/apple/foundationdb/releases/download/7.3.27/foundationdb-clients_7.3.27-1_amd64.deb \
    && dpkg -i /tmp/foundationdb-clients.deb \
    && rm -rf /var/lib/apt/lists/* /tmp/foundationdb-clients.deb

WORKDIR /app
COPY --from=build /workspace/build/install/backend /app

ENV BACKEND_PORT=8080
EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=3s --retries=3 CMD curl -fsS http://localhost:${BACKEND_PORT}/healthz || exit 1

CMD ["/app/bin/backend"]

