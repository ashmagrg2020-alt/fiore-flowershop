package com.fiore.service;

import com.fiore.dao.BouquetDAO;
import com.fiore.dao.OrderDAO;
import com.fiore.entity.Bouquet;
import com.fiore.entity.Order;

import java.util.List;

/**
 * OrderService - Business logic for order placement and management.
 */
public class OrderService {

    private final OrderDAO   orderDAO   = new OrderDAO();
    private final BouquetDAO bouquetDAO = new BouquetDAO();

    // ── Place order ────────────────────────────────────────────────────────

    /**
     * Places a new order after validating stock.
     *
     * @return null on success, or an error message
     */
    public String placeOrder(int userId, int bouquetId, int quantity, String note) {
        if (quantity <= 0) return "Quantity must be at least 1.";

        Bouquet b = bouquetDAO.findById(bouquetId);
        if (b == null) return "Bouquet not found.";
        if (!b.isInStock()) return "This bouquet is currently out of stock.";
        if (b.getStockQuantity() < quantity)
            return "Only " + b.getStockQuantity() + " unit(s) available.";

        double total = b.getPrice() * quantity;
        Order o = new Order(userId, bouquetId, quantity, total, "pending", note);

        // Reduce stock
        b.setStockQuantity(b.getStockQuantity() - quantity);
        bouquetDAO.updateBouquet(b);

        return orderDAO.insertOrder(o) > 0
               ? null : "Failed to place order. Please try again.";
    }

    // ── Read ───────────────────────────────────────────────────────────────

    public List<Order> getUserOrders(int userId) { return orderDAO.findByUserId(userId); }
    public List<Order> getAllOrders()            { return orderDAO.findAll(); }
    public Order       getById(int id)          { return orderDAO.findById(id); }
    public int         countOrders()            { return orderDAO.countOrders(); }
    public double      totalRevenue()           { return orderDAO.totalRevenue(); }

    // ── Update ─────────────────────────────────────────────────────────────

    public boolean updateStatus(int orderId, String status) {
        return orderDAO.updateStatus(orderId, status);
    }
}
