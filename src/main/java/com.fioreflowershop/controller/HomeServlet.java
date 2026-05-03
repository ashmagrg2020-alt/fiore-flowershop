package com.fioreflowershop.controller;

import com.fioreflowershop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * HomeServlet - Displays the public home/landing page.
 * Loads featured bouquets for the homepage showcase.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class HomeServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("featuredProducts", productService.getFeaturedProducts());
        } catch (SQLException e) {
            System.err.println("HomeServlet error: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/common/home.jsp").forward(req, resp);
    }
}