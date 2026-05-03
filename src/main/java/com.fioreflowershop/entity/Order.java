package com.fioreflowershop.model;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

/**
 * Order - Model class representing a customer order.
 * Maps to the 'orders' table in the database.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class Order {

    private int           orderId;
    private int           userId;
    private String        userFullName;
    private BigDecimal    totalAmount;
    private String        deliveryAddr;
    private String        orderStatus;   // PENDING, CONFIRMED, DELIVERED, CANCELLED
    private Timestamp     orderDate;
    private Date          deliveryDate;
    private String        notes;
    private List<OrderItem> items;

    // ===================== Constructors =====================

    public Order() {}

    // ===================== Getters & Setters =====================

    public int getOrderId()                             { return orderId; }
    public void setOrderId(int orderId)                 { this.orderId = orderId; }

    public int getUserId()                              { return userId; }
    public void setUserId(int userId)                   { this.userId = userId; }

    public String getUserFullName()                     { return userFullName; }
    public void setUserFullName(String name)            { this.userFullName = name; }

    public BigDecimal getTotalAmount()                  { return totalAmount; }
    public void setTotalAmount(BigDecimal totalAmount)  { this.totalAmount = totalAmount; }

    public String getDeliveryAddr()                     { return deliveryAddr; }
    public void setDeliveryAddr(String deliveryAddr)    { this.deliveryAddr = deliveryAddr; }

    public String getOrderStatus()                      { return orderStatus; }
    public void setOrderStatus(String orderStatus)      { this.orderStatus = orderStatus; }

    public Timestamp getOrderDate()                     { return orderDate; }
    public void setOrderDate(Timestamp orderDate)       { this.orderDate = orderDate; }

    public Date getDeliveryDate()                       { return deliveryDate; }
    public void setDeliveryDate(Date deliveryDate)      { this.deliveryDate = deliveryDate; }

    public String getNotes()                            { return notes; }
    public void setNotes(String notes)                  { this.notes = notes; }

    public List<OrderItem> getItems()                   { return items; }
    public void setItems(List<OrderItem> items)         { this.items = items; }

    // =========================================================

    /**
     * OrderItem - Inner class representing a single item in an order.
     * Maps to the 'order_items' table.
     */
    public static class OrderItem {

        private int        itemId;
        private int        orderId;
        private int        productId;
        private String     productName;
        private int        quantity;
        private BigDecimal unitPrice;

        public OrderItem() {}

        public int getItemId()                            { return itemId; }
        public void setItemId(int itemId)                 { this.itemId = itemId; }

        public int getOrderId()                           { return orderId; }
        public void setOrderId(int orderId)               { this.orderId = orderId; }

        public int getProductId()                         { return productId; }
        public void setProductId(int productId)           { this.productId = productId; }

        public String getProductName()                    { return productName; }
        public void setProductName(String productName)    { this.productName = productName; }

        public int getQuantity()                          { return quantity; }
        public void setQuantity(int quantity)             { this.quantity = quantity; }

        public BigDecimal getUnitPrice()                  { return unitPrice; }
        public void setUnitPrice(BigDecimal unitPrice)    { this.unitPrice = unitPrice; }

        /**
         * Calculates total price for this line item.
         * @return quantity * unitPrice
         */
        public BigDecimal getLineTotal() {
            return unitPrice.multiply(BigDecimal.valueOf(quantity));
        }
    }
}