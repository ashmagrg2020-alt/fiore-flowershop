package com.fioreflowershop.model;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 * Product - Model class representing a flower bouquet product.
 * Maps to the 'products' table in the database.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class Product {

    private int        productId;
    private String     productName;
    private String     sku;
    private int        categoryId;
    private String     categoryName;
    private Integer    occasionId;
    private String     occasionName;
    private String     description;
    private BigDecimal price;
    private int        stockQty;
    private String     imageUrl;
    private boolean    isFeatured;
    private String     status;      // ACTIVE, INACTIVE
    private Timestamp  createdAt;
    private Timestamp  updatedAt;

    // ===================== Constructors =====================

    public Product() {}

    public Product(String productName, String sku, int categoryId,
                   Integer occasionId, String description,
                   BigDecimal price, int stockQty) {
        this.productName = productName;
        this.sku         = sku;
        this.categoryId  = categoryId;
        this.occasionId  = occasionId;
        this.description = description;
        this.price       = price;
        this.stockQty    = stockQty;
        this.status      = "ACTIVE";
        this.isFeatured  = false;
    }

    // ===================== Getters & Setters =====================

    public int getProductId()                          { return productId; }
    public void setProductId(int productId)            { this.productId = productId; }

    public String getProductName()                     { return productName; }
    public void setProductName(String productName)     { this.productName = productName; }

    public String getSku()                             { return sku; }
    public void setSku(String sku)                     { this.sku = sku; }

    public int getCategoryId()                         { return categoryId; }
    public void setCategoryId(int categoryId)          { this.categoryId = categoryId; }

    public String getCategoryName()                    { return categoryName; }
    public void setCategoryName(String categoryName)   { this.categoryName = categoryName; }

    public Integer getOccasionId()                     { return occasionId; }
    public void setOccasionId(Integer occasionId)      { this.occasionId = occasionId; }

    public String getOccasionName()                    { return occasionName; }
    public void setOccasionName(String occasionName)   { this.occasionName = occasionName; }

    public String getDescription()                     { return description; }
    public void setDescription(String description)     { this.description = description; }

    public BigDecimal getPrice()                       { return price; }
    public void setPrice(BigDecimal price)             { this.price = price; }

    public int getStockQty()                           { return stockQty; }
    public void setStockQty(int stockQty)              { this.stockQty = stockQty; }

    public String getImageUrl()                        { return imageUrl; }
    public void setImageUrl(String imageUrl)           { this.imageUrl = imageUrl; }

    public boolean isFeatured()                        { return isFeatured; }
    public void setFeatured(boolean featured)          { this.isFeatured = featured; }

    public String getStatus()                          { return status; }
    public void setStatus(String status)               { this.status = status; }

    public Timestamp getCreatedAt()                    { return createdAt; }
    public void setCreatedAt(Timestamp createdAt)      { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt()                    { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt)      { this.updatedAt = updatedAt; }

    /**
     * Checks if the product is in stock.
     * @return true if stockQty > 0
     */
    public boolean isInStock() {
        return this.stockQty > 0;
    }

    @Override
    public String toString() {
        return "Product{productId=" + productId + ", name='" + productName +
                "', sku='" + sku + "', price=" + price + ", stock=" + stockQty + "}";
    }
}