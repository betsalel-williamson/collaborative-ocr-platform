#!/bin/bash
# Initialize FoundationDB database if not already initialized
set -e

# Wait for FoundationDB server to be running (not necessarily initialized)
echo "Waiting for FoundationDB server to be ready..."
for i in {1..30}; do
  if fdbcli --exec "status" >/dev/null 2>&1; then
    echo "FoundationDB server is ready"
    break
  fi
  if [ $i -eq 30 ]; then
    echo "Timeout waiting for FoundationDB server"
    exit 1
  fi
  sleep 2
done

# Check if database is already initialized
if fdbcli --exec "status minimal" 2>&1 | grep -q "available"; then
  echo "FoundationDB database is already initialized"
  exit 0
fi

# Initialize database with single memory storage
echo "Initializing FoundationDB database..."
fdbcli --exec "configure new single memory" || {
  # If configure fails, check if database is now available
  if fdbcli --exec "status minimal" 2>&1 | grep -q "available"; then
    echo "Database already initialized"
    exit 0
  fi
  echo "Failed to initialize database"
  exit 1
}

# Verify initialization
sleep 2
if fdbcli --exec "status minimal" 2>&1 | grep -q "available"; then
  echo "FoundationDB database initialized successfully"
  exit 0
else
  echo "Database initialization may have failed"
  exit 1
fi

