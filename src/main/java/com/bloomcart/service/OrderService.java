package com.bloomcart.service;

import com.bloomcart.dao.FlowerDAO;
import com.bloomcart.dao.OrderDAO;
import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.CartItem;
import com.bloomcart.model.Order;
import com.bloomcart.model.OrderItem;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderService {

    private final OrderDAO orderDAO = new OrderDAO();
    private final FlowerDAO flowerDAO = new FlowerDAO();

    public int placeOrder(int userId, List<CartItem> cartItems, String deliveryAddress, String notes)
            throws ValidationException, SQLException {

        if (cartItems == null || cartItems.isEmpty())
            throw new ValidationException("Cart is empty.");
        if (deliveryAddress == null || deliveryAddress.isBlank())
            throw new ValidationException("Delivery address is required.");

        for (CartItem item : cartItems) {
            if (item.getQuantity() > item.getFlower().getStockQuantity()) {
                throw new ValidationException("Insufficient stock for: " + item.getFlower().getName());
            }
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setDeliveryAddress(deliveryAddress);
        order.setNotes(notes);
        order.setStatus("PENDING");

        List<OrderItem> items = new ArrayList<>();
        double total = 0;
        for (CartItem ci : cartItems) {
            OrderItem oi = new OrderItem();
            oi.setFlowerId(ci.getFlower().getId());
            oi.setQuantity(ci.getQuantity());
            oi.setUnitPrice(ci.getFlower().getPrice());
            oi.setSubtotal(ci.getFlower().getPrice() * ci.getQuantity());
            total += oi.getSubtotal();
            items.add(oi);
        }
        order.setTotalAmount(total);
        order.setItems(items);

        int orderId = orderDAO.insert(order);

        for (CartItem ci : cartItems) {
            int newStock = ci.getFlower().getStockQuantity() - ci.getQuantity();
            flowerDAO.updateStock(ci.getFlower().getId(), newStock);
        }

        return orderId;
    }

    public List<Order> getOrdersByUser(int userId) throws SQLException {
        return orderDAO.findByUserId(userId);
    }

    public Order getOrderById(int id) throws SQLException {
        return orderDAO.findById(id);
    }

    public List<Order> getAllOrders() throws SQLException {
        return orderDAO.findAll();
    }

    public void updateOrderStatus(int orderId, String status) throws SQLException {
        orderDAO.updateStatus(orderId, status);
    }

    public long countOrders() throws SQLException {
        return orderDAO.count();
    }

    public long countPendingOrders() throws SQLException {
        return orderDAO.countByStatus("PENDING");
    }

    public double getTotalRevenue() throws SQLException {
        return orderDAO.sumRevenue();
    }
}
