package com.bloomcart.dao;

import com.bloomcart.model.Flower;
import com.bloomcart.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FlowerDAO {

    private static final String SELECT_BASE =
        "SELECT f.*, c.name AS category_name, o.name AS occasion_name " +
        "FROM flowers f " +
        "LEFT JOIN categories c ON f.category_id = c.id " +
        "LEFT JOIN occasions  o ON f.occasion_id  = o.id ";

    private Flower mapRow(ResultSet rs) throws SQLException {
        Flower f = new Flower();
        f.setId(rs.getInt("id"));
        f.setName(rs.getString("name"));
        f.setDescription(rs.getString("description"));
        f.setPrice(rs.getBigDecimal("price"));
        f.setStockQty(rs.getInt("stock_qty"));
        f.setCategoryId(rs.getInt("category_id"));
        f.setCategoryName(rs.getString("category_name"));
        f.setOccasionId(rs.getInt("occasion_id"));
        f.setOccasionName(rs.getString("occasion_name"));
        f.setImageName(rs.getString("image_name"));
        f.setFeatured(rs.getBoolean("is_featured"));
        f.setActive(rs.getBoolean("is_active"));
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) f.setCreatedAt(ca.toLocalDateTime());
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ua != null) f.setUpdatedAt(ua.toLocalDateTime());
        return f;
    }

    public List<Flower> findAll() throws SQLException {
        String sql = SELECT_BASE + "ORDER BY f.created_at DESC";
        List<Flower> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Flower> findActive() throws SQLException {
        String sql = SELECT_BASE + "WHERE f.is_active = 1 ORDER BY f.name";
        List<Flower> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public List<Flower> findFeatured() throws SQLException {
        String sql = SELECT_BASE + "WHERE f.is_active = 1 AND f.is_featured = 1 ORDER BY f.name";
        List<Flower> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Flower findById(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE f.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    /**
     * Search by name keyword, category, occasion, and price range.
     * All filters are optional (pass null/0 to skip).
     */
    public List<Flower> search(String keyword, Integer categoryId, Integer occasionId,
                               BigDecimal minPrice, BigDecimal maxPrice) throws SQLException {
        StringBuilder sql = new StringBuilder(SELECT_BASE + "WHERE f.is_active = 1 ");
        List<Object> params = new ArrayList<>();

        if (keyword != null && !keyword.isBlank()) {
            sql.append("AND f.name LIKE ? ");
            params.add("%" + keyword.trim() + "%");
        }
        if (categoryId != null && categoryId > 0) {
            sql.append("AND f.category_id = ? ");
            params.add(categoryId);
        }
        if (occasionId != null && occasionId > 0) {
            sql.append("AND f.occasion_id = ? ");
            params.add(occasionId);
        }
        if (minPrice != null) {
            sql.append("AND f.price >= ? ");
            params.add(minPrice);
        }
        if (maxPrice != null) {
            sql.append("AND f.price <= ? ");
            params.add(maxPrice);
        }
        sql.append("ORDER BY f.name");

        List<Flower> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                ps.setObject(i + 1, params.get(i));
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        }
        return list;
    }

    public int insert(Flower f) throws SQLException {
        String sql = "INSERT INTO flowers (name, description, price, stock_qty, category_id, occasion_id, image_name, is_featured, is_active) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, f.getName());
            ps.setString(2, f.getDescription());
            ps.setBigDecimal(3, f.getPrice());
            ps.setInt(4, f.getStockQty());
            setNullableInt(ps, 5, f.getCategoryId());
            setNullableInt(ps, 6, f.getOccasionId());
            ps.setString(7, f.getImageName());
            ps.setBoolean(8, f.isFeatured());
            ps.setBoolean(9, f.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public void update(Flower f) throws SQLException {
        String sql = "UPDATE flowers SET name=?, description=?, price=?, stock_qty=?, category_id=?, occasion_id=?, image_name=?, is_featured=?, is_active=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, f.getName());
            ps.setString(2, f.getDescription());
            ps.setBigDecimal(3, f.getPrice());
            ps.setInt(4, f.getStockQty());
            setNullableInt(ps, 5, f.getCategoryId());
            setNullableInt(ps, 6, f.getOccasionId());
            ps.setString(7, f.getImageName());
            ps.setBoolean(8, f.isFeatured());
            ps.setBoolean(9, f.isActive());
            ps.setInt(10, f.getId());
            ps.executeUpdate();
        }
    }

    public void decrementStock(int flowerId, int qty, Connection con) throws SQLException {
        String sql = "UPDATE flowers SET stock_qty = stock_qty - ? WHERE id = ? AND stock_qty >= ?";
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, qty);
            ps.setInt(2, flowerId);
            ps.setInt(3, qty);
            int rows = ps.executeUpdate();
            if (rows == 0) throw new SQLException("Insufficient stock for flower id=" + flowerId);
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM flowers WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    public int countActive() throws SQLException {
        String sql = "SELECT COUNT(*) FROM flowers WHERE is_active = 1";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }

    private void setNullableInt(PreparedStatement ps, int index, int value) throws SQLException {
        if (value > 0) ps.setInt(index, value);
        else ps.setNull(index, Types.INTEGER);
    }
}
