Fastlivery is a scalable, cloud‑ready logistics 
and package delivery platform built using a modern microservices architecture. 
It provides end‑to‑end support for user management, shipment creation, 
driver assignment, real‑time tracking, pricing, notifications, and more.

## JWT authentication (local development)

The gateway validates OAuth 2.0 Bearer access tokens issued by the `fastlivery` Keycloak realm. It does not create an HTTP session or store authentication state in Redis. The default Docker Compose stack keeps the user, shipment, pricing, and notification service ports inside the Compose network; call those APIs through `http://localhost:8088`.

The default stack expects PostgreSQL (`fedb`) and Redis on the host at their default ports. Set your PostgreSQL password, a valid OpenRouteService key, and a local Keycloak administrator password before starting:

```powershell
$env:KEYCLOAK_ADMIN_PASSWORD = "choose-a-local-development-password"
$env:DB_PASSWORD = "your-local-postgres-password"
$env:ORS_API_KEY = "your-openrouteservice-api-key"
$env:RABBITMQ_PASSWORD = "choose-a-local-rabbitmq-password"
docker compose -f docker-compose/default/docker-compose.yml up -d --build
```

Keycloak is available at `http://localhost:9095`. Sign in to the admin console, create a test user in the imported `fastlivery` realm, and assign a realm role (`CUSTOMER`, `DRIVER`, or `ADMIN`). Configure a client to use Authorization Code with PKCE to obtain an access token. Send it to the gateway as `Authorization: Bearer <access-token>`. Pricing configuration endpoints require the `ADMIN` realm role; other API routes require a valid token.

For another identity provider, set `JWT_ISSUER_URI` to the exact issuer in the token's `iss` claim, `JWT_JWK_SET_URI` to that provider's JWK Set endpoint, and `JWT_AUDIENCE` to the API audience included in access tokens. Tokens must pass issuer, signature, expiry, and audience validation.

The local Keycloak administrator password is supplied through the environment and must be unique to local development. Do not reuse it outside this stack.

## Verify with Postman

1. Confirm PostgreSQL (`fedb`) and Redis are running on the host, then start the Compose stack with the environment variables above. The config server also needs access to its configured Git repository.
2. In Keycloak, select the `fastlivery` realm, create a test user, set a non-temporary password, and assign `CUSTOMER` or `ADMIN`.
3. Import [Fastlivery-Express-JWT.postman_collection.json](Fastlivery-Express-JWT.postman_collection.json) into Postman. Open the collection's **Authorization** tab, choose **Get New Access Token**, sign in with that user, and select **Use Token**.
4. Run **Gateway → Health (public)** first. Then run **Gateway → Observability Ping** and an authenticated request such as **Pricing → Calculate Detailed Quote**. The quote request also needs a valid `ORS_API_KEY` and reachable Redis/PostgreSQL.
5. To try the shipment request flow, create a driver with a unique email/mobile number, confirm the driver's location and availability, then run **Shipment → Request Shipment Quote and Assign Driver**. Payment and confirmation requests need valid Stripe configuration and a paid checkout session.

The QA and production Compose files are legacy stacks and do not include this Keycloak/JWT gateway setup. Use the default Compose file for this collection.
