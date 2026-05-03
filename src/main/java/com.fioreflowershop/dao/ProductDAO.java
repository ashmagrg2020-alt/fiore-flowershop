package com.fioreflowershop.dao;

import com.fioreflowershop.model.Product;
import com.fioreflowershop.util.DBConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProductDAO - Data Access Object for performing CRUD operations on the products table.
 * Handles all direct database interactions for Product (bouquet) entities.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class ProductDAO {

    // ===================== INSERT =====================

    /**
     * Inserts a new product into the database.
     *
     * @param product the Product object to insert
     * @return true if insertion was successful
     * @throws SQLException if a database error occurs
     */
    public boolean addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_name, sku, category_id, occasion_id, description, " +
                "price, stock_qty, image_url, is_featured, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getSku());
            ps.setInt(3, product.getCategoryId());
            if (product.getOccasionId() != null)
                ps.setInt(4, product.getOccasionId());
            else
                ps.setNull(4, Types.INTEGER);
            ps.setString(5, product.getDescription());
            ps.setBigDecimal(6, product.getPrice());
            ps.setInt(7, product.getStockQty());
            ps.setString(8, product.getImageUrl());
            ps.setBoolean(9, product.isFeatured());
            ps.setString(10, product.getStatus());
            return ps.executeUpdate() == 1;
        }
    }

    // ===================== SELECT =====================

    /**
     * Retrieves all products with their category and occasion names.
     *
     * @return list of all Product objects
     * @throws SQLException if a database error occurs
     */
    public List<Product> findAll() throws SQLException {
        String sql = "SELECT p.*, c.category_name, o.occasion_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN occasions o ON p.occasion_id = o.occasion_id " +
                "ORDER BY p.created_at DESC";
        List<Product> products = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    /**
     * Retrieves all ACTIVE products for the shop page.
     *
     * @return list of active Product objects
     * @throws SQLException if a database error occurs
     */
    public List<Product> findAllActive() throws SQLException {
        String sql = "SELECT p.*, c.category_name, o.occasion_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN occasions o ON p.occasion_id = o.occasion_id " +
                "WHERE p.status = 'ACTIVE' ORDER BY p.product_name ASC";
        List<Product> products = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    /**
     * Retrieves featured products for the home page.
     *
     * @return list of featured Product objects
     * @throws SQLException if a database error occurs
     */
    public List<Product> findFeatured() throws SQLException {
        String sql = "SELECT p.*, c.category_name, o.occasion_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN occasions o ON p.occasion_id = o.occasion_id " +
                "WHERE p.status = 'ACTIVE' AND p.is_featured = 1 ORDER BY p.product_name ASC";
        List<Product> products = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param productId the product ID to search for
     * @return Product object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Product findById(int productId) throws SQLException {
        String sql = "SELECT p.*, c.category_name, o.occasion_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN occasions o ON p.occasion_id = o.occasion_id " +
                "WHERE p.product_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return mapRow(rs);
            }
        }
        return null;
    }

    /**
     * Searches products by name, SKU, or category (case-insensitive).
     *
     * @param keyword the search keyword
     * @return list of matching Product objects
     * @throws SQLException if a database error occurs
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        String sql = "SELECT p.*, c.category_name, o.occasion_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN occasions o ON p.occasion_id = o.occasion_id " +
                "WHERE p.status = 'ACTIVE' AND " +
                "(p.product_name LIKE ? OR p.sku LIKE ? OR c.category_name LIKE ?)";
        List<Product> products = new ArrayList<>();
        String likeKeyword = "%" + keyword + "%";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, likeKeyword);
            ps.setString(2, likeKeyword);
            ps.setString(3, likeKeyword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    /**
     * Retrieves products filtered by category ID.
     *
     * @param categoryId the category ID to filter by
     * @return list of matching Product objects
     * @throws SQLException if a database error occurs
     */
    public List<Product> findByCategory(int categoryId) throws SQLException {
        String sql = "SELECT p.*, c.category_name, o.occasion_name " +
                "FROM products p " +
                "JOIN categories c ON p.category_id = c.category_id " +
                "LEFT JOIN occasions o ON p.occasion_id = o.occasion_id " +
                "WHERE p.status = 'ACTIVE' AND p.category_id = ? ORDER BY p.product_name ASC";
        List<Product> products = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                products.add(mapRow(rs));
            }
        }
        return products;
    }

    // ===================== UPDATE =====================

    /**
     * Updates a product's information.
     *
     * @param product the Product object with updated data
     * @return true if update was successful
     * @throws SQLException if a database error occurs
     */
    public boolean updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET product_name=?, sku=?, category_id=?, occasion_id=?, " +
                "description=?, price=?, stock_qty=?, image_url=?, is_featured=?, status=? " +
                "WHERE product_id=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, product.getProductName());
            ps.setString(2, product.getSku());
            ps.setInt(3, product.getCategoryId());
            if (product.getOccasionId() != null)
                ps.setInt(4, product.getOccasionId());
            else
                ps.setNull(4, Types.INTEGER);
            ps.setString(5, product.getDescription());
            ps.setBigDecimal(6, product.getPrice());
            ps.setInt(7, product.getStockQty());
            ps.setString(8, product.getImageUrl());
            ps.setBoolean(9, product.isFeatured());
            ps.setString(10, product.getStatus());
            ps.setInt(11, product.getProductId());
            return ps.executeUpdate() == 1;
        }
    }

    // ===================== DELETE =====================

    /**
     * Deletes a product from the database by ID.
     *
     * @param productId the product ID to delete
     * @return true if deletion was successful
     * @throws SQLException if a database error occurs
     */
    public boolean deleteProduct(int productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id=?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, productId);
            return ps.executeUpdate() == 1;
        }
    }

    // ===================== HELPER =====================

    /**
     * Checks if a SKU already exists.
     *
     * @param sku the SKU to check
     * @return true if SKU exists
     * @throws SQLException if a database error occurs
     */
    public boolean skuExists(String sku) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE sku = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sku);
            return ps.executeQuery().next();
        }
    }

    /**
     * Checks if a SKU exists excluding a specific product (for updates).
     *
     * @param sku       the SKU to check
     * @param productId the product to exclude
     * @return true if SKU exists in another product
     * @throws SQLException if a database error occurs
     */
    public boolean skuExistsExcluding(String sku, int productId) throws SQLException {
        String sql = "SELECT 1 FROM products WHERE sku = ? AND product_id != ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, sku);
            ps.setInt(2, productId);
            return ps.executeQuery().next();
        }
    }

    /**
     * Gets total count of products per status for dashboard stats.
     *
     * @return array: [total, active, inactive]
     * @throws SQLException if a database error occurs
     */
    public int[] getProductStats() throws SQLException {
        String sql = "SELECT COUNT(*) AS total, " +
                "SUM(CASE WHEN status='ACTIVE' THEN 1 ELSE 0 END) AS active, " +
                "SUM(CASE WHEN status='INACTIVE' THEN 1 ELSE 0 END) AS inactive FROM products";
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            if (rs.next()) {
                return new int[]{rs.getInt("total"), rs.getInt("active"), rs.getInt("inactive")};
            }
        }
        return new int[]{0, 0, 0};
    }

    /**
     * Maps a ResultSet row to a Product object.
     *
     * @param rs the ResultSet positioned at the current row
     * @return populated Product object
     * @throws SQLException if column access fails
     */
    private Product mapRow(ResultSet rs) throws SQLException {
        Product p = new Product();
        p.setProductId(rs.getInt("product_id"));
        p.setProductName(rs.getString("product_name"));
        p.setSku(rs.getString("sku"));
        p.setCategoryId(rs.getInt("category_id"));
        p.setCategoryName(rs.getString("category_name"));
        int occId = rs.getInt("occasion_id");
        p.setOccasionId(rs.wasNull() ? null : occId);
        p.setOccasionName(rs.getString("occasion_name"));
        p.setDescription(rs.getString("description"));
        p.setPrice(rs.getBigDecimal("price"));
        p.setStockQty(rs.getInt("stock_qty"));
        p.setImageUrl(rs.getString("image_url"));
        p.setFeatured(rs.getBoolean("is_featured"));
        p.setStatus(rs.getString("status"));
        p.setCreatedAt(rs.getTimestamp("created_at"));
        p.setUpdatedAt(rs.getTimestamp("updated_at"));
        return p;
    }
}