package com.example.orderservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.List;

public class CreateOrderRequest {
    @NotBlank(message = "User email is required")
    private String userEmail;

    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemRequest> items;

    public CreateOrderRequest() {}
    public CreateOrderRequest(String userEmail, List<OrderItemRequest> items) {
        this.userEmail = userEmail;
        this.items = items;
    }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
    public List<OrderItemRequest> getItems() { return items; }
    public void setItems(List<OrderItemRequest> items) { this.items = items; }

    public static class OrderItemRequest {
        @NotBlank(message = "SKU is required")
        private String sku;

        @Positive(message = "Quantity must be positive")
        private int quantity;

        @Positive(message = "Unit price must be positive")
        private BigDecimal unitPrice;

        public OrderItemRequest() {}
        public OrderItemRequest(String sku, int quantity, BigDecimal unitPrice) {
            this.sku = sku;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
        }

        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        public BigDecimal getUnitPrice() { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
    }
}
