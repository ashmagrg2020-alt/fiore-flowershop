package com.fiore.dao;

import com.fiore.entity.User;
import com.fiore.util.DBConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * UserDAO - Handles all CRUD operations against the `users` table.
 */
public class UserDAO {

    // ── INSERT ─────────────────────────────────────────────────────────────

    /**
     * Inserts a new user into the database.
     *
     * @param user fully populated User entity (password must already be hashed)
     * @return true if exactly one row was inserted
     */
    public boolean insertUser(User user) {
        String sql = "INSERT INTO users (full_name, email, phone, password_hash, address, "
                   + "date_of_birth, role, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPhone());
            ps.setString(4, user.getPasswordHash());
            ps.setString(5, user.getAddress());
            ps.setDate(6,   user.getDateOfBirth() != null
                            ? Date.valueOf(user.getDateOfBirth()) : null);
            ps.setString(7, user.getRole());
            ps.setString(8, user.getStatus());

            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("[UserDAO.insertUser] " + e.getMessage());
            return false;
        }
    }

    // ── SELECT ─────────────────────────────────────────────────────────────

    /** Finds a user by their unique email address (used during login). */
    public User findByEmail(String email) {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("[UserDAO.findByEmail] " + e.getMessage());
        }
        return null;
    }

    /** Finds a user by their primary key. */
    public User findById(int id) {
        String sql = "SELECT * FROM users WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);

        } catch (SQLException e) {
            System.err.println("[UserDAO.findById] " + e.getMessage());
        }
        return null;
    }

    /** Returns true if an account with the given phone already exists (for duplicate check). */
    public boolean phoneExists(String phone) {
        String sql = "SELECT COUNT(*) FROM users WHERE phone = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("[UserDAO.phoneExists] " + e.getMessage());
        }
        return false;
    }

    /** Returns true if an account with the given email already exists. */
    public boolean emailExists(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;

        } catch (SQLException e) {
            System.err.println("[UserDAO.emailExists] " + e.getMessage());
        }
        return false;
    }

    /** Returns all users with role = 'user'. */
    public List<User> findAllUsers() {
        return findByRole("user");
    }

    /** Returns users filtered by role. */
    public List<User> findByRole(String role) {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE role = ? ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[UserDAO.findByRole] " + e.getMessage());
        }
        return list;
    }

    /** Returns pending-registration users awaiting admin approval. */
    public List<User> findPendingUsers() {
        List<User> list = new ArrayList<>();
        String sql = "SELECT * FROM users WHERE status = 'pending' AND role = 'user' ORDER BY created_at ASC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[UserDAO.findPendingUsers] " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────

    /** Updates a user's profile (name, phone, address). Password updated separately. */
    public boolean updateProfile(User user) {
        String sql = "UPDATE users SET full_name=?, phone=?, address=?, date_of_birth=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getFullName());
            ps.setString(2, user.getPhone());
            ps.setString(3, user.getAddress());
            ps.setDate(4, user.getDateOfBirth() != null
                         ? Date.valueOf(user.getDateOfBirth()) : null);
            ps.setInt(5, user.getId());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("[UserDAO.updateProfile] " + e.getMessage());
            return false;
        }
    }

    /** Updates only the password hash for a given user id. */
    public boolean updatePassword(int userId, String newHash) {
        String sql = "UPDATE users SET password_hash=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newHash);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("[UserDAO.updatePassword] " + e.getMessage());
            return false;
        }
    }

    /** Admin approves or rejects a user registration. */
    public boolean updateStatus(int userId, String newStatus) {
        String sql = "UPDATE users SET status=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, newStatus);
            ps.setInt(2, userId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("[UserDAO.updateStatus] " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────

    /** Deletes a user record (cascades to orders if FK configured). */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("[UserDAO.deleteUser] " + e.getMessage());
            return false;
        }
    }

    // ── COUNT ──────────────────────────────────────────────────────────────

    /** Returns total number of non-admin users. */
    public int countUsers() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='user'";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[UserDAO.countUsers] " + e.getMessage());
        }
        return 0;
    }

    // ── MAPPING ────────────────────────────────────────────────────────────

    /** Maps a ResultSet row to a User entity. */
    private User mapRow(ResultSet rs) throws SQLException {
        User u = new User();
        u.setId(rs.getInt("id"));
        u.setFullName(rs.getString("full_name"));
        u.setEmail(rs.getString("email"));
        u.setPhone(rs.getString("phone"));
        u.setPasswordHash(rs.getString("password_hash"));
        u.setAddress(rs.getString("address"));
        Date dob = rs.getDate("date_of_birth");
        if (dob != null) u.setDateOfBirth(dob.toLocalDate());
        u.setRole(rs.getString("role"));
        u.setStatus(rs.getString("status"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) u.setCreatedAt(ts.toLocalDateTime());
        return u;
    }
}
