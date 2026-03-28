package com.example.inventoryservice.dto;

public class InventoryDto {
    private String sku;
    private int available;

    public InventoryDto() {}
    public InventoryDto(String sku, int available) {
        this.sku = sku;
        this.available = available;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String sku;
        private int available;

        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder available(int available) { this.available = available; return this; }

        public InventoryDto build() { return new InventoryDto(sku, available); }
    }
}
