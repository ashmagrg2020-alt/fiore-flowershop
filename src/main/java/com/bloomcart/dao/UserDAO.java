package com.bloomcart.dao;

import com.bloomcart.model.User;
import com.bloomcart.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPassword(rs.getString("password"));
        u.setSalt(rs.getString("salt"));
        u.setAddress(rs.getString("address"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) u.setDateOfBirth(dob.toLocalDate());
        u.setRoleId(rs.getInt("role_id"));
        u.setRoleName(rs.getString("role_name"));
        u.setActive(rs.getBoolean("is_active"));
        Timestamp ca = rs.getTimestamp("created_at");
        if (ca != null) u.setCreatedAt(ca.toLocalDateTime());
        Timestamp ua = rs.getTimestamp("updated_at");
        if (ua != null) u.setUpdatedAt(ua.toLocalDateTime());
        return u;
    }

    private static final String SELECT_BASE =
        "SELECT u.*, r.name AS role_name FROM users u JOIN roles r ON u.role_id = r.id ";

    public User findById(int id) throws SQLException {
        String sql = SELECT_BASE + "WHERE u.id = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public User findByEmail(String email) throws SQLException {
        String sql = SELECT_BASE + "WHERE u.email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    public boolean emailExists(String email) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public boolean phoneExists(String phone) throws SQLException {
        String sql = "SELECT id FROM users WHERE phone = ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public boolean emailExistsForOther(String email, int excludeId) throws SQLException {
        String sql = "SELECT id FROM users WHERE email = ? AND id <> ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public boolean phoneExistsForOther(String phone, int excludeId) throws SQLException {
        String sql = "SELECT id FROM users WHERE phone = ? AND id <> ?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, phone);
            ps.setInt(2, excludeId);
            try (ResultSet rs = ps.executeQuery()) { return rs.next(); }
        }
    }

    public int insert(User u) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, phone, password, salt, address, date_of_birth, role_id, is_active) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getEmail());
            ps.setString(3, u.getPhone());
            ps.setString(4, u.getPassword());
            ps.setString(5, u.getSalt());
            ps.setString(6, u.getAddress());
            if (u.getDateOfBirth() != null)
                ps.setDate(7, Date.valueOf(u.getDateOfBirth()));
            else
                ps.setNull(7, Types.DATE);
            ps.setInt(8, u.getRoleId());
            ps.setBoolean(9, u.isActive());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                return keys.next() ? keys.getInt(1) : -1;
            }
        }
    }

    public void updateProfile(User u) throws SQLException {
        String sql = "UPDATE users SET full_name=?, phone=?, address=?, date_of_birth=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, u.getFullName());
            ps.setString(2, u.getPhone());
            ps.setString(3, u.getAddress());
            if (u.getDateOfBirth() != null)
                ps.setDate(4, Date.valueOf(u.getDateOfBirth()));
            else
                ps.setNull(4, Types.DATE);
            ps.setInt(5, u.getId());
            ps.executeUpdate();
        }
    }

    public void updatePassword(int userId, String hashedPassword, String salt) throws SQLException {
        String sql = "UPDATE users SET password=?, salt=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, hashedPassword);
            ps.setString(2, salt);
            ps.setInt(3, userId);
            ps.executeUpdate();
        }
    }

    public void setActiveStatus(int userId, boolean active) throws SQLException {
        String sql = "UPDATE users SET is_active=?, updated_at=NOW() WHERE id=?";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setBoolean(1, active);
            ps.setInt(2, userId);
            ps.executeUpdate();
        }
    }

    public List<User> findAllCustomers() throws SQLException {
        String sql = SELECT_BASE + "WHERE u.role_id = 2 ORDER BY u.created_at DESC";
        List<User> list = new ArrayList<>();
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        }
        return list;
    }

    public int countCustomers() throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE role_id = 2";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return rs.next() ? rs.getInt(1) : 0;
        }
    }
}
