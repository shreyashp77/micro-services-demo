# Troubleshooting Guide

## Common Issues

### 1. Docker Containers Exit Immediately
*   **Cause**: Often due to missing environment variables or database connection failures.
*   **Fix**: Check logs with `docker-compose logs <service_name>`. Ensure `.env` file exists and variables are correct.

### 2. Port Conflicts
*   **Symptom**: Error `Bind for 0.0.0.0:8080 failed: port is already allocated`.
*   **Fix**: Stop other services using these ports (Jenkins, other Tomcat instances).
    *   `lsof -i :8080` (Mac/Linux) to find the PID.
    *   `kill -9 <PID>` to stop it.

### 3. Kafka Connection Refused
*   **Cause**: Services start before Kafka is fully ready.
*   **Fix**: The `docker-compose.yml` has `depends_on` and health checks, but if it still fails, restart the specific service:
    ```bash
    docker-compose restart order-service
    ```

### 4. Email Not Received
*   **Cause**: The `email-service` might be listening to the wrong topic or Mailhog is down.
*   **Fix**:
    *   Check `email-service` logs: `docker-compose logs email-service`.
    *   Verify the topic in `.env`: It should match the topic `order-service` sends to (`order-created`).
    *   Check Mailhog UI at `http://localhost:8025`.

### 5. Eureka Registration Failures
*   **Cause**: Services cannot reach Eureka.
*   **Fix**: Ensure `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE` is correct in `docker-compose.yml`. It should be `http://eureka-server:8761/eureka/`.

### 6. Database Connection Errors
*   **Cause**: Postgres container not healthy or credentials mismatch.
*   **Fix**:
    *   Check if `user-db`, `product-db`, etc., are running.
    *   Verify `SPRING_DATASOURCE_USERNAME` and `PASSWORD` match the `POSTGRES_USER` and `PASSWORD` in `docker-compose.yml`.
