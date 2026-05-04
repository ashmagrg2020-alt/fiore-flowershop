package com.bloomcart.dao;

import com.bloomcart.model.WishlistItem;
import com.bloomcart.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class WishlistDAO {

    private WishlistItem mapRow(ResultSet rs) throws SQLException {
        WishlistItem w = new WishlistItem();
        w.setId(rs.getInt("id"));
        w.setUserId(rs.getInt("user_id"));
        w.setFlowerId(rs.getInt("flower_id"));
        w.setFlowerName(rs.getString("name"));
        w.setImageName(rs.getString("image_name"));
        w.setPrice(rs.getBigDecimal("price"));
        w.setInStock(rs.getInt("stock_qty") > 0);
        Timestamp at = rs.getTimestamp("added_at");
        if (at != null) w.setAddedAt(at.toLocalDateTime());
        return w;
    }

    public List<WishlistItem> findByUserId(int userId) throws SQLException {
        String sql = "SELECT w.id, w.user_id, w.flower_id, w.added_at, f.name, f.image_name, f.price, f.stock_qty " +
                     "FROM wishlist w JOIN flowers f ON w.flower_id = f.id WHERE w.user_id = ? ORDER BY w.added_at DESC";
        List<WishlistItem> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public boolean exists(int userId, int flowerId) throws SQLException {
        String sql = "SELECT id FROM wishlist WHERE user_id = ? AND flower_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, flowerId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public void add(int userId, int flowerId) throws SQLException {
        String sql = "INSERT IGNORE INTO wishlist (user_id, flower_id) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, flowerId);
            ps.executeUpdate();
        }
    }

    public void remove(int userId, int flowerId) throws SQLException {
        String sql = "DELETE FROM wishlist WHERE user_id = ? AND flower_id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setInt(2, flowerId);
            ps.executeUpdate();
        }
    }
}
