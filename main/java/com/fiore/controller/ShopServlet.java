package com.fiore.controller;

import com.fiore.service.BouquetService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ShopServlet - Serves the public shop page with search and filter support.
 */
public class ShopServlet extends HttpServlet {

    private final BouquetService bouquetService = new BouquetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String keyword  = req.getParameter("search");
        String category = req.getParameter("category");

        req.setAttribute("bouquets",    bouquetService.search(keyword, category));
        req.setAttribute("keyword",     keyword  != null ? keyword  : "");
        req.setAttribute("activeCategory", category != null ? category : "All");

        req.getRequestDispatcher("/WEB-INF/views/shop.jsp").forward(req, resp);
    }
}
