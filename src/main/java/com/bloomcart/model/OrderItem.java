package com.bloomcart.model;

import java.math.BigDecimal;

public class OrderItem {
    private int id;
    private int orderId;
    private int flowerId;
    private String flowerName;
    private String flowerImage;
    private int quantity;
    private BigDecimal unitPrice;

    public OrderItem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrderId() { return orderId; }
    public void setOrderId(int orderId) { this.orderId = orderId; }

    public int getFlowerId() { return flowerId; }
    public void setFlowerId(int flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public String getFlowerImage() { return flowerImage; }
    public void setFlowerImage(String flowerImage) { this.flowerImage = flowerImage; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public BigDecimal getSubtotal() {
        return unitPrice == null ? BigDecimal.ZERO : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
