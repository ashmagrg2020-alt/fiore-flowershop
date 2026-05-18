package com.fiore.controller.admin;

import com.fiore.service.UserService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminUserServlet - Manages user registrations (approve / reject / delete).
 */
public class AdminUserServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("users",        userService.getAllUsers());
        req.setAttribute("pendingUsers", userService.getPending());
        req.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        int    id     = Integer.parseInt(req.getParameter("id"));

        switch (action) {
            case "approve": userService.approveUser(id);
                req.getSession().setAttribute("flashSuccess", "User approved."); break;
            case "reject":  userService.rejectUser(id);
                req.getSession().setAttribute("flashSuccess", "User rejected."); break;
            case "delete":  userService.deleteUser(id);
                req.getSession().setAttribute("flashSuccess", "User deleted."); break;
        }
        resp.sendRedirect(req.getContextPath() + "/admin/users");
    }
}
