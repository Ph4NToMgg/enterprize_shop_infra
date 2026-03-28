# Auth Service

Authentication and user management service for enterprise-shop.

## Token Format

### Access Token (JWT)
```
Header:  { "alg": "HS256", "typ": "JWT" }
Payload: { "sub": "<email>", "roles": "ROLE_USER|ROLE_ADMIN", "iat": ..., "exp": ... }
```
- **Lifetime**: 15 minutes
- **Algorithm**: HMAC-SHA256

### Refresh Token
- UUID string stored in DB
- **Lifetime**: 7 days

## Required Headers

| Endpoint | Header |
|---|---|
| `POST /api/auth/signup` | `Content-Type: application/json` |
| `POST /api/auth/login` | `Content-Type: application/json` |
| `POST /api/auth/refresh` | `Content-Type: application/json` |
| `GET /api/auth/me` | `Authorization: Bearer <access_token>` |
| All protected endpoints | `Authorization: Bearer <access_token>` |

## API Endpoints

### POST /api/auth/signup
Register a new user.
```bash
curl -X POST http://localhost:8081/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "email": "john@example.com",
    "password": "SecurePass123!"
  }'
```
**Response** (201):
```json
{
  "accessToken": "eyJhbGciOi...",
  "refreshToken": "550e8400-e29b-41d4-a716-446655440000",
  "tokenType": "Bearer",
  "username": "john",
  "email": "john@example.com",
  "role": "ROLE_USER"
}
```

### POST /api/auth/login
Authenticate with username/email and password.
```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john@example.com",
    "password": "SecurePass123!"
  }'
```

### POST /api/auth/refresh
Get a new access token using a refresh token.
```bash
curl -X POST http://localhost:8081/api/auth/refresh \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "550e8400-e29b-41d4-a716-446655440000"
  }'
```

### GET /api/auth/me
Get current authenticated user info.
```bash
curl http://localhost:8081/api/auth/me \
  -H "Authorization: Bearer eyJhbGciOi..."
```
**Response** (200):
```json
{
  "id": "...",
  "username": "john",
  "email": "john@example.com",
  "role": "ROLE_USER",
  "enabled": true
}
```

## Running Locally

```bash
# Start dependencies
docker-compose up -d postgres

# Run service
cd auth-service
mvn spring-boot:run

# Run tests
mvn test
```

## Configuration

| Property | Default | Description |
|---|---|---|
| `jwt.secret` | (base64 dev key) | Base64-encoded HMAC signing secret |
| `jwt.access-expiration-ms` | 900000 (15min) | Access token lifetime |
| `jwt.refresh-expiration-ms` | 604800000 (7d) | Refresh token lifetime |
| `DB_URL` | `jdbc:postgresql://localhost:5432/enterpriseshop` | Database URL |
