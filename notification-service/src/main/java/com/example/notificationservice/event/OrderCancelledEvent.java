package com.example.notificationservice.event;

import lombok.*;
import java.time.Instant;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class OrderCancelledEvent {
    private UUID orderId;
    private String userEmail;
    private String reason;
    private Instant cancelledAt;
}
