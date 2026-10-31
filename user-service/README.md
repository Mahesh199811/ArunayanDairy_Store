# User Service

Spring Boot 3.4 service for account registration, JWT authentication, role-based access control (RBAC), and organization membership. The service listens on port `8081` by default and stores data in MongoDB Atlas.

## Requirements

- Java 17+
- Maven 3.9+
- MongoDB Atlas cluster and connection URI

## Configuration

Copy the included environment template, then edit `.env` with your MongoDB Atlas credentials and generated JWT key:

```bash
cp .env.example .env
```

`.env` is ignored by Git. Spring Boot does not load dotenv files automatically, so export the values into your shell before starting the service:

| Variable | Required | Description |
| --- | --- | --- |
| `MONGODB_URI` | Yes | MongoDB Atlas URI, including the database name |
| `JWT_SECRET_BASE64` | Yes | Base64-encoded random key of at least 32 bytes |
| `BOOTSTRAP_ADMIN_EMAIL` | No | Email for the initial administrator |
| `BOOTSTRAP_ADMIN_PASSWORD` | No | Password for the initial administrator |
| `BOOTSTRAP_ADMIN_NAME` | No | Display name for the initial administrator |
| `FRONTEND_ORIGIN` | No | Allowed frontend origin; defaults to `http://localhost:5173` |
| `SERVER_PORT` | No | HTTP port; defaults to `8081` |
| `JWT_EXPIRATION` | No | Access-token lifetime; defaults to `PT15M` |

Generate a signing key using a secure local random generator and put the output in `JWT_SECRET_BASE64` in `.env`:

```bash
openssl rand -base64 32
```

Do not commit the generated key. The bootstrap admin variables must either both be set or both be omitted. Without an administrator, public registration and login work, but admin-only organization management is unavailable.

## Run locally

From `user-service/`, after editing `.env`, load it and run:

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

Use the same `JWT_SECRET_BASE64` in all three services.

The health endpoint is `GET http://localhost:8081/actuator/health`.

## API

All request and response bodies use JSON. Signup users receive the `USER` role; the role is never accepted from the public registration request. Supply the returned access token as `Authorization: Bearer <token>` for protected endpoints.

| Method | Path | Access | Description |
| --- | --- | --- | --- |
| `POST` | `/api/auth/register` | Public | Create an account and receive an access token |
| `POST` | `/api/auth/login` | Public | Authenticate and receive an access token |
| `GET` | `/api/auth/me` | Authenticated | Get the current account and roles |
| `GET` | `/api/organizations` | Authenticated | List organizations the current user belongs to |
| `POST` | `/api/organizations` | `ADMIN` | Create an organization |
| `POST` | `/api/organizations/{organizationId}/members` | `ADMIN` | Add an existing user by email |

Signup example:

```json
{
  "name": "A dairy customer",
  "email": "customer@example.com",
  "password": "a-strong-password"
}
```

The returned token is short-lived (15 minutes by default). Refresh tokens, password reset, account verification, organization deletion, and billing are not implemented.

## Build and test

```bash
mvn test
mvn package
```
