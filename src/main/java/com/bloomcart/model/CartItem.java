package com.bloomcart.model;

import java.io.Serializable;
import java.math.BigDecimal;

public class CartItem implements Serializable {
    private static final long serialVersionUID = 1L;

    private int flowerId;
    private String flowerName;
    private String imageName;
    private BigDecimal unitPrice;
    private int quantity;

    public CartItem() {}

    public CartItem(int flowerId, String flowerName, String imageName, BigDecimal unitPrice, int quantity) {
        this.flowerId = flowerId;
        this.flowerName = flowerName;
        this.imageName = imageName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public int getFlowerId() { return flowerId; }
    public void setFlowerId(int flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public BigDecimal getSubtotal() {
        return unitPrice == null ? BigDecimal.ZERO : unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
