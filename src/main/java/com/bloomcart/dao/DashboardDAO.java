package com.bloomcart.dao;

import com.bloomcart.model.DashboardStats;
import com.bloomcart.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;

public class DashboardDAO {

    public DashboardStats getStats() throws SQLException {
        String sql =
            "SELECT " +
            "  (SELECT COUNT(*) FROM flowers   WHERE is_active = 1)            AS total_flowers, " +
            "  (SELECT COUNT(*) FROM users     WHERE role_id   = 2)            AS total_users, " +
            "  (SELECT COUNT(*) FROM orders)                                   AS total_orders, " +
            "  (SELECT COUNT(*) FROM orders    WHERE status = 'PENDING')       AS pending_orders, " +
            "  (SELECT COALESCE(SUM(total_amount),0) FROM orders WHERE status NOT IN ('CANCELLED')) AS total_revenue, " +
            "  (SELECT COUNT(*) FROM categories)                               AS total_categories, " +
            "  (SELECT COUNT(*) FROM contact_messages WHERE is_read = 0)       AS unread_messages";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                DashboardStats stats = new DashboardStats();
                stats.setTotalFlowers(rs.getInt("total_flowers"));
                stats.setTotalUsers(rs.getInt("total_users"));
                stats.setTotalOrders(rs.getInt("total_orders"));
                stats.setPendingOrders(rs.getInt("pending_orders"));
                stats.setTotalRevenue(rs.getBigDecimal("total_revenue"));
                stats.setTotalCategories(rs.getInt("total_categories"));
                stats.setUnreadMessages(rs.getInt("unread_messages"));
                return stats;
            }
        }
        return new DashboardStats();
    }
}
