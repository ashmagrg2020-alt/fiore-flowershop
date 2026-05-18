package com.fiore.service;

import com.fiore.dao.BouquetDAO;
import com.fiore.entity.Bouquet;
import com.fiore.util.ValidationUtil;

import java.util.List;

/**
 * BouquetService - Business logic for managing flower bouquets.
 */
public class BouquetService {

    private final BouquetDAO bouquetDAO = new BouquetDAO();

    // ── Read ───────────────────────────────────────────────────────────────

    public List<Bouquet> getAll()               { return bouquetDAO.findAll(); }
    public List<Bouquet> getFeatured()          { return bouquetDAO.findFeatured(); }
    public Bouquet       getById(int id)        { return bouquetDAO.findById(id); }
    public int           countBouquets()        { return bouquetDAO.countBouquets(); }
    public List<Bouquet> getTopSellers(int n)   { return bouquetDAO.findTopSellers(n); }

    /**
     * Search and filter bouquets for the shop page.
     *
     * @param keyword  name keyword (nullable)
     * @param category category filter (nullable / "All")
     */
    public List<Bouquet> search(String keyword, String category) {
        return bouquetDAO.search(keyword, category);
    }

    // ── Create ─────────────────────────────────────────────────────────────

    /**
     * Validates and creates a new bouquet.
     *
     * @return null on success, or an error message
     */
    public String createBouquet(String name, String description, String price,
                                String stock, String category, String occasion,
                                String imagePath, boolean featured) {
        String err = validate(name, description, price, stock, category, occasion);
        if (err != null) return err;

        Bouquet b = new Bouquet();
        b.setName(name.trim());
        b.setDescription(description.trim());
        b.setPrice(Double.parseDouble(price.trim()));
        b.setStockQuantity(Integer.parseInt(stock.trim()));
        b.setCategory(category.trim());
        b.setOccasion(occasion != null ? occasion.trim() : "");
        b.setImagePath(imagePath != null ? imagePath.trim() : "default.jpg");
        b.setFeatured(featured);

        return bouquetDAO.insertBouquet(b) > 0
               ? null : "Failed to create bouquet. Please try again.";
    }

    // ── Update ─────────────────────────────────────────────────────────────

    public String updateBouquet(int id, String name, String description, String price,
                                String stock, String category, String occasion,
                                String imagePath, boolean featured) {
        String err = validate(name, description, price, stock, category, occasion);
        if (err != null) return err;

        Bouquet b = bouquetDAO.findById(id);
        if (b == null) return "Bouquet not found.";

        b.setName(name.trim());
        b.setDescription(description.trim());
        b.setPrice(Double.parseDouble(price.trim()));
        b.setStockQuantity(Integer.parseInt(stock.trim()));
        b.setCategory(category.trim());
        b.setOccasion(occasion != null ? occasion.trim() : "");
        if (imagePath != null && !imagePath.trim().isEmpty()) b.setImagePath(imagePath.trim());
        b.setFeatured(featured);

        return bouquetDAO.updateBouquet(b) ? null : "Failed to update bouquet.";
    }

    // ── Delete ─────────────────────────────────────────────────────────────

    public boolean deleteBouquet(int id) { return bouquetDAO.deleteBouquet(id); }

    // ── Helpers ────────────────────────────────────────────────────────────

    private String validate(String name, String description, String price,
                            String stock, String category, String occasion) {
        if (ValidationUtil.isNullOrEmpty(name))     return "Bouquet name is required.";
        if (name.trim().length() > 120)             return "Bouquet name must be under 120 characters.";
        if (ValidationUtil.isNullOrEmpty(description)) return "Description is required.";
        if (!ValidationUtil.isPositiveNumber(price))   return "Price must be a positive number.";
        if (!ValidationUtil.isNonNegativeInt(stock))   return "Stock quantity must be 0 or more.";
        if (ValidationUtil.isNullOrEmpty(category))    return "Category is required.";
        return null;
    }
}
