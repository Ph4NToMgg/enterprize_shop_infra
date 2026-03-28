package com.example.inventoryservice.dto;

public class ReservationResponse {
    private String sku;
    private boolean reserved;
    private String correlationId;
    private String message;

    public ReservationResponse() {}
    public ReservationResponse(String sku, boolean reserved, String correlationId, String message) {
        this.sku = sku;
        this.reserved = reserved;
        this.correlationId = correlationId;
        this.message = message;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public boolean isReserved() { return reserved; }
    public void setReserved(boolean reserved) { this.reserved = reserved; }
    public String getCorrelationId() { return correlationId; }
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String sku;
        private boolean reserved;
        private String correlationId;
        private String message;

        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder reserved(boolean reserved) { this.reserved = reserved; return this; }
        public Builder correlationId(String correlationId) { this.correlationId = correlationId; return this; }
        public Builder message(String message) { this.message = message; return this; }

        public ReservationResponse build() {
            return new ReservationResponse(sku, reserved, correlationId, message);
        }
    }
}
