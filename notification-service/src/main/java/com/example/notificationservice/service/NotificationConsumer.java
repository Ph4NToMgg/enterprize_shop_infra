package com.example.notificationservice.service;

import com.example.notificationservice.dto.NotificationEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationConsumer {

    private final EmailService emailService;

    @KafkaListener(topics = "order-events", groupId = "notification-group")
    public void consumeOrderEvent(String message) {
        log.info("Received order event: {}", message);
        // TODO: deserialize message → NotificationEvent, then dispatch
        // emailService.send(event.getRecipient(), event.getSubject(), event.getBody());
    }

    @KafkaListener(topics = "notification-events", groupId = "notification-group")
    public void consumeNotificationEvent(String message) {
        log.info("Received notification event: {}", message);
        // TODO: parse and route to email/SMS
    }
}
