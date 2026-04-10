package com.example.inventoryservice.dto;

public class InventoryDto {
    private String sku;
    private int available;
    private int availableQuantity;
    private int reservedQuantity;

    public InventoryDto() {}
    public InventoryDto(String sku, int available, int availableQuantity, int reservedQuantity) {
        this.sku = sku;
        this.available = available;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
    }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getAvailable() { return available; }
    public void setAvailable(int available) { this.available = available; }
    public int getAvailableQuantity() { return availableQuantity; }
    public void setAvailableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; }
    public int getReservedQuantity() { return reservedQuantity; }
    public void setReservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String sku;
        private int available;
        private int availableQuantity;
        private int reservedQuantity;

        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder available(int available) { this.available = available; return this; }
        public Builder availableQuantity(int availableQuantity) { this.availableQuantity = availableQuantity; return this; }
        public Builder reservedQuantity(int reservedQuantity) { this.reservedQuantity = reservedQuantity; return this; }

        public InventoryDto build() { return new InventoryDto(sku, available, availableQuantity, reservedQuantity); }
    }
}
