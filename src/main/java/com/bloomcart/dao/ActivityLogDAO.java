package com.bloomcart.dao;

import com.bloomcart.model.ActivityLog;
import com.bloomcart.util.DBConnection;

import java.sql.*;

public class ActivityLogDAO {

    public void log(ActivityLog entry) {
        String sql = "INSERT INTO activity_logs (user_id, action, description, ip_address) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            if (entry.getUserId() != null) ps.setInt(1, entry.getUserId());
            else ps.setNull(1, Types.INTEGER);
            ps.setString(2, entry.getAction());
            ps.setString(3, entry.getDescription());
            ps.setString(4, entry.getIpAddress());
            ps.executeUpdate();
        } catch (SQLException e) {
            // Logging failures must never break the main flow
            System.err.println("ActivityLog insert failed: " + e.getMessage());
        }
    }

    /** Convenience wrapper — no checked exception propagated. */
    public void log(Integer userId, String action, String description, String ip) {
        log(new ActivityLog(userId, action, description, ip));
    }
}
