package com.example.notificationservice.service;

import com.example.notificationservice.event.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Simulated email service — logs emails instead of actually sending them.
 * In production, wire up JavaMailSender.
 *
 * Lombok-free for IDE language-server compatibility.
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    public void sendOrderConfirmation(String toEmail, String orderId,
                                       String totalAmount,
                                       List<OrderCreatedEvent.OrderItemEvent> items) {
        log.info("=== SENDING EMAIL (simulated) ===");
        log.info("To: {}", toEmail);
        log.info("Subject: Order Confirmation - #{}", orderId);
        log.info("Body:");
        log.info("  Thank you for your order!");
        log.info("  Order ID: {}", orderId);
        log.info("  Total: ${}", totalAmount);
        if (items != null) {
            log.info("  Items:");
            items.forEach(item ->
                    log.info("    - {} x{} @ ${}", item.getSku(), item.getQuantity(), item.getPrice()));
        }
        log.info("=== EMAIL SENT (simulated) ===");
    }
}
