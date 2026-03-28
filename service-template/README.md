# Service Template

> A reusable Spring Boot 3.2 microservice skeleton for the **enterprise-shop** platform.

## Directory structure

```
src/main/java/com/example/servicetemplate/
├── config/          # Bean configurations (CORS, RestTemplate, etc.)
├── controller/      # REST controllers
├── dto/             # Data Transfer Objects (request/response)
├── entity/          # JPA entities
├── mapper/          # MapStruct mappers (Entity ↔ DTO)
├── repository/      # Spring Data JPA repositories
├── service/         # Service interfaces
│   └── impl/        # Service implementations
├── security/        # Security configs (JWT, filters)
├── exception/       # Custom exceptions & global handler
└── util/            # Utility helpers
```

## Quick start

```bash
# Build
mvn clean package

# Run locally
mvn spring-boot:run

# Docker
docker build -t my-service .
docker run -p 8080:8080 my-service
```

## Environment variables

| Variable | Default | Description |
|---|---|---|
| `SERVER_PORT` | `8080` | Server port |
| `DB_URL` | `jdbc:postgresql://localhost:5432/enterpriseshop` | JDBC URL |
| `DB_USERNAME` | `shop_user` | DB user |
| `DB_PASSWORD` | `shop_password` | DB password |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Kafka brokers |
| `EUREKA_URL` | `http://localhost:8761/eureka/` | Eureka endpoint |
| `CONFIG_SERVER_URL` | `http://localhost:8888` | Config Server URL |

## How to instantiate a new service

1. Copy this folder → rename to `<service-name>`.
2. Rename package `com.example.servicetemplate` → `com.example.<servicename>`.
3. Update `pom.xml` `<artifactId>` and `<name>`.
4. Replace sample Entity/DTO/Mapper/Repository/Service/Controller with your domain objects.
5. Edit `application.yml` to set `SERVICE_NAME` and port.
