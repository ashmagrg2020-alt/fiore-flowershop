package com.bloomcart.controller;

import com.bloomcart.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("activate".equals(action) || "deactivate".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                userService.updateUserStatus(id, "activate".equals(action) ? "ACTIVE" : "INACTIVE");
                res.sendRedirect(req.getContextPath() + "/admin/users?success=" + action);
                return;
            }
            if ("delete".equals(action)) {
                userService.deleteUser(Integer.parseInt(req.getParameter("id")));
                res.sendRedirect(req.getContextPath() + "/admin/users?success=deleted");
                return;
            }
            req.setAttribute("users", userService.getAllUsers());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, res);
    }
}
