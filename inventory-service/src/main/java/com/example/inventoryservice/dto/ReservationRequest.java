package com.example.inventoryservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ReservationRequest {
    @NotBlank(message = "SKU is required")
    private String sku;

    @Positive(message = "Quantity must be positive")
    private int quantity;

    private String correlationId;

    public ReservationRequest() {}
    public ReservationRequest(String sku, int quantity, String correlationId) {
        this.sku = sku;
        this.quantity = quantity;
        this.correlationId = correlationId;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
}
