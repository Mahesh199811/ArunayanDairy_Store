# Order Service

Spring Boot 3.4 order history and billing simulation service. It runs on port `8083`, persists order snapshots in MongoDB Atlas, and calls the product service to price items and reserve stock.

## Configuration and run

Requires Java 17+, Maven 3.9+, MongoDB Atlas, and the product service running at the configured URL. Copy `.env.example` to `.env`, then set your database URI and the shared secrets:

```bash
cp .env.example .env
```

`.env` is ignored by Git. Spring Boot does not load dotenv files automatically.

Configure these values in `.env`:

| Variable | Required | Description |
| --- | --- | --- |
| `MONGODB_URI` | Yes | MongoDB Atlas URI, including this service's database name |
| `JWT_SECRET_BASE64` | Yes | Same Base64 secret used by `user-service`; at least 32 decoded bytes |
| `INTERNAL_SERVICE_KEY` | Yes | Same private key configured in `product-service` |
| `PRODUCT_SERVICE_URL` | No | Product service URL; defaults to `http://localhost:8082` |
| `FRONTEND_ORIGIN` | No | Allowed browser origin; defaults to `http://localhost:5173` |
| `SERVER_PORT` | No | HTTP port; defaults to `8083` |

```bash
set -a
source .env
set +a
mvn spring-boot:run
```

Start `product-service` first. Use a distinct MongoDB database name for each service, even if they share an Atlas cluster.

## API

Every endpoint requires `Authorization: Bearer <access-token>` from `user-service`. Customers can see only their own orders; administrators can list or inspect any order.

| Method | Path | Description |
| --- | --- | --- |
| `POST` | `/api/orders` | Create an order from active products and reserve inventory |
| `GET` | `/api/orders` | List the signed-in user's order history (`ADMIN` sees all) |
| `GET` | `/api/orders/{id}` | Get an order owned by the caller (`ADMIN` can inspect any order) |

Create-order example:

```json
{
  "items": [
    { "productId": "MongoDB product id", "quantity": 2 }
  ],
  "deliveryAddress": "12 Farm Lane, Bengaluru"
}
```

Prices are read from `product-service`, never trusted from the request. An order stores a snapshot of item names and prices. Billing is simulated only: confirmed orders are marked `SIMULATED_PAID`; no payment provider or real charge is involved. If a later stock reservation or order save fails, the service attempts to release reservations already made. This is a compensating action, not a distributed transaction; monitor the service logs for failed stock releases.

## Test and package

```bash
mvn test
mvn package
```
