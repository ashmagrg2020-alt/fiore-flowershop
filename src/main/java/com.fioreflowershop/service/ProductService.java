package com.fioreflowershop.service;

import com.fioreflowershop.dao.CategoryDAO;
import com.fioreflowershop.dao.ProductDAO;
import com.fioreflowershop.model.Category;
import com.fioreflowershop.model.Product;
import com.fioreflowershop.service.UserService.ServiceResult;
import com.fioreflowershop.util.ValidationUtil;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

/**
 * ProductService - Service class encapsulating business logic for product management.
 * Handles validation and coordinates between Controller and ProductDAO.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class ProductService {

    private final ProductDAO  productDAO  = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();

    /**
     * Adds a new product after validating all input.
     *
     * @param name        product name
     * @param sku         product SKU (must be unique)
     * @param categoryId  category ID
     * @param occasionId  occasion ID (can be null)
     * @param description product description
     * @param priceStr    price as string
     * @param qtyStr      stock quantity as string
     * @param imageUrl    image URL (optional)
     * @param featured    whether product is featured
     * @return ServiceResult with success/error message
     */
    public ServiceResult addProduct(String name, String sku, String categoryId,
                                    String occasionId, String description,
                                    String priceStr, String qtyStr,
                                    String imageUrl, boolean featured) {
        try {
            // ---- Validation ----
            if (ValidationUtil.isNullOrEmpty(name)) {
                return ServiceResult.error("Product name is required.");
            }
            if (!ValidationUtil.isValidSKU(sku)) {
                return ServiceResult.error("SKU format must be SKU-XXX (e.g., SKU-007).");
            }
            if (ValidationUtil.isNullOrEmpty(categoryId)) {
                return ServiceResult.error("Please select a category.");
            }
            if (!ValidationUtil.isValidPrice(priceStr)) {
                return ServiceResult.error("Price must be a positive number (e.g., 999.00).");
            }
            if (!ValidationUtil.isValidQuantity(qtyStr)) {
                return ServiceResult.error("Stock quantity must be a non-negative integer.");
            }

            // ---- Duplicate SKU Check ----
            if (productDAO.skuExists(sku.trim().toUpperCase())) {
                return ServiceResult.error("A product with this SKU already exists.");
            }

            // ---- Build Product ----
            Product product = new Product();
            product.setProductName(ValidationUtil.sanitize(name));
            product.setSku(sku.trim().toUpperCase());
            product.setCategoryId(Integer.parseInt(categoryId.trim()));
            product.setOccasionId(
                    (ValidationUtil.isNullOrEmpty(occasionId)) ? null : Integer.parseInt(occasionId.trim())
            );
            product.setDescription(ValidationUtil.sanitize(description));
            product.setPrice(new BigDecimal(priceStr.trim()));
            product.setStockQty(Integer.parseInt(qtyStr.trim()));
            product.setImageUrl(ValidationUtil.sanitize(imageUrl));
            product.setFeatured(featured);
            product.setStatus("ACTIVE");

            boolean success = productDAO.addProduct(product);
            return success
                    ? ServiceResult.success("Bouquet added successfully!")
                    : ServiceResult.error("Failed to add product. Please try again.");

        } catch (NumberFormatException e) {
            return ServiceResult.error("Invalid numeric value entered.");
        } catch (SQLException e) {
            System.err.println("ProductService.addProduct() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    /**
     * Updates an existing product after validating input.
     *
     * @param productId   the product ID to update
     * @param name        updated name
     * @param sku         updated SKU
     * @param categoryId  updated category
     * @param occasionId  updated occasion
     * @param description updated description
     * @param priceStr    updated price
     * @param qtyStr      updated stock quantity
     * @param imageUrl    updated image URL
     * @param featured    updated featured flag
     * @param status      updated status (ACTIVE/INACTIVE)
     * @return ServiceResult
     */
    public ServiceResult updateProduct(int productId, String name, String sku,
                                       String categoryId, String occasionId,
                                       String description, String priceStr, String qtyStr,
                                       String imageUrl, boolean featured, String status) {
        try {
            if (ValidationUtil.isNullOrEmpty(name)) {
                return ServiceResult.error("Product name is required.");
            }
            if (!ValidationUtil.isValidSKU(sku)) {
                return ServiceResult.error("SKU format must be SKU-XXX.");
            }
            if (!ValidationUtil.isValidPrice(priceStr)) {
                return ServiceResult.error("Price must be a positive number.");
            }
            if (!ValidationUtil.isValidQuantity(qtyStr)) {
                return ServiceResult.error("Stock quantity must be a non-negative integer.");
            }

            // Check SKU uniqueness excluding current product
            if (productDAO.skuExistsExcluding(sku.trim().toUpperCase(), productId)) {
                return ServiceResult.error("Another product already uses this SKU.");
            }

            Product product = new Product();
            product.setProductId(productId);
            product.setProductName(ValidationUtil.sanitize(name));
            product.setSku(sku.trim().toUpperCase());
            product.setCategoryId(Integer.parseInt(categoryId.trim()));
            product.setOccasionId(
                    (ValidationUtil.isNullOrEmpty(occasionId)) ? null : Integer.parseInt(occasionId.trim())
            );
            product.setDescription(ValidationUtil.sanitize(description));
            product.setPrice(new BigDecimal(priceStr.trim()));
            product.setStockQty(Integer.parseInt(qtyStr.trim()));
            product.setImageUrl(ValidationUtil.sanitize(imageUrl));
            product.setFeatured(featured);
            product.setStatus(status);

            boolean success = productDAO.updateProduct(product);
            return success
                    ? ServiceResult.success("Bouquet updated successfully!")
                    : ServiceResult.error("Failed to update product.");

        } catch (NumberFormatException e) {
            return ServiceResult.error("Invalid numeric value entered.");
        } catch (SQLException e) {
            System.err.println("ProductService.updateProduct() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    /**
     * Deletes a product by ID.
     *
     * @param productId the product ID to delete
     * @return ServiceResult
     */
    public ServiceResult deleteProduct(int productId) {
        try {
            boolean success = productDAO.deleteProduct(productId);
            return success
                    ? ServiceResult.success("Bouquet deleted successfully.")
                    : ServiceResult.error("Failed to delete product.");
        } catch (SQLException e) {
            System.err.println("ProductService.deleteProduct() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    /**
     * Retrieves all products (for admin view).
     */
    public List<Product> getAllProducts() throws SQLException {
        return productDAO.findAll();
    }

    /**
     * Retrieves active products (for shop page).
     */
    public List<Product> getActiveProducts() throws SQLException {
        return productDAO.findAllActive();
    }

    /**
     * Retrieves featured products (for home page).
     */
    public List<Product> getFeaturedProducts() throws SQLException {
        return productDAO.findFeatured();
    }

    /**
     * Retrieves a single product by ID.
     */
    public Product getProductById(int id) throws SQLException {
        return productDAO.findById(id);
    }

    /**
     * Searches products by keyword.
     */
    public List<Product> searchProducts(String keyword) throws SQLException {
        return productDAO.searchProducts(keyword);
    }

    /**
     * Retrieves all categories for dropdown menus.
     */
    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    /**
     * Gets product statistics for the admin dashboard.
     *
     * @return int[]{total, active, inactive}
     */
    public int[] getProductStats() throws SQLException {
        return productDAO.getProductStats();
    }
}