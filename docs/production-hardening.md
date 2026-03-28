# Production Hardening Checklist

## Security
- [ ] Rotate JWT secret — use strong 256+ bit key from secrets manager
- [ ] Enable HTTPS/TLS termination at load balancer
- [ ] Implement OWASP headers (X-Content-Type-Options, CSP, HSTS)
- [ ] Enable CORS whitelisting (not *)
- [ ] Add rate limiting at API Gateway
- [ ] Run OWASP Dependency-Check: `mvn org.owasp:dependency-check-maven:check`
- [ ] Enable secrets scanning in CI (e.g., trufflehog, gitleaks)
- [ ] Validate all input (already done via `@Valid`)
- [ ] Use parameterized queries (JPA handles this)

## Database
- [ ] Use connection pooling (HikariCP — already default in Spring Boot)
- [ ] Set appropriate pool sizes per service
- [ ] Enable SSL for PostgreSQL connections
- [ ] Create read replicas for high-read services (product-service)
- [ ] Set up automated backups

## Observability
- [ ] Configure Prometheus scrape targets for all services
- [ ] Set up Grafana dashboards: JVM, HTTP, Kafka consumer lag
- [ ] Configure alerting (PagerDuty/Slack) for error rates, p99 latency
- [ ] Enable distributed tracing end-to-end (API Gateway → services)
- [ ] Set up structured logging with ELK/Loki

## Kafka
- [ ] Set `acks=all` for order events producer
- [ ] Configure Dead Letter Topic for failed messages
- [ ] Set appropriate retention policies
- [ ] Monitor consumer lag
- [ ] Enable idempotent producer (`enable.idempotence=true`)

## Resilience
- [ ] Tune Resilience4j circuit breaker thresholds
- [ ] Add bulkhead patterns for thread isolation
- [ ] Configure timeouts for all inter-service calls
- [ ] Implement health checks for downstream dependencies
- [ ] Add retry with exponential backoff

## Deployment
- [ ] Use multi-stage Docker builds (already done)
- [ ] Run containers as non-root user
- [ ] Set resource limits (CPU/memory) in k8s manifests
- [ ] Configure liveness/readiness probes (actuator endpoints ready)
- [ ] Use rolling deployments with zero downtime
- [ ] Set up staging environment

## Performance
- [ ] Run k6 load tests before each release
- [ ] Profile JVM with async-profiler in staging
- [ ] Enable Redis caching for inventory-service (commented in config)
- [ ] Optimize database indexes based on query patterns
- [ ] Consider Spring Boot Native Image for cold start optimization
