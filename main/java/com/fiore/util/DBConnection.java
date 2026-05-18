package com.fiore.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Utility class for managing MySQL database connections.
 * Uses singleton-style static method for simplicity in a J2EE coursework context.
 */
public class DBConnection {

    // ── Database Configuration ─────────────────────────────────────────────
    private static final String DRIVER   = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/fiore_db"
            + "?useSSL=false&serverTimezone=UTC"
            + "&allowPublicKeyRetrieval=true";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Test123@";

    // Private constructor – utility class should not be instantiated
    private DBConnection() {}

    /**
     * Returns a new Connection to the fiore_db database.
     *
     * @return Connection object
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage(), e);
        }
    }

    /**
     * Silently closes a Connection; safe to call with null.
     *
     * @param conn the Connection to close
     */
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("[DBConnection] Failed to close connection: " + e.getMessage());
            }
        }
    }
}
