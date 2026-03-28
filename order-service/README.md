# order-service

**Files to edit for business logic:**
- `entity/OrderEntity.java` — Add payment ref, shipping address, notes
- `entity/OrderItemEntity.java` — Add product snapshot (name, image at order time)
- `service/impl/OrderServiceImpl.java` — Wire Kafka producer for order events, integrate inventory reservation
- `controller/OrderController.java` — Add pagination, admin endpoints
- Add `config/KafkaProducerConfig.java`
