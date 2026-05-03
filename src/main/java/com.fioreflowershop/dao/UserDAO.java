package com.fioreflowershop.dao;

import com.fioreflowershop.model.User;
import com.fioreflowershop.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Data Access Object for performing CRUD operations on the users table.
 * Handles all direct database interactions for User entities.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class UserDAO {

    // ===================== INSERT =====================

    /**
     * Registers a new user in the database.
     *
     * @param user the User object to insert
     * @return true if insertion was successful, false otherwise
     * @throws SQLException if a database error occurs
     */
    public boolean registerUser(User user) throws SQLException {
        String sql = "INSERT INTO users (full_name, email, phone, password_hash, date_of_birth, address, role_id, status) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPasswordHash());
            ps.setDate(5, user.getDateOfBirth());
            ps.setString(6, user.getAddress());
            ps.setInt(7, user.getRoleId());
            ps.setString(8, user.getStatus());
            return ps.executeUpdate() == 1;
        }
    }

    // ===================== SELECT =====================

    /**
     * Retrieves a user by their email address.
     *
     * @param email the email to search for
     * @return User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT u.*, r.role_name FROM users u " +
                "JOIN roles r ON u.role_id = r.role_id WHERE u.email = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Retrieves a user by their phone number.
     *
     * @param phone the phone number to search for
     * @return User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User findByPhone(String phone) throws SQLException {
        String sql = "SELECT u.*, r.role_name FROM users u " +
                "JOIN roles r ON u.role_id = r.role_id WHERE u.phone = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param userId the user ID to search for
     * @return User object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public User findById(int userId) throws SQLException {
        String sql = "SELECT u.*, r.role_name FROM users u " +
                "JOIN roles r ON u.role_id = r.role_id WHERE u.user_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Retrieves all users with a specific status.
     *
     * @param status the status filter (PENDING, APPROVED, REJECTED)
     * @return list of matching User objects
     * @throws SQLException if a database error occurs
     */
    public List<User> findByStatus(String status) throws SQLException {
        String sql = "SELECT u.*, r.role_name FROM users u " +
                "JOIN roles r ON u.role_id = r.role_id WHERE u.status = ? ORDER BY u.created_at DESC";
        List<User> users = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    /**
     * Retrieves all users from the database.
     *
     * @return list of all User objects
     * @throws SQLException if a database error occurs
     */
    public List<User> findAll() throws SQLException {
        String sql = "SELECT u.*, r.role_name FROM users u " +
                "JOIN roles r ON u.role_id = r.role_id ORDER BY u.created_at DESC";
        List<User> users = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                users.add(mapRow(rs));
            }
        }
        return users;
    }

    // ===================== UPDATE =====================

    /**
     * Updates a user's profile information.
     *
     * @param user the User object with updated data
     * @return true if update was successful
     * @throws SQLException if a database error occurs
     */
    public boolean updateProfile(User user) throws SQLException {
        String sql = "UPDATE users SET full_name=?, phone=?, address=?, date_of_birth=? WHERE user_id=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddress());
            ps.setDate(4, user.getDateOfBirth());
            ps.setInt(5, user.getUserId());
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Updates a user's password hash.
     *
     * @param userId       the user ID
     * @param newHashedPwd the new BCrypt password hash
     * @return true if update was successful
     * @throws SQLException if a database error occurs
     */
    public boolean updatePassword(int userId, String newHashedPwd) throws SQLException {
        String sql = "UPDATE users SET password_hash=? WHERE user_id=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newHashedPwd);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }

    /**
     * Updates a user's approval status (admin action).
     *
     * @param userId the user ID
     * @param status the new status (APPROVED or REJECTED)
     * @return true if update was successful
     * @throws SQLException if a database error occurs
     */
    public boolean updateStatus(int userId, String status) throws SQLException {
        String sql = "UPDATE users SET status=? WHERE user_id=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;
        }
    }

    // ===================== DELETE =====================

    /**
     * Deletes a user from the database by ID.
     *
     * @param userId the user ID to delete
     * @return true if deletion was successful
     * @throws SQLException if a database error occurs
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE user_id=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;
        }
    }

    // ===================== HELPER =====================

    /**
     * Checks if an email already exists in the database.
     *
     * @param email the email to check
     * @return true if email exists
     * @throws SQLException if a database error occurs
     */
    public boolean emailExists(String email) throws SQLException {
        return findByEmail(email) != null;
    }

    /**
     * Checks if a phone number already exists in the database.
     *
     * @param phone the phone to check
     * @return true if phone exists
     * @throws SQLException if a database error occurs
     */
    public boolean phoneExists(String phone) throws SQLException {
        return findByPhone(phone) != null;
    }

    /**
     * Maps a ResultSet row to a User object.
     *
     * @param rs the ResultSet positioned at the current row
     * @return populated User object
     * @throws SQLException if column access fails
     */
    private User mapRow(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("user_id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setPasswordHash(rs.getString("password_hash"));
        user.setDateOfBirth(rs.getDate("date_of_birth"));
        user.setAddress(rs.getString("address"));
        user.setRoleId(rs.getInt("role_id"));
        user.setRoleName(rs.getString("role_name"));
        user.setStatus(rs.getString("status"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}