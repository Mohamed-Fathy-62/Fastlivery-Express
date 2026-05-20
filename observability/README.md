# Fastlivery Observability

Local stack:

- Prometheus: metrics scraping from Spring Boot `/actuator/prometheus`
- Loki: centralized log storage
- Promtail: ships Docker container logs to Loki
- Grafana: dashboards and exploration UI

Run from the repository root:

```powershell
docker compose -f docker-compose/default/docker-compose.yml up -d prometheus loki promtail grafana
```

If you want to run the full application plus observability stack:

```powershell
docker compose -f docker-compose/default/docker-compose.yml up -d --build
```

The first run builds the local Spring Boot images from these folders:

- `configserver`
- `eurekaserver`
- `gatewayserver`
- `user`
- `shipment`
- `pricing`

Open:

- Grafana: http://localhost:3000
- Prometheus: http://localhost:9090
- Loki: http://localhost:3100/ready

Default Grafana login:

```text
admin / admin
```

Useful Prometheus queries:

```promql
up
http_server_requests_seconds_count
jvm_memory_used_bytes
process_cpu_usage
```

Useful Loki query:

```logql
{service=~"users|shipments|pricing|gatewayserver|configserver|eurekaserver"}
```

Grafana provisions a starter dashboard automatically:

```text
Dashboards -> Fastlivery -> Fastlivery Overview
```

## Quick test

Check containers:

```powershell
docker compose -f docker-compose/default/docker-compose.yml ps
```

Generate one gateway request and one clear log line:

```powershell
curl "http://localhost:8088/api/observability/ping?message=hello-grafana"
```

Verify Prometheus can scrape targets:

1. Open http://localhost:9090/targets
2. Confirm the Fastlivery jobs are `UP`
3. Open http://localhost:9090/query
4. Run:

```promql
up{job=~"fastlivery-.*"}
```

After hitting the ping endpoint a few times, test request metrics:

```promql
sum by (application) (rate(http_server_requests_seconds_count[1m]))
```

Verify logs in Grafana:

1. Open http://localhost:3000 and sign in with `admin` / `admin`
2. Go to `Explore`
3. Select the `Loki` data source
4. Run:

```logql
{service="gatewayserver"} |= "Observability ping received"
```

You can also query Loki directly:

```powershell
curl -G "http://localhost:3100/loki/api/v1/query_range" --data-urlencode "query={service=\"gatewayserver\"} |= \"Observability ping received\"" --data-urlencode "limit=20"
```
