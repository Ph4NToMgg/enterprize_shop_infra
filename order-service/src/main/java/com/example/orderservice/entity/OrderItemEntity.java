package com.example.orderservice.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "order_items")
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @Column(nullable = false)
    private String sku;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    public OrderItemEntity() {}

    public OrderItemEntity(UUID id, OrderEntity order, String sku, int quantity, BigDecimal unitPrice) {
        this.id = id;
        this.order = order;
        this.sku = sku;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public OrderEntity getOrder() { return order; }
    public void setOrder(OrderEntity order) { this.order = order; }
    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private UUID id;
        private OrderEntity order;
        private String sku;
        private int quantity;
        private BigDecimal unitPrice;

        public Builder id(UUID id) { this.id = id; return this; }
        public Builder order(OrderEntity order) { this.order = order; return this; }
        public Builder sku(String sku) { this.sku = sku; return this; }
        public Builder quantity(int quantity) { this.quantity = quantity; return this; }
        public Builder unitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; return this; }

        public OrderItemEntity build() {
            return new OrderItemEntity(id, order, sku, quantity, unitPrice);
        }
    }
}
