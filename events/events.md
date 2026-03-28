# Event Contracts & Versioning Rules

## Kafka Topics

| Topic | Key | Value Schema | Producers | Consumers |
|---|---|---|---|---|
| `order.events` | orderId (UUID) | `OrderCreated.v1.json` / `OrderCancelled.v1.json` | order-service | notification-service |

## Versioning Strategy

- Use **semantic versioning** for event schemas: `EventName.vN.json`
- New **minor** fields: add as **optional** fields (backward compatible)
- **Breaking** changes: create a new version (e.g., `OrderCreated.v2.json`)
- Consumers MUST ignore unknown fields (forward compatibility)
- Never remove or rename required fields within the same version

## Compatibility Rules

1. **Backward compatible** changes (no new version needed):
   - Adding optional fields
   - Adding new event types to a topic

2. **Breaking** changes (new version required):
   - Removing or renaming fields
   - Changing field types
   - Changing required/optional status of existing fields

## Idempotency

- Consumers MUST implement idempotency using `orderId` as the deduplication key
- notification-service uses an in-memory `ConcurrentHashMap` (replace with DB table for production)
