# Arunayan Dairy Store

A dairy-store application with a React storefront and three Spring Boot 3.x microservices. The services use MongoDB Atlas; User Service issues JWT access tokens used by Product and Order Services.

## Technology

- **Frontend:** React 18, Vite, Tailwind CSS
- **Backend:** Java 17+, Spring Boot 3.4, Maven
- **Database:** MongoDB Atlas
- **Authentication:** JWT with role-based access (`USER`, `ADMIN`)

## Services

| Service | Port | Responsibilities |
| --- | ---: | --- |
| [user-service/](./user-service/) | `8081` | Registration, login, JWT, RBAC, and organization management |
| [product-service/](./product-service/) | `8082` | Product catalog, inventory, and stock reservation |
| [order-service/](./order-service/) | `8083` | Order creation and history; simulated billing |

## Project layout

```text
frontend/          React 18, Vite, Tailwind CSS storefront
user-service/      Spring Boot authentication and user API
product-service/   Spring Boot catalog and inventory API
order-service/     Spring Boot orders and billing simulation API
```

## Run locally

Install Node.js 18+ and Java 17+, and have a MongoDB Atlas cluster ready. In each service directory, copy `.env.example` to `.env` and fill in the values. The `.env` files are ignored by Git. Use separate MongoDB database names for each service, the same `JWT_SECRET_BASE64` across all three, and the same private `INTERNAL_SERVICE_KEY` in Product and Order Services. Spring Boot does not load `.env` files automatically, so export each file before starting its service:

Start each backend in its own terminal, in this order:

```bash
cd product-service
cp .env.example .env # First time only; then edit .env
set -a && source .env && set +a
mvn spring-boot:run
```

```bash
cd user-service
cp .env.example .env # First time only; then edit .env
set -a && source .env && set +a
mvn spring-boot:run
```

```bash
cd order-service
cp .env.example .env # First time only; then edit .env
set -a && source .env && set +a
mvn spring-boot:run
```

Then start the storefront:

```bash
cd frontend
npm install
npm run dev
```

The storefront runs at `http://localhost:5173`. Service health checks are available at `/actuator/health` on each backend port.

## Documentation and tests

- [Frontend setup and scripts](./frontend/README.md)
- [User Service configuration and API](./user-service/README.md)
- [Product Service configuration and API](./product-service/README.md)
- [Order Service configuration and API](./order-service/README.md)

Run backend tests from each service directory with `mvn test`. Order billing is a simulation only; the application does not charge real payments. Order creation coordinates stock reservations across services with a compensating release on failure, not a distributed database transaction.
