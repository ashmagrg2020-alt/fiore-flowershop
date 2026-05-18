package com.fiore.controller;

import com.fiore.service.BouquetService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * HomeServlet - Serves the public home / landing page.
 */
public class HomeServlet extends HttpServlet {

    private final BouquetService bouquetService = new BouquetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // Load featured bouquets for the homepage grid
        req.setAttribute("featuredBouquets", bouquetService.getFeatured());

        req.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(req, resp);
    }
}
