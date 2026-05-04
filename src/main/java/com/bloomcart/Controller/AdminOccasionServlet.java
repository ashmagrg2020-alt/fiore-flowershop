package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.Occasion;
import com.bloomcart.service.OccasionService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/occasions")
public class AdminOccasionServlet extends HttpServlet {

    private final OccasionService occasionService = new OccasionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("delete".equals(action)) {
                occasionService.deleteOccasion(Integer.parseInt(req.getParameter("id")));
                res.sendRedirect(req.getContextPath() + "/admin/occasions?success=deleted");
                return;
            }
            if ("edit".equals(action)) {
                req.setAttribute("occasion", occasionService.getOccasionById(Integer.parseInt(req.getParameter("id"))));
            }
            req.setAttribute("occasions", occasionService.getAllOccasions());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/occasions.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Occasion occ = new Occasion();
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isBlank()) occ.setId(Integer.parseInt(idParam));
        occ.setName(req.getParameter("name"));
        occ.setDescription(req.getParameter("description"));

        try {
            if ("add".equals(action)) occasionService.addOccasion(occ);
            else if ("update".equals(action)) occasionService.updateOccasion(occ);
            res.sendRedirect(req.getContextPath() + "/admin/occasions?success=" + action);
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("occasion", occ);
            try { req.setAttribute("occasions", occasionService.getAllOccasions()); } catch (Exception ignored) {}
            req.getRequestDispatcher("/WEB-INF/views/admin/occasions.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/admin/occasions?error=failed");
        }
    }
}
