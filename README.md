# Enterprise Shop — Microservices Platform

Cloud-native e-commerce platform built with Spring Boot 3, Spring Cloud, Kafka, and PostgreSQL.

## Architecture

```
┌──────────────┐     ┌──────────────┐     ┌──────────────┐
│  API Gateway │──▶ │   Eureka     │◀────│ Config Server│
│  (port 8080) │     │  (port 8761) │     │  (port 8888) │
└──────┬───────┘     └──────────────┘     └──────────────┘
       │
       ├──── auth-service     (8081) — JWT auth, user management
       ├──── product-service  (8082) — Product CRUD, search
       ├──── inventory-service(8083) — Stock reservation
       ├──── order-service    (8084) — Order creation, Kafka events
       └──── notification-svc (8085) — Email notifications (Kafka consumer)
```

## Quick Start

```bash
# Start infrastructure
docker-compose up -d

# Or run services individually
cd auth-service && mvn spring-boot:run
cd product-service && mvn spring-boot:run
cd inventory-service && mvn spring-boot:run
cd order-service && mvn spring-boot:run
cd notification-service && mvn spring-boot:run
```

## Services

| Service | Port | Description |
|---|---|---|
| auth-service | 8081 | JWT authentication, signup/login/refresh/me |
| product-service | 8082 | Product CRUD with SKU, paginated search |
| inventory-service | 8083 | SKU-based stock reservation |
| order-service | 8084 | Order creation with inventory check + Kafka events |
| notification-service | 8085 | Kafka consumer, simulated email notifications |

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3.5, Spring Cloud 2023.0.0 |
| Database | PostgreSQL + Flyway migrations |
| Messaging | Apache Kafka |
| Service Discovery | Netflix Eureka |
| Config | Spring Cloud Config |
| Resilience | Resilience4j (circuit breaker, retry) |
| Auth | JWT (access + refresh tokens) |
| Monitoring | Micrometer + Prometheus + Grafana |
| Tracing | Micrometer Tracing + Zipkin |
| CI/CD | GitHub Actions |
| Load Testing | k6 |

## API Demo Flow

```bash
# 1. Register a user
curl -X POST http://localhost:8081/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"demo","email":"demo@test.com","password":"Pass123!"}'

# 2. Create a product (admin)
curl -X POST http://localhost:8082/api/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"LAPTOP-001","name":"MacBook Pro","price":2499.99,"category":"electronics"}'

# 3. Check inventory
curl http://localhost:8083/api/inventory?sku=LAPTOP-001

# 4. Create an order
curl -X POST http://localhost:8084/api/orders \
  -H "Content-Type: application/json" \
  -d '{"userEmail":"demo@test.com","items":[{"sku":"LAPTOP-001","quantity":1,"unitPrice":2499.99}]}'
```

## Running Tests

```bash
# Run tests for a specific service
cd auth-service && mvn test

# Run all tests (CI)
mvn -f auth-service/pom.xml verify
mvn -f product-service/pom.xml verify
mvn -f inventory-service/pom.xml verify
mvn -f order-service/pom.xml verify
mvn -f notification-service/pom.xml verify
```

## Load Testing

```bash
# Install k6: https://k6.io/docs/getting-started/installation/
k6 run k6/load-test.js
```

## Event Contracts

See [events/events.md](events/events.md) for Kafka topic schemas and versioning rules.

## Project Structure

```
enterprise-shop-infra/
├── auth-service/           # Authentication & user management
├── product-service/        # Product catalog
├── inventory-service/      # Stock management
├── order-service/          # Order processing
├── notification-service/   # Email notifications
├── config-server/          # Centralized configuration
├── discovery-server/       # Eureka service registry
├── api-gateway/            # API Gateway
├── common/                 # Shared utilities (GlobalExceptionHandler)
├── events/                 # Event schemas (JSON Schema)
├── k6/                     # Load test scripts
├── .github/workflows/      # CI/CD pipelines
└── docker-compose.yml      # Local development stack
```
