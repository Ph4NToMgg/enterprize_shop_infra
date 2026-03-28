package com.example.orderservice.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderSummary {
    private UUID orderId;
    private String userEmail;
    private String status;
    private BigDecimal totalAmount;
    private LocalDateTime createdAt;
    private List<OrderItemSummary> items;

    public OrderSummary() {}
    public OrderSummary(UUID orderId, String userEmail, String status, BigDecimal totalAmount,
                        LocalDateTime createdAt, List<OrderItemSummary> items) {
        this.orderId = orderId;
        this.userEmail = userEmail;
        this.status = status;
        this.totalAmount = totalAmount;
        this.createdAt = createdAt;
        this.items = items;
    }

    public UUID getOrderId() { return orderId; }
    public void setOrderId(UUID orderId) { this.orderId = orderId; }
    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BigDecimal getTotalAmount() { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<OrderItemSummary> getItems() { return items; }
    public void setItems(List<OrderItemSummary> items) { this.items = items; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID orderId;
        private String userEmail;
        private String status;
        private BigDecimal totalAmount;
        private LocalDateTime createdAt;
        private List<OrderItemSummary> items;

        public Builder orderId(UUID orderId) { this.orderId = orderId; return this; }
        public Builder userEmail(String userEmail) { this.userEmail = userEmail; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder totalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder items(List<OrderItemSummary> items) { this.items = items; return this; }

        public OrderSummary build() {
            return new OrderSummary(orderId, userEmail, status, totalAmount, createdAt, items);
        }
    }

    public static class OrderItemSummary {
        private String sku;
        private int quantity;
        private BigDecimal unitPrice;
        private BigDecimal subtotal;

        public OrderItemSummary() {}
        public OrderItemSummary(String sku, int quantity, BigDecimal unitPrice, BigDecimal subtotal) {
            this.sku = sku;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subtotal = subtotal;
        }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
        public BigDecimal getSubtotal() { return subtotal; }
        public void setSubtotal(BigDecimal subtotal) { this.subtotal = subtotal; }

        public static ItemBuilder builder() { return new ItemBuilder(); }

        public static class ItemBuilder {
            private String sku;
            private int quantity;
            private BigDecimal unitPrice;
            private BigDecimal subtotal;

            public ItemBuilder sku(String sku) { this.sku = sku; return this; }
            public ItemBuilder quantity(int quantity) { this.quantity = quantity; return this; }
            public ItemBuilder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }
            public ItemBuilder subtotal(BigDecimal subtotal) { this.subtotal = subtotal; return this; }

            public OrderItemSummary build() {
                return new OrderItemSummary(sku, quantity, unitPrice, subtotal);
            }
        }
    }
}
