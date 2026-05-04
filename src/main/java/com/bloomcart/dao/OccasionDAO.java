package com.bloomcart.dao;

import com.bloomcart.model.Occasion;
import com.bloomcart.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OccasionDAO {

    private Occasion mapRow(ResultSet rs) throws SQLException {
        Occasion o = new Occasion();
        o.setId(rs.getInt("id"));
        o.setName(rs.getString("name"));
        o.setDescription(rs.getString("description"));
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) o.setCreatedAt(ca.toLocalDateTime());
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ua != null) o.setUpdatedAt(ua.toLocalDateTime());
        return o;
    }

    public List<Occasion> findAll() throws SQLException {
        String sql = "SELECT * FROM occasions ORDER BY name";
        List<Occasion> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public Occasion findById(int id) throws SQLException {
        String sql = "SELECT * FROM occasions WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean nameExists(String name) throws SQLException {
        String sql = "SELECT id FROM occasions WHERE name = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public boolean nameExistsForOther(String name, int excludeId) throws SQLException {
        String sql = "SELECT id FROM occasions WHERE name = ? AND id <> ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public int insert(Occasion o) throws SQLException {
        String sql = "INSERT INTO occasions (name, description) VALUES (?, ?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, o.getName());
            ps.setString(2, o.getDescription());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public void update(Occasion o) throws SQLException {
        String sql = "UPDATE occasions SET name=?, description=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, o.getName());
            ps.setString(2, o.getDescription());
            ps.setInt(3, o.getId());
            ps.executeUpdate();
        }
    }

    public void delete(int id) throws SQLException {
        String sql = "DELETE FROM occasions WHERE id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}
