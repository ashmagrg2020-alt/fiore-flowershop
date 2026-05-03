package com.fioreflowershop.controller.admin;

import com.fioreflowershop.model.Product;
import com.fioreflowershop.service.ProductService;
import com.fioreflowershop.service.UserService.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * AdminProductServlet - Controller for admin bouquet (product) management.
 * Supports full CRUD operations via action parameter.
 *
 * GET  /admin/products             → List all products
 * GET  /admin/products?action=add  → Show add form
 * GET  /admin/products?action=edit&id=X → Show edit form
 * POST /admin/products?action=add  → Process add
 * POST /admin/products?action=edit → Process edit
 * POST /admin/products?action=delete&id=X → Process delete
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class AdminProductServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                // Show the add bouquet form
                req.setAttribute("categories", productService.getAllCategories());
                req.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(req, resp);

            } else if ("edit".equals(action)) {
                // Show the edit bouquet form pre-populated
                int productId = Integer.parseInt(req.getParameter("id"));
                Product product = productService.getProductById(productId);
                if (product == null) {
                    req.setAttribute("errorMessage", "Product not found.");
                    resp.sendRedirect(req.getContextPath() + "/admin/products");
                    return;
                }
                req.setAttribute("product", product);
                req.setAttribute("categories", productService.getAllCategories());
                req.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(req, resp);

            } else {
                // Default: List all products
                req.setAttribute("products", productService.getAllProducts());
                req.setAttribute("categories", productService.getAllCategories());
                req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
            }
        } catch (NumberFormatException e) {
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (SQLException e) {
            System.err.println("AdminProductServlet doGet error: " + e.getMessage());
            req.setAttribute("errorMessage", "Database error: " + e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/admin/products.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        try {
            if ("add".equals(action)) {
                // ---- CREATE ----
                ServiceResult result = productService.addProduct(
                        req.getParameter("productName"),
                        req.getParameter("sku"),
                        req.getParameter("categoryId"),
                        req.getParameter("occasionId"),
                        req.getParameter("description"),
                        req.getParameter("price"),
                        req.getParameter("stockQty"),
                        req.getParameter("imageUrl"),
                        "on".equals(req.getParameter("isFeatured"))
                );

                if (result.isSuccess()) {
                    req.getSession().setAttribute("successMessage", result.getMessage());
                    resp.sendRedirect(req.getContextPath() + "/admin/products");
                } else {
                    req.setAttribute("errorMessage", result.getMessage());
                    req.setAttribute("categories", productService.getAllCategories());
                    // Re-populate form fields
                    req.setAttribute("formName",        req.getParameter("productName"));
                    req.setAttribute("formSku",         req.getParameter("sku"));
                    req.setAttribute("formDescription", req.getParameter("description"));
                    req.setAttribute("formPrice",       req.getParameter("price"));
                    req.setAttribute("formQty",         req.getParameter("stockQty"));
                    req.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(req, resp);
                }

            } else if ("edit".equals(action)) {
                // ---- UPDATE ----
                int productId = Integer.parseInt(req.getParameter("productId"));
                ServiceResult result = productService.updateProduct(
                        productId,
                        req.getParameter("productName"),
                        req.getParameter("sku"),
                        req.getParameter("categoryId"),
                        req.getParameter("occasionId"),
                        req.getParameter("description"),
                        req.getParameter("price"),
                        req.getParameter("stockQty"),
                        req.getParameter("imageUrl"),
                        "on".equals(req.getParameter("isFeatured")),
                        req.getParameter("status")
                );

                if (result.isSuccess()) {
                    req.getSession().setAttribute("successMessage", result.getMessage());
                    resp.sendRedirect(req.getContextPath() + "/admin/products");
                } else {
                    req.setAttribute("errorMessage", result.getMessage());
                    req.setAttribute("product", productService.getProductById(productId));
                    req.setAttribute("categories", productService.getAllCategories());
                    req.getRequestDispatcher("/WEB-INF/views/admin/product_form.jsp").forward(req, resp);
                }

            } else if ("delete".equals(action)) {
                // ---- DELETE ----
                int productId = Integer.parseInt(req.getParameter("id"));
                ServiceResult result = productService.deleteProduct(productId);
                req.getSession().setAttribute(
                        result.isSuccess() ? "successMessage" : "errorMessage",
                        result.getMessage()
                );
                resp.sendRedirect(req.getContextPath() + "/admin/products");

            } else {
                resp.sendRedirect(req.getContextPath() + "/admin/products");
            }

        } catch (NumberFormatException e) {
            req.setAttribute("errorMessage", "Invalid ID parameter.");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        } catch (SQLException e) {
            System.err.println("AdminProductServlet doPost error: " + e.getMessage());
            req.getSession().setAttribute("errorMessage", "Database error occurred.");
            resp.sendRedirect(req.getContextPath() + "/admin/products");
        }
    }
}