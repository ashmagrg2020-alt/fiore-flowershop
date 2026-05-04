package com.bloomcart.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class WishlistItem {
    private int id;
    private int userId;
    private int flowerId;
    private String flowerName;
    private String imageName;
    private BigDecimal price;
    private boolean inStock;
    private LocalDateTime addedAt;

    public WishlistItem() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public int getFlowerId() { return flowerId; }
    public void setFlowerId(int flowerId) { this.flowerId = flowerId; }

    public String getFlowerName() { return flowerName; }
    public void setFlowerName(String flowerName) { this.flowerName = flowerName; }

    public String getImageName() { return imageName; }
    public void setImageName(String imageName) { this.imageName = imageName; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public boolean isInStock() { return inStock; }
    public void setInStock(boolean inStock) { this.inStock = inStock; }

    public LocalDateTime getAddedAt() { return addedAt; }
    public void setAddedAt(LocalDateTime addedAt) { this.addedAt = addedAt; }
}
