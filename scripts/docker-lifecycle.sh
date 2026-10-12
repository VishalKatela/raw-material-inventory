#!/usr/bin/env bash
set -euo pipefail

IMAGE_NAME="raw-material-inventory"
TAG="v1.0.0"
CONTAINER_NAME="raw-material-inventory-app"
PORT=8080

echo "=========================================================="
echo " Stage 11: Docker Image and Container Lifecycle Automation "
echo "=========================================================="

echo "[1/7] Building application JAR..."
./mvnw clean package -DskipTests

echo "[2/7] Building and tagging Docker image (${IMAGE_NAME}:${TAG})..."
docker build -t "${IMAGE_NAME}:${TAG}" -t "${IMAGE_NAME}:latest" .
docker images | grep "${IMAGE_NAME}"

echo "[3/7] Running Docker container with port mapping (${PORT}:8080)..."
docker rm -f "${CONTAINER_NAME}" 2>/dev/null || true
docker run -d --name "${CONTAINER_NAME}" -p "${PORT}:8080" "${IMAGE_NAME}:${TAG}"

echo "[4/7] Verifying container status and inspecting logs..."
docker ps --filter "name=${CONTAINER_NAME}"
sleep 5
docker logs --tail 20 "${CONTAINER_NAME}"

echo "[5/7] Executing HTTP health check..."
for attempt in $(seq 1 15); do
    if curl --fail --silent "http://localhost:${PORT}/" >/dev/null; then
        echo "Container is HEALTHY on port ${PORT}!"
        break
    fi
    echo "Waiting for container startup (attempt ${attempt}/15)..."
    sleep 2
done

echo "[6/7] Testing container Stop and Restart lifecycle..."
docker stop "${CONTAINER_NAME}"
echo "Container stopped."
docker start "${CONTAINER_NAME}"
echo "Container restarted."
sleep 3
docker ps --filter "name=${CONTAINER_NAME}"

echo "[7/7] Cleaning up container..."
docker stop "${CONTAINER_NAME}"
docker rm "${CONTAINER_NAME}"
echo "Container lifecycle test completed successfully!"
