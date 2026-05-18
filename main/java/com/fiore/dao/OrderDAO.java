package com.fiore.dao;

import com.fiore.entity.Order;
import com.fiore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * OrderDAO - Handles all CRUD operations against the `orders` table.
 */
public class OrderDAO {

    // ── INSERT ─────────────────────────────────────────────────────────────

    /** Places a new order. Returns the generated order ID, or -1 on failure. */
    public int insertOrder(Order o) {
        String sql = "INSERT INTO orders (user_id, bouquet_id, quantity, total_price, status, special_note) "
                   + "VALUES (?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1,    o.getUserId());
            ps.setInt(2,    o.getBouquetId());
            ps.setInt(3,    o.getQuantity());
            ps.setDouble(4, o.getTotalPrice());
            ps.setString(5, o.getStatus());
            ps.setString(6, o.getSpecialNote());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[OrderDAO.insertOrder] " + e.getMessage());
        }
        return -1;
    }

    // ── SELECT ─────────────────────────────────────────────────────────────

    /** Returns all orders for a given user (with bouquet name joined). */
    public List<Order> findByUserId(int userId) {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, b.name AS bouquet_name, b.image_path AS bouquet_image "
                   + "FROM orders o JOIN bouquets b ON o.bouquet_id = b.id "
                   + "WHERE o.user_id = ? ORDER BY o.ordered_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO.findByUserId] " + e.getMessage());
        }
        return list;
    }

    /** Returns ALL orders (for admin view), with user name and bouquet name. */
    public List<Order> findAll() {
        List<Order> list = new ArrayList<>();
        String sql = "SELECT o.*, u.full_name AS user_name, "
                   + "b.name AS bouquet_name, b.image_path AS bouquet_image "
                   + "FROM orders o "
                   + "JOIN users    u ON o.user_id    = u.id "
                   + "JOIN bouquets b ON o.bouquet_id = b.id "
                   + "ORDER BY o.ordered_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[OrderDAO.findAll] " + e.getMessage());
        }
        return list;
    }

    /** Finds a single order by ID. */
    public Order findById(int id) {
        String sql = "SELECT o.*, u.full_name AS user_name, "
                   + "b.name AS bouquet_name, b.image_path AS bouquet_image "
                   + "FROM orders o "
                   + "JOIN users    u ON o.user_id    = u.id "
                   + "JOIN bouquets b ON o.bouquet_id = b.id "
                   + "WHERE o.id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[OrderDAO.findById] " + e.getMessage());
        }
        return null;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────

    /** Admin updates order status (confirm / deliver / cancel). */
    public boolean updateStatus(int orderId, String newStatus) {
        String sql = "UPDATE orders SET status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, orderId);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("[OrderDAO.updateStatus] " + e.getMessage());
            return false;
        }
    }

    // ── COUNT ──────────────────────────────────────────────────────────────

    public int countOrders() {
        String sql = "SELECT COUNT(*) FROM orders";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[OrderDAO.countOrders] " + e.getMessage());
        }
        return 0;
    }

    /** Returns total revenue (sum of confirmed/delivered orders). */
    public double totalRevenue() {
        String sql = "SELECT COALESCE(SUM(total_price),0) FROM orders "
                   + "WHERE status IN ('confirmed','delivered')";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            System.err.println("[OrderDAO.totalRevenue] " + e.getMessage());
        }
        return 0.0;
    }

    // ── MAPPING ────────────────────────────────────────────────────────────

    private Order mapRow(ResultSet rs) throws SQLException {
        Order o = new Order();
        o.setId(rs.getInt("id"));
        o.setUserId(rs.getInt("user_id"));
        o.setBouquetId(rs.getInt("bouquet_id"));
        o.setQuantity(rs.getInt("quantity"));
        o.setTotalPrice(rs.getDouble("total_price"));
        o.setStatus(rs.getString("status"));
        o.setSpecialNote(rs.getString("special_note"));
        Timestamp ts = rs.getTimestamp("ordered_at");
        if (ts != null) o.setOrderedAt(ts.toLocalDateTime());
        // Joined columns (may not always be present)
        try { o.setUserName(rs.getString("user_name")); }   catch (SQLException ignored) {}
        try { o.setBouquetName(rs.getString("bouquet_name")); } catch (SQLException ignored) {}
        try { o.setBouquetImage(rs.getString("bouquet_image")); } catch (SQLException ignored) {}
        return o;
    }
}
