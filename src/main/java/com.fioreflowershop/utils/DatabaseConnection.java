package com.fioreflowershop.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBConnection - Utility class for managing MySQL database connections.
 * Uses singleton pattern to ensure a single connection instance.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class DBConnection {

    // Database connection constants
    private static final String DB_URL      = "jdbc:mysql://localhost:3306/fiore_flowershop?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER     = "root";
    private static final String DB_PASSWORD = "";  // Update with your MySQL password
    private static final String DRIVER      = "com.mysql.cj.jdbc.Driver";

    private static Connection connection = null;

    /**
     * Private constructor prevents direct instantiation.
     */
    private DBConnection() {}

    /**
     * Returns the singleton database connection.
     * Creates a new connection if none exists or if it has been closed.
     *
     * @return Connection object
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            }
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found: " + e.getMessage());
        }
        return connection;
    }

    /**
     * Closes the database connection safely.
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}