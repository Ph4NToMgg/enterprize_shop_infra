package com.example.notificationservice.consumer;

import com.example.notificationservice.event.OrderCreatedEvent;
import com.example.notificationservice.service.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Kafka consumer for order events.
 * Uses an in-memory idempotency store (Set) to skip duplicate events.
 * In production, use a DB table for persistent idempotency.
 *
 * Lombok-free for IDE language-server compatibility.
 */
@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    private final EmailService emailService;

    // Simple in-memory idempotency store; replace with DB in production
    private final Set<UUID> processedOrderIds = ConcurrentHashMap.newKeySet();

    public OrderEventConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(topics = "order.events", groupId = "notification-group")
    public void handleOrderCreated(OrderCreatedEvent event) {
        if (event.getOrderId() == null) {
            log.warn("Received OrderCreatedEvent with null orderId, skipping");
            return;
        }

        if (!processedOrderIds.add(event.getOrderId())) {
            log.info("Duplicate OrderCreatedEvent for orderId={}, skipping", event.getOrderId());
            return;
        }

        log.info("Processing OrderCreatedEvent: orderId={}, userEmail={}, total={}",
                event.getOrderId(), event.getUserEmail(), event.getTotalAmount());

        emailService.sendOrderConfirmation(
                event.getUserEmail(),
                event.getOrderId().toString(),
                event.getTotalAmount().toPlainString(),
                event.getItems()
        );
    }
}
