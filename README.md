# Microservices E-Commerce Demo

> **Portfolio Project**: A comprehensive demonstration of a modern, event-driven microservices architecture using Spring Boot, Kafka, Docker, and Kubernetes-ready patterns.

## 📖 Project Overview

This repository demonstrates a robust e-commerce backend built with **Spring Boot** microservices. It showcases industry-standard patterns including **Service Discovery** (Eureka), **API Gateway** (Spring Cloud Gateway), **Centralized Authentication** (JWT), **Event-Driven Architecture** (Kafka), and **Distributed Caching** (Redis).

The system handles core e-commerce flows: user registration/auth, product management, order placement, and asynchronous email notifications.

### Key Features
*   **Microservices Architecture**: Decoupled services for scalability and independent deployment.
*   **Event-Driven**: Asynchronous communication between Order and Email services using Kafka.
*   **Secure**: Centralized JWT authentication and authorization at the Gateway and Service level.
*   **Resilient**: Circuit breaking and service discovery integration.
*   **Containerized**: Fully Dockerized setup with `docker-compose` for easy local orchestration.

---

## 🏗️ Architecture

### High-Level Components

```mermaid
graph TD
    Client([Client App / Postman]) -->|HTTPS| Gateway[API Gateway :8080]
    
    subgraph Infrastructure
        Eureka[Eureka Registry :8761]
        Kafka[Kafka Broker :9092]
        Redis[Redis Cache :6379]
        Zookeeper[Zookeeper :2181]
    end

    subgraph Services
        Auth[Auth Service :9000]
        User[User Service :9002]
        Product[Product Service :9001]
        Order[Order Service :9003]
        Email[Email Service :Python]
    end

    subgraph Databases
        AuthDB[(Auth DB)]
        UserDB[(User DB)]
        ProductDB[(Product DB)]
    end

    %% Connections
    Gateway -->|Auth Check| Auth
    Gateway -->|Route| User
    Gateway -->|Route| Product
    Gateway -->|Route| Order
    
    Auth --> AuthDB
    User --> UserDB
    Product --> ProductDB
    Product -.->|Cache| Redis
    
    Order -->|Publish 'order-created'| Kafka
    Kafka -->|Consume 'order-created'| Email
    
    %% Service Discovery
    Gateway -.->|Discover| Eureka
    Auth -.->|Register| Eureka
    User -.->|Register| Eureka
    Product -.->|Register| Eureka
    Order -.->|Register| Eureka
```

### Request Flow (Order Placement)

```mermaid
sequenceDiagram
    participant Client
    participant Gateway
    participant Auth as Auth Service
    participant Order as Order Service
    participant Kafka
    participant Email as Email Service

    Client->>Gateway: POST /orders (Bearer Token)
    Gateway->>Auth: Validate Token
    Auth-->>Gateway: Token Valid
    Gateway->>Order: Forward Request
    Order->>Order: Create Order Logic
    Order->>Kafka: Publish "order-created" event
    Order-->>Gateway: 200 OK
    Gateway-->>Client: 200 OK (Order Placed)
    
    par Async Processing
        Kafka->>Email: Consume event
        Email->>Email: Send Confirmation Email
    end
```

### Deployment Architecture

```mermaid
graph LR
    subgraph Host Machine
        Docker[Docker Engine]
        subgraph Network[Bridge Network]
            Container1[API Gateway]
            Container2[Services...]
            Container3[Databases]
            Container4[Message Broker]
        end
    end
    
    Dev[Developer] -->|docker-compose up| Docker
```

---

## 🛠️ Tech Stack

| Category | Technology | Usage |
|----------|------------|-------|
| **Framework** | Spring Boot 3.5.6 | Core framework for all Java services |
| **Language** | Java 17 | Primary backend language |
| **Language** | Python 3 | Email service consumer |
| **Discovery** | Netflix Eureka | Service registration and discovery |
| **Gateway** | Spring Cloud Gateway | Entry point, routing, and auth validation |
| **Database** | PostgreSQL 18 | Relational data storage (per-service DBs) |
| **Messaging** | Apache Kafka | Asynchronous event streaming |
| **Caching** | Redis | Distributed caching for products |
| **Security** | Spring Security + JWT | Stateless authentication |
| **Build** | Maven | Dependency management |
| **Container** | Docker & Docker Compose | Containerization and orchestration |

---

## 🔌 API Reference

### Authentication
**Base URL**: `http://localhost:8080/auth`

| Method | Endpoint | Description | Auth Required |
|--------|----------|-------------|---------------|
| `POST` | `/token` | Login to get JWT. Requires Basic Auth header. | No |
| `POST` | `/register` | Register a new admin user. | No |

**Example: Get Token**
```bash
# Basic Auth (username:password base64 encoded)
curl -X POST http://localhost:8080/auth/token \
  -H "Authorization: Basic dXNlcnNlcnZpY2VfdXNlcjpyb290"
```

### Products
**Base URL**: `http://localhost:8080/products`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/` | List all products |
| `GET` | `/{id}` | Get product details |
| `POST` | `/` | Create a product (Admin) |

**Example: Create Product**
```bash
curl -X POST http://localhost:8080/products \
  -H "Authorization: Bearer <YOUR_JWT>" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptop",
    "price": 1200.00,
    "description": "High performance laptop"
  }'
```

### Orders
**Base URL**: `http://localhost:8080/orders`

| Method | Endpoint | Description |
|--------|----------|-------------|
| `POST` | `/` | Place a new order |

**Example: Place Order**
```bash
curl -X POST http://localhost:8080/orders \
  -H "Authorization: Bearer <YOUR_JWT>" \
  -H "Content-Type: application/json" \
  -d '{
    "productId": "12345",
    "quantity": 1
  }'
```

---

## 🚀 Run Locally

### Prerequisites
*   Docker & Docker Compose
*   Java 17+ (for local dev)
*   Maven

### Quick Start (Docker)
The easiest way to run the entire system is via Docker Compose.

1.  **Clone the repository**
    ```bash
    git clone <repo-url>
    cd micro-services-demo
    ```

2.  **Configure Environment**
    Copy the sample env file:
    ```bash
    cp .env.sample .env
    ```
    *Note: You may need to do this for individual services if running them standalone, but the root `docker-compose.yml` handles most env injection.*

3.  **Start Services**
    ```bash
    docker-compose up -d --build
    ```
    *This will start Postgres, Kafka, Zookeeper, Redis, Mailhog, and all microservices.*

4.  **Verify**
    *   **Eureka Dashboard**: [http://localhost:8761](http://localhost:8761)
    *   **Mailhog (Email Test)**: [http://localhost:8025](http://localhost:8025)
    *   **API Gateway**: [http://localhost:8080](http://localhost:8080)

### Local Development (Hybrid)
To run a specific service (e.g., `product-service`) locally while keeping infra in Docker:

1.  Start infrastructure only:
    ```bash
    cd devops/dev
    docker-compose up -d
    ```
2.  Run the service:
    ```bash
    cd product-service
    mvn spring-boot:run
    ```

---

## 📊 Observability & Monitoring

*   **Distributed Tracing**: Currently, logs are aggregated per container. Use `docker-compose logs -f <service_name>` to tail logs.
*   **Mailhog**: Used to capture emails sent by the Email Service. Access the UI at `http://localhost:8025`.
*   **Health Checks**: All services expose Spring Boot Actuator health endpoints (e.g., `/actuator/health`).

---

## 🔒 Security

*   **JWT Authentication**: The `auth-service` issues tokens signed with a secret key.
*   **Gateway Validation**: The `api-gateway` validates the signature of incoming requests before routing.
*   **Secret Management**: Secrets are currently passed via environment variables.
    *   *Remediation Note*: For production, use a secret manager (Vault, AWS Secrets Manager) instead of `.env` files.

---

## 📂 Data Model

*   **User DB**: Stores user profiles and credentials.
*   **Product DB**: Stores product catalog (Name, Price, Description, Stock).
*   **Auth DB**: Stores admin/auth specific data (if separate from User DB).
*   **Migrations**: Database schemas are managed via Hibernate `update` (auto-ddl).

---

## 🤝 Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for details on code style and pull request process.

## ❓ Troubleshooting

See [TROUBLESHOOTING.md](TROUBLESHOOTING.md) for common issues like port conflicts or Docker errors.
