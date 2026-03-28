package com.example.orderservice.client;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

    private final RestTemplate restTemplate;
    private final String inventoryBaseUrl;

    public InventoryClient(
            RestTemplate restTemplate,
            @Value("${inventory-service.url:http://localhost:8083}") String inventoryBaseUrl) {
        this.restTemplate = restTemplate;
        this.inventoryBaseUrl = inventoryBaseUrl;
    }

    @CircuitBreaker(name = "inventory", fallbackMethod = "reserveFallback")
    @Retry(name = "inventory")
    public ReservationResult reserve(String sku, int quantity, String correlationId) {
        ReserveRequest body = new ReserveRequest(sku, quantity, correlationId);
        ReserveResponse response = restTemplate.postForObject(
                inventoryBaseUrl + "/api/inventory/reserve",
                body,
                ReserveResponse.class
        );
        if (response != null && response.isReserved()) {
            return new ReservationResult(true, response.getMessage());
        }
        return new ReservationResult(false, response != null ? response.getMessage() : "No response");
    }

    public ReservationResult reserveFallback(String sku, int quantity, String correlationId, Exception ex) {
        log.warn("Circuit breaker fallback for inventory reserve sku={}: {}", sku, ex.getMessage());
        return new ReservationResult(false, "Inventory service unavailable: " + ex.getMessage());
    }

    public static class ReservationResult {
        private final boolean reserved;
        private final String message;

        public ReservationResult(boolean reserved, String message) {
            this.reserved = reserved;
            this.message = message;
        }

        public boolean isReserved() { return reserved; }
        public String getMessage() { return message; }
    }

    static class ReserveRequest {
        private String sku;
        private int quantity;
        private String correlationId;

        public ReserveRequest() {}
        public ReserveRequest(String sku, int quantity, String correlationId) {
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

    static class ReserveResponse {
        private String sku;
        private boolean reserved;
        private String correlationId;
        private String message;

        public ReserveResponse() {}
        public String getSku() { return sku; }
        public void setSku(String sku) { this.sku = sku; }
        public boolean isReserved() { return reserved; }
        public void setReserved(boolean reserved) { this.reserved = reserved; }
        public String getCorrelationId() { return correlationId; }
        public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}
