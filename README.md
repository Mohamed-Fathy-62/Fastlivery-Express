# Fastlivery Express

Fastlivery is a microservices demo for customer profiles, driver assignment, shipment pricing, payments, and tracking.

## Local run

The default Compose stack runs the application services and connects them to PostgreSQL and Redis running on the host. Create a PostgreSQL database named `fedb`; the local defaults are username `postgres` and password `postgres`. Replace the OpenRouteService API-key placeholder in `docker-compose/default/docker-compose.yml` if you want route-based quotes to work. Config Server also needs access to its configured Git repository.

Start the stack from the repository root:

```powershell
docker compose -f docker-compose/default/docker-compose.yml up -d --build
```

The API gateway is at `http://localhost:8088`. JWT authentication is handled by the user service; there is no Keycloak service. The local HS256 signing secret is configured in the user service, gateway settings, and default Compose file. It is a development value; change it before deploying anywhere shared.

## Register and use a JWT

Register a customer with `POST http://localhost:8088/api/v1/auth/register`:

```json
{
  "firstname": "Demo",
  "lastname": "Customer",
  "email": "demo.customer@example.com",
  "mobileNumber": "01012345678",
  "password": "DemoPassword123!",
  "preferredLanguage": "en",
  "deliveryAddress": "Nasr City, Cairo"
}
```

Registration creates a `Customer` profile and returns an access token. The profile ID is the JWT subject. Passwords are stored as BCrypt hashes. Tokens expire after 60 minutes by default. To get another token, call `POST /api/v1/auth/login` with the same email and password. Send the token on protected requests as `Authorization: Bearer <access-token>`.

The gateway reads roles from the token. Public registration can only create `ROLE_CUSTOMER`; it cannot assign admin or driver roles. For local admin testing, register a customer, promote that profile in PostgreSQL with `UPDATE users SET role = 'ROLE_ADMIN' WHERE email = 'demo.customer@example.com';`, then log in again to get a token with the updated role. Pricing configuration endpoints require `ROLE_ADMIN`; other protected routes require a valid token.

Shipment requests derive `customerId` from the verified JWT subject, so clients should omit it from `POST /api/v1/shipments/request`. `GET /api/v1/shipments/all` likewise uses the signed-in customer's profile ID.

## Postman

Import [Fastlivery-Express-JWT.postman_collection.json](Fastlivery-Express-JWT.postman_collection.json). Run **Authentication → Register Customer** once, or **Authentication → Login** for an existing account. The collection scripts save the returned token and customer profile ID; subsequent requests use the token automatically. Use unique email and mobile values when registering again.

Start with **Gateway → Health (public)**, then try **Gateway → Observability Ping** and **Shipment → Request Shipment Quote and Assign Driver**. The shipment flow also needs an available driver with a location, PostgreSQL, Redis, and a valid OpenRouteService key. Stripe checkout requires Stripe configuration.

To stop the stack:

```powershell
docker compose -f docker-compose/default/docker-compose.yml down
```
