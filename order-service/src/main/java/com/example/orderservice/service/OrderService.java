package com.example.orderservice.service;

import com.example.orderservice.client.InventoryClient;
import com.example.orderservice.dto.CreateOrderRequest;
import com.example.orderservice.dto.OrderSummary;
import com.example.orderservice.entity.OrderEntity;
import com.example.orderservice.entity.OrderItemEntity;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.repository.OrderRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final InventoryClient inventoryClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "order.events";

    public OrderService(OrderRepository orderRepository,
                        InventoryClient inventoryClient,
                        KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.inventoryClient = inventoryClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    public OrderSummary createOrder(CreateOrderRequest request) {
        String correlationId = UUID.randomUUID().toString();
        log.info("Creating order for {} with correlationId={}", request.getUserEmail(), correlationId);

        // 1. Reserve inventory for each item
        for (CreateOrderRequest.OrderItemRequest item : request.getItems()) {
            InventoryClient.ReservationResult result = inventoryClient.reserve(
                    item.getSku(), item.getQuantity(), correlationId);
            if (!result.isReserved()) {
                throw new RuntimeException("Inventory reservation failed for SKU " +
                        item.getSku() + ": " + result.getMessage());
            }
        }

        // 2. Build and save order
        BigDecimal total = request.getItems().stream()
                .map(i -> i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        OrderEntity order = OrderEntity.builder()
                .userEmail(request.getUserEmail())
                .status("CONFIRMED")
                .totalAmount(total)
                .build();

        List<OrderItemEntity> items = request.getItems().stream()
                .map(i -> OrderItemEntity.builder()
                        .order(order)
                        .sku(i.getSku())
                        .quantity(i.getQuantity())
                        .unitPrice(i.getUnitPrice())
                        .build())
                .toList();
        order.setItems(items);

        OrderEntity saved = orderRepository.save(order);

        // 3. Publish Kafka event
        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .orderId(saved.getId())
                .userEmail(saved.getUserEmail())
                .totalAmount(saved.getTotalAmount())
                .createdAt(Instant.now())
                .items(saved.getItems().stream()
                        .map(i -> OrderCreatedEvent.OrderItemEvent.builder()
                                .sku(i.getSku())
                                .quantity(i.getQuantity())
                                .price(i.getUnitPrice())
                                .build())
                        .toList())
                .build();

        kafkaTemplate.send(TOPIC, saved.getId().toString(), event);
        log.info("Published OrderCreatedEvent for orderId={}", saved.getId());

        // 4. Build summary
        return toSummary(saved);
    }

    @Transactional(readOnly = true)
    public OrderSummary getOrder(UUID orderId) {
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found: " + orderId));
        return toSummary(order);
    }

    private OrderSummary toSummary(OrderEntity order) {
        return OrderSummary.builder()
                .orderId(order.getId())
                .userEmail(order.getUserEmail())
                .status(order.getStatus())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .items(order.getItems().stream()
                        .map(i -> OrderSummary.OrderItemSummary.builder()
                                .sku(i.getSku())
                                .quantity(i.getQuantity())
                                .unitPrice(i.getUnitPrice())
                                .subtotal(i.getUnitPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                                .build())
                        .toList())
                .build();
    }
}
