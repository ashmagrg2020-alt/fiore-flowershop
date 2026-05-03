package com.fioreflowershop.controller;

import com.fioreflowershop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * ShopServlet - Displays the public shop/catalogue page.
 * Supports search by keyword and filter by category.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class ShopServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            String keyword    = req.getParameter("search");
            String categoryId = req.getParameter("category");

            if (keyword != null && !keyword.trim().isEmpty()) {
                req.setAttribute("products", productService.searchProducts(keyword.trim()));
                req.setAttribute("searchKeyword", keyword.trim());
            } else {
                req.setAttribute("products", productService.getActiveProducts());
            }
            req.setAttribute("categories", productService.getAllCategories());
        } catch (SQLException e) {
            System.err.println("ShopServlet error: " + e.getMessage());
            req.setAttribute("errorMessage", "Unable to load products. Please try again.");
        }
        req.getRequestDispatcher("/WEB-INF/views/common/shop.jsp").forward(req, resp);
    }
}