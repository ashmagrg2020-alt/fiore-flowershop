package com.fiore.controller;

import com.fiore.entity.User;
import com.fiore.service.UserService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ProfileServlet - Allows logged-in users to view and update their profile.
 */
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("loggedInUser");
        req.setAttribute("user", userService.getById(user.getId())); // fresh from DB
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        User sessionUser = (User) req.getSession().getAttribute("loggedInUser");
        String action    = req.getParameter("action");

        if ("password".equals(action)) {
            String error = userService.changePassword(
                sessionUser.getId(),
                req.getParameter("currentPassword"),
                req.getParameter("newPassword"),
                req.getParameter("confirmNewPassword")
            );
            if (error != null) {
                req.setAttribute("pwError", error);
            } else {
                req.setAttribute("pwSuccess", "Password changed successfully.");
            }
        } else {
            String error = userService.updateProfile(
                sessionUser.getId(),
                req.getParameter("fullName"),
                req.getParameter("phone"),
                req.getParameter("address"),
                req.getParameter("dateOfBirth")
            );
            if (error != null) {
                req.setAttribute("profileError", error);
            } else {
                // Refresh session user
                User updated = userService.getById(sessionUser.getId());
                req.getSession().setAttribute("loggedInUser", updated);
                req.getSession().setAttribute("userName", updated.getFullName());
                req.setAttribute("profileSuccess", "Profile updated successfully.");
            }
        }

        req.setAttribute("user", userService.getById(sessionUser.getId()));
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }
}
