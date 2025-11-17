# Interactive Test Commands

Use these commands to test the infrastructure once `make infra-up` has completed.

## Check Service Status

```bash
# Check all containers are running
docker ps --filter "name=ocr-"

# Check specific service logs
docker logs ocr-foundationdb
docker logs ocr-kestra
docker logs ocr-review-backend-1
docker logs ocr-review-client-1

# Follow logs in real-time
docker logs -f ocr-foundationdb
```

## Test Backend Health Endpoint

```bash
# Check backend health (includes FoundationDB connectivity)
curl http://localhost:8080/healthz

# Pretty print JSON response
curl -s http://localhost:8080/healthz | jq .

# Check backend is proxying client
curl -I http://localhost:8080/
```

## Test FoundationDB

```bash
# Connect to FoundationDB container and run fdbcli
docker exec -it ocr-foundationdb fdbcli

# Once in fdbcli, try:
#   status
#   status minimal
#   exit

# Test FoundationDB from backend container
docker exec ocr-review-backend-1 java -cp /app/classes com.prototype.ocr.backend.FDBHealthChecker
```

## Test Client (Vite PWA)

```bash
# Access client directly
curl http://localhost:4173/

# Open in browser
open http://localhost:4173

# Check client is being proxied through backend
curl http://localhost:8080/
```

## Test Kestra

```bash
# Access Kestra UI in browser
open http://localhost:8081

# Check Kestra API
curl http://localhost:8081/api/v1/flows

# List flows via Kestra CLI (from container)
docker exec ocr-kestra kestra flow ls

# Validate a flow
docker exec ocr-kestra kestra flow validate /app/flows/hello-world.yaml
```

## Test Full Stack Integration

```bash
# Test backend health (should show FoundationDB is connected)
curl -v http://localhost:8080/healthz

# Test client through backend proxy
curl -v http://localhost:8080/

# Check all services are healthy
docker ps --format "table {{.Names}}\t{{.Status}}\t{{.Health}}" | grep ocr
```

## Network Testing

```bash
# Test backend can reach FoundationDB
docker exec ocr-review-backend-1 ping -c 3 foundationdb

# Test backend can reach client
docker exec ocr-review-backend-1 ping -c 3 client

# Check network connectivity
docker network inspect ocr-review_default
```

## FoundationDB Cluster File

```bash
# View cluster file
cat config/foundationdb/fdb.cluster

# Check cluster file is mounted in containers
docker exec ocr-review-backend-1 cat /app/config/fdb.cluster
docker exec ocr-foundationdb cat /etc/foundationdb/fdb.cluster
```

## Stop and Cleanup

```bash
# Stop all services
make infra-down

# Stop and remove volumes (clean slate)
docker compose down -v

# Remove all containers and volumes
docker compose down -v --remove-orphans
```

## Quick Health Check Script

```bash
#!/bin/bash
echo "=== FoundationDB ==="
docker exec ocr-foundationdb fdbcli --exec "status minimal" 2>&1 | head -3

echo -e "\n=== Backend Health ==="
curl -s http://localhost:8080/healthz | jq . || curl -s http://localhost:8080/healthz

echo -e "\n=== Client ==="
curl -s -o /dev/null -w "Status: %{http_code}\n" http://localhost:4173/

echo -e "\n=== Kestra ==="
curl -s -o /dev/null -w "Status: %{http_code}\n" http://localhost:8081/
```

