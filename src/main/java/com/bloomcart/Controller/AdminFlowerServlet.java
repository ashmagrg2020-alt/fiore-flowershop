package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.Flower;
import com.bloomcart.service.CategoryService;
import com.bloomcart.service.FlowerService;
import com.bloomcart.service.OccasionService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/flowers")
public class AdminFlowerServlet extends HttpServlet {

    private final FlowerService flowerService = new FlowerService();
    private final CategoryService categoryService = new CategoryService();
    private final OccasionService occasionService = new OccasionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("edit".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("flower", flowerService.getFlowerById(id));
                req.setAttribute("categories", categoryService.getAllCategories());
                req.setAttribute("occasions", occasionService.getAllOccasions());
                req.getRequestDispatcher("/WEB-INF/views/admin/flowerForm.jsp").forward(req, res);
                return;
            } else if ("delete".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                flowerService.deleteFlower(id);
                res.sendRedirect(req.getContextPath() + "/admin/flowers?success=deleted");
                return;
            } else if ("add".equals(action)) {
                req.setAttribute("categories", categoryService.getAllCategories());
                req.setAttribute("occasions", occasionService.getAllOccasions());
                req.getRequestDispatcher("/WEB-INF/views/admin/flowerForm.jsp").forward(req, res);
                return;
            }
            req.setAttribute("flowers", flowerService.getAllFlowers());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/flowers.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");

        Flower flower = new Flower();
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isBlank()) flower.setId(Integer.parseInt(idParam));
        flower.setName(req.getParameter("name"));
        flower.setDescription(req.getParameter("description"));
        flower.setCategoryId(parseIntOrZero(req.getParameter("categoryId")));
        flower.setOccasionId(parseIntOrZero(req.getParameter("occasionId")));
        flower.setPrice(parseDoubleOrZero(req.getParameter("price")));
        flower.setStockQuantity(parseIntOrZero(req.getParameter("stockQuantity")));
        flower.setImageUrl(req.getParameter("imageUrl"));
        flower.setFeatured("on".equals(req.getParameter("featured")));

        try {
            if ("add".equals(action)) {
                flowerService.addFlower(flower);
                res.sendRedirect(req.getContextPath() + "/admin/flowers?success=added");
            } else if ("update".equals(action)) {
                flowerService.updateFlower(flower);
                res.sendRedirect(req.getContextPath() + "/admin/flowers?success=updated");
            }
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("flower", flower);
            try {
                req.setAttribute("categories", categoryService.getAllCategories());
                req.setAttribute("occasions", occasionService.getAllOccasions());
            } catch (Exception ignored) {}
            req.getRequestDispatcher("/WEB-INF/views/admin/flowerForm.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/admin/flowers?error=failed");
        }
    }

    private int parseIntOrZero(String s) {
        try { return Integer.parseInt(s); } catch (Exception e) { return 0; }
    }
    private double parseDoubleOrZero(String s) {
        try { return Double.parseDouble(s); } catch (Exception e) { return 0; }
    }
}
