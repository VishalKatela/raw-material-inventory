# Docker Image and Container Lifecycle

## Overview
This document specifies the containerization lifecycle for the Raw Material Inventory System, including build commands, tagging conventions, container runtime parameters, and health monitoring.

## Base image and multi-environment portability
- Base Image: `eclipse-temurin:21-jre` (lightweight, official OpenJDK 21 runtime).
- Application Path: `/app/app.jar`.
- Default Port: `8080`.
- Non-root user & minimal attack surface.

## Docker Lifecycle Steps

### 1. Build and Tagging
Compile the package and create tagged versions:
```bash
./mvnw clean package -DskipTests
docker build -t raw-material-inventory:v1.0.0 -t raw-material-inventory:latest .
```

Verify the image details:
```bash
docker images | grep raw-material-inventory
# Output shows IMAGE ID, TAG (v1.0.0, latest), SIZE (~300MB)
```

### 2. Run Container with Port Mapping
Run detached with port `8080` forwarded and restart policy:
```bash
docker run --detach \
  --name raw-material-inventory \
  --restart unless-stopped \
  --publish 8080:8080 \
  raw-material-inventory:v1.0.0
```

### 3. Inspect Running Containers and Logs
Check process status:
```bash
docker ps --filter "name=raw-material-inventory"
```

Stream application startup logs:
```bash
docker logs -f raw-material-inventory
```

### 4. Health Check
Verify endpoint accessibility:
```bash
curl -I http://localhost:8080/
# Returns HTTP/1.1 200 OK
```

### 5. Stop and Restart Lifecycle
```bash
# Stop running instance
docker stop raw-material-inventory

# Restart instance
docker start raw-material-inventory

# Verify recovery
docker ps
```

### 6. Cleanup
```bash
docker stop raw-material-inventory
docker rm raw-material-inventory
```

## Docker Compose Support
Alternatively, launch the complete container stack using:
```bash
docker-compose up -d
docker-compose ps
docker-compose logs
docker-compose down
```
