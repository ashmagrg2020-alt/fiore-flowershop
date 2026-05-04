package com.bloomcart.dao;

import com.bloomcart.model.Order;
import com.bloomcart.model.OrderItem;
import com.bloomcart.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDAO {

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setTotalAmount(rs.getBigDecimal("total_amount"));
        o.setStatus(rs.getString("status"));
        o.setDeliveryAddress(rs.getString("delivery_address"));
        o.setNotes(rs.getString("notes"));
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) o.setCreatedAt(ca.toLocalDateTime());
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ua != null) o.setUpdatedAt(ua.toLocalDateTime());
        try { o.setUserFullName(rs.getString("full_name")); } catch (SQLException ignored) {}
        return o;
    }

    private OrderItem mapItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setFlowerId(rs.getInt("flower_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setUnitPrice(rs.getBigDecimal("unit_price"));
        try { item.setFlowerName(rs.getString("flower_name")); } catch (SQLException ignored) {}
        try { item.setFlowerImage(rs.getString("image_name")); } catch (SQLException ignored) {}
        return item;
    }

    /**
     * Inserts order + items in a single transaction.
     * Delegates stock decrement to FlowerDAO inside the same connection.
     */
    public int insertWithItems(Order order, List<OrderItem> items) throws SQLException {
        String orderSql = "INSERT INTO orders (user_id, total_amount, status, delivery_address, notes) VALUES (?,?,?,?,?)";
        String itemSql  = "INSERT INTO order_items (order_id, flower_id, quantity, unit_price) VALUES (?,?,?,?)";

        Connection con = DBConnection.getConnection();
        con.setAutoCommit(false);
        try {
            int orderId;
            try (PreparedStatement ps = con.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setInt(1, order.getUserId());
                ps.setBigDecimal(2, order.getTotalAmount());
                ps.setString(3, "PENDING");
                ps.setString(4, order.getDeliveryAddress());
                ps.setString(5, order.getNotes());
                ps.executeUpdate();
                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) throw new SQLException("Order insert failed.");
                    orderId = keys.getInt(1);
                }
            }

            FlowerDAO flowerDAO = new FlowerDAO();
            try (PreparedStatement ps = con.prepareStatement(itemSql)) {
                for (OrderItem item : items) {
                    ps.setInt(1, orderId);
                    ps.setInt(2, item.getFlowerId());
                    ps.setInt(3, item.getQuantity());
                    ps.setBigDecimal(4, item.getUnitPrice());
                    ps.addBatch();
                    flowerDAO.decrementStock(item.getFlowerId(), item.getQuantity(), con);
                }
                ps.executeBatch();
            }

            con.commit();
            return orderId;
        } catch (SQLException e) {
            con.rollback();
            throw e;
        } finally {
            con.setAutoCommit(true);
            DBConnection.close(con);
        }
    }

    public Order findById(int id) throws SQLException {
        String sql = "SELECT o.*, u.full_name FROM orders o JOIN users u ON o.user_id = u.id WHERE o.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (!rs.next()) return null;
                Order order = mapOrder(rs);
                order.setItems(findItemsByOrderId(id));
                return order;
            }
        }
    }

    public List<Order> findByUserId(int userId) throws SQLException {
        String sql = "SELECT o.*, u.full_name FROM orders o JOIN users u ON o.user_id = u.id WHERE o.user_id = ? ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapOrder(rs));
            }
        }
        return list;
    }

    public List<Order> findAll() throws SQLException {
        String sql = "SELECT o.*, u.full_name FROM orders o JOIN users u ON o.user_id = u.id ORDER BY o.created_at DESC";
        List<Order> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapOrder(rs));
        }
        return list;
    }

    public List<OrderItem> findItemsByOrderId(int orderId) throws SQLException {
        String sql = "SELECT oi.*, f.name AS flower_name, f.image_name FROM order_items oi JOIN flowers f ON oi.flower_id = f.id WHERE oi.order_id = ?";
        List<OrderItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapItem(rs));
            }
        }
        return list;
    }

    public void updateStatus(int orderId, String status) throws SQLException {
        String sql = "UPDATE orders SET status=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, orderId);
            ps.executeUpdate();
        }
    }

    public int countAll() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public int countPending() throws SQLException {
        String sql = "SELECT COUNT(*) FROM orders WHERE status = 'PENDING'";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    public BigDecimal totalRevenue() throws SQLException {
        String sql = "SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status NOT IN ('CANCELLED')";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getBigDecimal(1) : BigDecimal.ZERO;
        }
    }
}
