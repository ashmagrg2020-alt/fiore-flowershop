package com.fioreflowershop.controller.admin;

import com.fioreflowershop.service.UserService;
import com.fioreflowershop.service.UserService.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * AdminUserServlet - Controller for admin user management.
 * Handles viewing all users and approving/rejecting/deleting accounts.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            req.setAttribute("users", userService.getAllUsers());
            req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
        } catch (SQLException e) {
            System.err.println("AdminUserServlet doGet error: " + e.getMessage());
            req.setAttribute("errorMessage", "Error loading users.");
            req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        int userId;
        try {
            userId = Integer.parseInt(req.getParameter("userId"));
        } catch (NumberFormatException e) {
            req.getSession().setAttribute("errorMessage", "Invalid user ID.");
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        ServiceResult result;
        if ("approve".equals(action)) {
            result = userService.updateUserStatus(userId, "APPROVED");
        } else if ("reject".equals(action)) {
            result = userService.updateUserStatus(userId, "REJECTED");
        } else if ("delete".equals(action)) {
            result = userService.deleteUser(userId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/admin/users");
            return;
        }

        req.getSession().setAttribute(
                result.isSuccess() ? "successMessage" : "errorMessage",
                result.getMessage()
        );
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}