package com.example.notificationservice.event;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

/**
 * Event published to Kafka when an order is created.
 * Lombok-free for IDE language-server compatibility.
 */
public class OrderCreatedEvent {
    private UUID orderId;
    private String userEmail;
    private BigDecimal totalAmount;
    private Instant createdAt;
    private List<OrderItemEvent> items;

    public OrderCreatedEvent() {}

    public OrderCreatedEvent(UUID orderId, String userEmail, BigDecimal totalAmount,
                             Instant createdAt, List<OrderItemEvent> items) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }

    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }

    public List<OrderItemEvent> getItems() { return items; }
    public void setItems(List<OrderItemEvent> items) { this.items = items; }

    // ── Builder ──────────────────────────────────────────────────────────────
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID orderId;
        private String userEmail;
        private BigDecimal totalAmount;
        private Instant createdAt;
        private List<OrderItemEvent> items;

        public Builder orderId(UUID orderId) { this.orderId = orderId; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder createdAt(Instant createdAt) { this.createdAt = createdAt; return this; }
        public Builder items(List<OrderItemEvent> items) { this.items = items; return this; }

        public OrderCreatedEvent build() {
            return new OrderCreatedEvent(orderId, userEmail, totalAmount, createdAt, items);
        }
    }

    // ── Nested event for each order line ─────────────────────────────────────
    public static class OrderItemEvent {
        private String sku;
        private int quantity;
        private BigDecimal price;

        public OrderItemEvent() {}

        public OrderItemEvent(String sku, int quantity, BigDecimal price) {
            this.sku = sku;
            this.quantity = quantity;
            this.price = price;
        }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public BigDecimal getPrice() { return price; }
        public void setPrice(BigDecimal price) { this.price = price; }

        public static Builder builder() { return new Builder(); }

        public static class Builder {
            private String sku;
            private int quantity;
            private BigDecimal price;

            public Builder sku(String sku) { this.sku = sku; return this; }
            public Builder quantity(int quantity) { this.quantity = quantity; return this; }
            public Builder price(BigDecimal price) { this.price = price; return this; }

            public OrderItemEvent build() {
                return new OrderItemEvent(sku, quantity, price);
            }
        }
    }
}
