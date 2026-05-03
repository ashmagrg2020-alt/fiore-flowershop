package com.fioreflowershop.dao;

import com.fioreflowershop.model.Category;
import com.fioreflowershop.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * CategoryDAO - Data Access Object for the categories table.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class CategoryDAO {

    /**
     * Retrieves all categories.
     *
     * @return list of Category objects
     * @throws SQLException if a database error occurs
     */
    public List<Category> findAll() throws SQLException {
        String sql = "SELECT * FROM categories ORDER BY category_name";
        List<Category> cats = new ArrayList<>();
        Connection conn = DBConnection.getConnection();
        try (Statement st = conn.createStatement()) {
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("category_name"));
                c.setDescription(rs.getString("description"));
                cats.add(c);
            }
        }
        return cats;
    }

    /**
     * Retrieves a category by ID.
     *
     * @param id the category ID
     * @return Category object if found, null otherwise
     * @throws SQLException if a database error occurs
     */
    public Category findById(int id) throws SQLException {
        String sql = "SELECT * FROM categories WHERE category_id = ?";
        Connection conn = DBConnection.getConnection();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Category c = new Category();
                c.setCategoryId(rs.getInt("category_id"));
                c.setCategoryName(rs.getString("category_name"));
                c.setDescription(rs.getString("description"));
                return c;
            }
        }
        return null;
    }
}