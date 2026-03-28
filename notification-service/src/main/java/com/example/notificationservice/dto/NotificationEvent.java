package com.example.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationEvent {
    private String type;      // ORDER_CREATED, ORDER_SHIPPED, etc.
    private String recipient;  // email address or phone number
    private String subject;
    private String body;
}
