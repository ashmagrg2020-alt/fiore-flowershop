package com.fiore.controller;

import com.fiore.service.BouquetService;
import com.fiore.entity.Bouquet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * BouquetDetailServlet - Shows the detail page for a single bouquet.
 */
public class BouquetDetailServlet extends HttpServlet {

    private final BouquetService bouquetService = new BouquetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            Bouquet b = bouquetService.getById(id);
            if (b == null) { resp.sendError(404); return; }
            req.setAttribute("bouquet", b);
            req.getRequestDispatcher("/WEB-INF/views/bouquet-detail.jsp").forward(req, resp);
        } catch (NumberFormatException e) {
            resp.sendError(400);
        }
    }
}
