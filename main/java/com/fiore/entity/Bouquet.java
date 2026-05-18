package com.fiore.entity;

import java.time.LocalDateTime;

/**
 * Bouquet - Entity representing a flower bouquet available in the shop.
 */
public class Bouquet {

    // ── Fields ─────────────────────────────────────────────────────────────
    private int           id;
    private String        name;
    private String        description;
    private double        price;
    private int           stockQuantity;
    private String        category;     // e.g. "Real Flowers", "Artificial Flowers", "Mixed Bouquets", "Seasonal"
    private String        occasion;     // e.g. "Wedding", "Birthday", "Anniversary"
    private String        imagePath;    // relative path under /static/images/bouquets/
    private boolean       featured;
    private LocalDateTime createdAt;

    // ── Constructors ───────────────────────────────────────────────────────

    public Bouquet() {}

    public Bouquet(String name, String description, double price,
                   int stockQuantity, String category, String occasion,
                   String imagePath, boolean featured) {
        this.name          = name;
        this.description   = description;
        this.price         = price;
        this.stockQuantity = stockQuantity;
        this.category      = category;
        this.occasion      = occasion;
        this.imagePath     = imagePath;
        this.featured      = featured;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getId()                           { return id; }
    public void setId(int id)                    { this.id = id; }

    public String getName()                      { return name; }
    public void setName(String name)             { this.name = name; }

    public String getDescription()               { return description; }
    public void setDescription(String d)         { this.description = d; }

    public double getPrice()                     { return price; }
    public void setPrice(double price)           { this.price = price; }

    public int getStockQuantity()                { return stockQuantity; }
    public void setStockQuantity(int q)          { this.stockQuantity = q; }

    public String getCategory()                  { return category; }
    public void setCategory(String category)     { this.category = category; }

    public String getOccasion()                  { return occasion; }
    public void setOccasion(String occasion)     { this.occasion = occasion; }

    public String getImagePath()                 { return imagePath; }
    public void setImagePath(String imagePath)   { this.imagePath = imagePath; }

    public boolean isFeatured()                  { return featured; }
    public void setFeatured(boolean featured)    { this.featured = featured; }

    public LocalDateTime getCreatedAt()          { return createdAt; }
    public void setCreatedAt(LocalDateTime t)    { this.createdAt = t; }

    // ── Helpers ────────────────────────────────────────────────────────────

    public boolean isInStock() { return stockQuantity > 0; }

    /** Returns the price formatted as "NPR X,XXX" */
    public String getFormattedPrice() {
        return String.format("NPR %,.0f", price);
    }

    @Override
    public String toString() {
        return "Bouquet{id=" + id + ", name=" + name + ", price=" + price + "}";
    }
}
