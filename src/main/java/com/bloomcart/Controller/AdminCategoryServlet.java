package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.Category;
import com.bloomcart.service.CategoryService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/categories")
public class AdminCategoryServlet extends HttpServlet {

    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("delete".equals(action)) {
                categoryService.deleteCategory(Integer.parseInt(req.getParameter("id")));
                res.sendRedirect(req.getContextPath() + "/admin/categories?success=deleted");
                return;
            }
            if ("edit".equals(action)) {
                req.setAttribute("category", categoryService.getCategoryById(Integer.parseInt(req.getParameter("id"))));
            }
            req.setAttribute("categories", categoryService.getAllCategories());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        Category cat = new Category();
        String idParam = req.getParameter("id");
        if (idParam != null && !idParam.isBlank()) cat.setId(Integer.parseInt(idParam));
        cat.setName(req.getParameter("name"));
        cat.setDescription(req.getParameter("description"));

        try {
            if ("add".equals(action)) categoryService.addCategory(cat);
            else if ("update".equals(action)) categoryService.updateCategory(cat);
            res.sendRedirect(req.getContextPath() + "/admin/categories?success=" + action);
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("category", cat);
            try { req.setAttribute("categories", categoryService.getAllCategories()); } catch (Exception ignored) {}
            req.getRequestDispatcher("/WEB-INF/views/admin/categories.jsp").forward(req, res);
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/admin/categories?error=failed");
        }
    }
}
