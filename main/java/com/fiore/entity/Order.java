package com.fiore.entity;

import java.time.LocalDateTime;

/**
 * Order - Entity representing a bouquet purchase order placed by a user.
 */
public class Order {

    // ── Fields ─────────────────────────────────────────────────────────────
    private int           id;
    private int           userId;
    private int           bouquetId;
    private int           quantity;
    private double        totalPrice;
    private String        status;       // "pending" | "confirmed" | "delivered" | "cancelled"
    private String        specialNote;
    private LocalDateTime orderedAt;

    // Joined fields (for display — populated by DAO queries with JOIN)
    private String        userName;
    private String        bouquetName;
    private String        bouquetImage;

    // ── Constructors ───────────────────────────────────────────────────────

    public Order() {}

    public Order(int userId, int bouquetId, int quantity,
                 double totalPrice, String status, String specialNote) {
        this.userId      = userId;
        this.bouquetId   = bouquetId;
        this.quantity    = quantity;
        this.totalPrice  = totalPrice;
        this.status      = status;
        this.specialNote = specialNote;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getId()                           { return id; }
    public void setId(int id)                    { this.id = id; }

    public int getUserId()                       { return userId; }
    public void setUserId(int userId)            { this.userId = userId; }

    public int getBouquetId()                    { return bouquetId; }
    public void setBouquetId(int bouquetId)      { this.bouquetId = bouquetId; }

    public int getQuantity()                     { return quantity; }
    public void setQuantity(int quantity)        { this.quantity = quantity; }

    public double getTotalPrice()                { return totalPrice; }
    public void setTotalPrice(double t)          { this.totalPrice = t; }

    public String getStatus()                    { return status; }
    public void setStatus(String status)         { this.status = status; }

    public String getSpecialNote()               { return specialNote; }
    public void setSpecialNote(String n)         { this.specialNote = n; }

    public LocalDateTime getOrderedAt()          { return orderedAt; }
    public void setOrderedAt(LocalDateTime t)    { this.orderedAt = t; }

    public String getUserName()                  { return userName; }
    public void setUserName(String n)            { this.userName = n; }

    public String getBouquetName()               { return bouquetName; }
    public void setBouquetName(String n)         { this.bouquetName = n; }

    public String getBouquetImage()              { return bouquetImage; }
    public void setBouquetImage(String i)        { this.bouquetImage = i; }

    // ── Helpers ────────────────────────────────────────────────────────────

    public String getFormattedTotal() {
        return String.format("NPR %,.0f", totalPrice);
    }

    @Override
    public String toString() {
        return "Order{id=" + id + ", userId=" + userId
             + ", bouquetId=" + bouquetId + ", status=" + status + "}";
    }
}
