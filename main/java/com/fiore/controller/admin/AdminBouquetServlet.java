package com.fiore.controller.admin;

import com.fiore.service.BouquetService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminBouquetServlet - Admin CRUD operations for flower bouquets.
 * GET  /admin/bouquets          -> list all
 * GET  /admin/bouquets?action=add -> show add form
 * GET  /admin/bouquets?action=edit&id=X -> show edit form
 * POST /admin/bouquets?action=add    -> create
 * POST /admin/bouquets?action=edit   -> update
 * POST /admin/bouquets?action=delete -> delete
 */
public class AdminBouquetServlet extends HttpServlet {

    private final BouquetService bouquetService = new BouquetService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");

        if ("add".equals(action)) {
            req.getRequestDispatcher("/WEB-INF/views/admin/bouquet-form.jsp").forward(req, resp);

        } else if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            req.setAttribute("bouquet", bouquetService.getById(id));
            req.getRequestDispatcher("/WEB-INF/views/admin/bouquet-form.jsp").forward(req, resp);

        } else {
            req.setAttribute("bouquets", bouquetService.getAll());
            req.getRequestDispatcher("/WEB-INF/views/admin/bouquets.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        if ("delete".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            bouquetService.deleteBouquet(id);
            req.getSession().setAttribute("flashSuccess", "Bouquet deleted.");
            resp.sendRedirect(req.getContextPath() + "/admin/bouquets");
            return;
        }

        boolean featured = "on".equals(req.getParameter("featured"));
        String  name     = req.getParameter("name");
        String  desc     = req.getParameter("description");
        String  price    = req.getParameter("price");
        String  stock    = req.getParameter("stockQuantity");
        String  cat      = req.getParameter("category");
        String  occ      = req.getParameter("occasion");
        String  img      = req.getParameter("imagePath");

        String error;

        if ("edit".equals(action)) {
            int id = Integer.parseInt(req.getParameter("id"));
            error = bouquetService.updateBouquet(id, name, desc, price, stock, cat, occ, img, featured);
            if (error != null) {
                req.setAttribute("error",   error);
                req.setAttribute("bouquet", bouquetService.getById(id));
                req.getRequestDispatcher("/WEB-INF/views/admin/bouquet-form.jsp").forward(req, resp);
                return;
            }
            req.getSession().setAttribute("flashSuccess", "Bouquet updated successfully.");
        } else {
            error = bouquetService.createBouquet(name, desc, price, stock, cat, occ, img, featured);
            if (error != null) {
                req.setAttribute("error", error);
                req.getRequestDispatcher("/WEB-INF/views/admin/bouquet-form.jsp").forward(req, resp);
                return;
            }
            req.getSession().setAttribute("flashSuccess", "Bouquet added successfully.");
        }

        resp.sendRedirect(req.getContextPath() + "/admin/bouquets");
    }
}
