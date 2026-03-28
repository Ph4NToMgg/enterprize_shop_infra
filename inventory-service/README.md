# inventory-service

**Files to edit for business logic:**
- `entity/InventoryEntity.java` — Add warehouse location, batch tracking
- `service/impl/InventoryServiceImpl.java` — Add optimistic locking, Kafka event listeners for order events
- `controller/InventoryController.java` — Add bulk stock update endpoint
