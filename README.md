# ordered-config-server

[![CI](https://github.com/ordered-system/ordered-config-server/actions/workflows/ci.yml/badge.svg)](https://github.com/ordered-system/ordered-config-server/actions/workflows/ci.yml)

Centralized configuration for [ordered-system](https://github.com/ordered-system), built on Spring Cloud Config Server (native profile — config is bundled as a classpath resource rather than pulled from a separate Git repo, which is simpler for a project of this size).

## Why it exists

The main thing every service needs but shouldn't each configure independently is the **JWT signing secret**: `ordered-gateway` verifies incoming tokens, `ordered-user-service` issues them, and both need to agree on the exact same key. Instead of duplicating `JWT_SECRET` across services' environment variables, only `ordered-config-server` needs it set — every other service fetches it from here on startup via `spring.config.import: optional:configserver:...`.

```yaml
# what it currently overrides for every consuming service
app.jwt.secret: ${JWT_SECRET}
ordered.gateway.security.jwt-secret: ${JWT_SECRET}
```

## Stack

Java 21 · Spring Boot 4.1.0 · Spring Cloud Config Server (native profile)

## Running it

This is the very first thing to start, before eureka and before any business service.

```bash
JWT_SECRET=some-dev-secret-at-least-256-bits-long ./mvnw spring-boot:run
```

Runs on **port 8888** (override with `PORT`). If `JWT_SECRET` isn't set, it falls back to an obviously-fake placeholder locally (`change-me-in-prod-min-256-bits-long-please-replace`) — fine for local dev, never for anything public-facing.

Sanity check it's serving config correctly:

```bash
curl http://localhost:8888/order-service/default
```

### Docker

```bash
docker build -t ordered-config-server .
docker run -p 8888:8888 -e JWT_SECRET=... ordered-config-server
```

## Testing

```bash
make test
```

## Where this fits

Part of the [ordered-system](https://github.com/ordered-system) organization. See [ordered-infra](https://github.com/ordered-system/ordered-infra) to run the whole platform together, including production secret handling via `.env.prod`.

## License

MIT — see [LICENSE](LICENSE).
