package com.fiore.dao;

import com.fiore.entity.Bouquet;
import com.fiore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * BouquetDAO - Handles all CRUD operations against the `bouquets` table.
 */
public class BouquetDAO {

    // ── INSERT ─────────────────────────────────────────────────────────────

    /** Inserts a new bouquet record and returns the generated ID, or -1 on failure. */
    public int insertBouquet(Bouquet b) {
        String sql = "INSERT INTO bouquets (name, description, price, stock_quantity, "
                   + "category, occasion, image_path, featured) VALUES (?,?,?,?,?,?,?,?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, b.getName());
            ps.setString(2, b.getDescription());
            ps.setDouble(3, b.getPrice());
            ps.setInt(4,    b.getStockQuantity());
            ps.setString(5, b.getCategory());
            ps.setString(6, b.getOccasion());
            ps.setString(7, b.getImagePath());
            ps.setBoolean(8, b.isFeatured());

            int rows = ps.executeUpdate();
            if (rows == 1) {
                ResultSet keys = ps.getGeneratedKeys();
                if (keys.next()) return keys.getInt(1);
            }
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.insertBouquet] " + e.getMessage());
        }
        return -1;
    }

    // ── SELECT ─────────────────────────────────────────────────────────────

    /** Returns all bouquets ordered by creation date descending. */
    public List<Bouquet> findAll() {
        List<Bouquet> list = new ArrayList<>();
        String sql = "SELECT * FROM bouquets ORDER BY created_at DESC";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.findAll] " + e.getMessage());
        }
        return list;
    }

    /** Returns all featured bouquets (for the home-page section). */
    public List<Bouquet> findFeatured() {
        List<Bouquet> list = new ArrayList<>();
        String sql = "SELECT * FROM bouquets WHERE featured=1 ORDER BY created_at DESC LIMIT 4";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.findFeatured] " + e.getMessage());
        }
        return list;
    }

    /** Finds a single bouquet by primary key. */
    public Bouquet findById(int id) {
        String sql = "SELECT * FROM bouquets WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return mapRow(rs);
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.findById] " + e.getMessage());
        }
        return null;
    }

    /**
     * Searches / filters bouquets by name keyword and/or category.
     * Pass null or empty string to skip a filter criterion.
     */
    public List<Bouquet> search(String keyword, String category) {
        List<Bouquet> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder("SELECT * FROM bouquets WHERE 1=1");
        if (keyword  != null && !keyword.trim().isEmpty())
            sb.append(" AND name LIKE ?");
        if (category != null && !category.trim().isEmpty() && !category.equals("All"))
            sb.append(" AND category = ?");
        sb.append(" ORDER BY featured DESC, created_at DESC");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sb.toString())) {

            int idx = 1;
            if (keyword  != null && !keyword.trim().isEmpty())
                ps.setString(idx++, "%" + keyword.trim() + "%");
            if (category != null && !category.trim().isEmpty() && !category.equals("All"))
                ps.setString(idx, category.trim());

            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));

        } catch (SQLException e) {
            System.err.println("[BouquetDAO.search] " + e.getMessage());
        }
        return list;
    }

    /** Returns the top-N most-sold bouquets for the admin report. */
    public List<Bouquet> findTopSellers(int limit) {
        List<Bouquet> list = new ArrayList<>();
        String sql = "SELECT b.*, SUM(o.quantity) AS total_sold "
                   + "FROM bouquets b JOIN orders o ON b.id = o.bouquet_id "
                   + "GROUP BY b.id ORDER BY total_sold DESC LIMIT ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, limit);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapRow(rs));
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.findTopSellers] " + e.getMessage());
        }
        return list;
    }

    // ── UPDATE ─────────────────────────────────────────────────────────────

    /** Updates all editable fields of a bouquet. */
    public boolean updateBouquet(Bouquet b) {
        String sql = "UPDATE bouquets SET name=?, description=?, price=?, stock_quantity=?, "
                   + "category=?, occasion=?, image_path=?, featured=? WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, b.getName());
            ps.setString(2, b.getDescription());
            ps.setDouble(3, b.getPrice());
            ps.setInt(4,    b.getStockQuantity());
            ps.setString(5, b.getCategory());
            ps.setString(6, b.getOccasion());
            ps.setString(7, b.getImagePath());
            ps.setBoolean(8, b.isFeatured());
            ps.setInt(9,    b.getId());
            return ps.executeUpdate() == 1;

        } catch (SQLException e) {
            System.err.println("[BouquetDAO.updateBouquet] " + e.getMessage());
            return false;
        }
    }

    // ── DELETE ─────────────────────────────────────────────────────────────

    /** Deletes a bouquet by ID. */
    public boolean deleteBouquet(int id) {
        String sql = "DELETE FROM bouquets WHERE id=?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() == 1;
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.deleteBouquet] " + e.getMessage());
            return false;
        }
    }

    // ── COUNT ──────────────────────────────────────────────────────────────

    public int countBouquets() {
        String sql = "SELECT COUNT(*) FROM bouquets";
        try (Connection conn = DBConnection.getConnection();
             Statement  st   = conn.createStatement();
             ResultSet  rs   = st.executeQuery(sql)) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            System.err.println("[BouquetDAO.countBouquets] " + e.getMessage());
        }
        return 0;
    }

    // ── MAPPING ────────────────────────────────────────────────────────────

    private Bouquet mapRow(ResultSet rs) throws SQLException {
        Bouquet b = new Bouquet();
        b.setId(rs.getInt("id"));
        b.setName(rs.getString("name"));
        b.setDescription(rs.getString("description"));
        b.setPrice(rs.getDouble("price"));
        b.setStockQuantity(rs.getInt("stock_quantity"));
        b.setCategory(rs.getString("category"));
        b.setOccasion(rs.getString("occasion"));
        b.setImagePath(rs.getString("image_path"));
        b.setFeatured(rs.getBoolean("featured"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) b.setCreatedAt(ts.toLocalDateTime());
        return b;
    }
}
