# Product Service

Spring Boot 3.4 product catalog and inventory service. It runs on port `8082` and uses MongoDB Atlas.

## Configuration and run

Requires Java 17+, Maven 3.9+, and a MongoDB Atlas database. Copy `.env.example` to `.env`, fill in your Atlas URI, JWT secret, and private internal service key, then export its values before running:

```bash
cp .env.example .env
```

`.env` is ignored by Git. Spring Boot does not load dotenv files automatically.

Configure these values in `.env`:

| Variable | Required | Description |
| --- | --- | --- |
| `MONGODB_URI` | Yes | MongoDB Atlas URI, including this service's database name |
| `JWT_SECRET_BASE64` | Yes | Same Base64 secret used by `user-service`; at least 32 decoded bytes |
| `INTERNAL_SERVICE_KEY` | Yes | Shared secret used only by the order service for stock reserve/release |
| `FRONTEND_ORIGIN` | No | Allowed browser origin; defaults to `http://localhost:5173` |
| `SERVER_PORT` | No | HTTP port; defaults to `8082` |

Use the same JWT secret in every service so they can validate access tokens from `user-service`. Keep the internal service key private and use the exact same value in `order-service`; do not commit either secret.

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

## API

Public product endpoints return active products. Product creation, updates, stock adjustments, and deactivation require an `ADMIN` JWT. Inventory reservation endpoints are internal and require `X-Internal-Service-Key`; they are intended for the order service only.

| Method | Path | Access | Description |
| --- | --- | --- | --- |
| `GET` | `/api/products` | Public | List active products |
| `GET` | `/api/products/{id}` | Public | Get an active product |
| `POST` | `/api/products` | `ADMIN` | Create a product |
| `PUT` | `/api/products/{id}` | `ADMIN` | Update product details (SKU is immutable) |
| `PATCH` | `/api/products/{id}/stock` | `ADMIN` | Set available stock |
| `DELETE` | `/api/products/{id}` | `ADMIN` | Deactivate a product |
| `POST` | `/api/internal/inventory/{id}/reserve?quantity=N` | Internal key | Atomically reserve available stock |
| `POST` | `/api/internal/inventory/{id}/release?quantity=N` | Internal key | Return reserved stock |

Product amounts are denominated in INR. The internal inventory endpoints are not browser APIs; do not expose the shared key to the frontend.

## Test and package

```bash
mvn test
mvn package
```
