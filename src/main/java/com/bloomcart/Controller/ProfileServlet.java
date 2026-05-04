package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.User;
import com.bloomcart.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/profile")
public class ProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute("userId");
        try {
            req.setAttribute("profile", userService.getUserById(userId));
        } catch (Exception e) {
            req.setAttribute("error", "Could not load profile.");
        }
        req.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int userId = (int) req.getSession().getAttribute("userId");
        String action = req.getParameter("action");

        try {
            if ("updateProfile".equals(action)) {
                User user = userService.getUserById(userId);
                user.setFullName(req.getParameter("fullName"));
                user.setPhone(req.getParameter("phone"));
                user.setAddress(req.getParameter("address"));
                userService.updateProfile(user);
                req.getSession().setAttribute("userName", user.getFullName());
                req.setAttribute("success", "Profile updated successfully.");

            } else if ("changePassword".equals(action)) {
                String oldPw = req.getParameter("oldPassword");
                String newPw = req.getParameter("newPassword");
                String confirmPw = req.getParameter("confirmPassword");
                if (!newPw.equals(confirmPw)) throw new ValidationException("Passwords do not match.");
                userService.changePassword(userId, oldPw, newPw);
                req.setAttribute("success", "Password changed successfully.");
            }
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            req.setAttribute("error", "Update failed. Please try again.");
        }

        try {
            req.setAttribute("profile", userService.getUserById(userId));
        } catch (Exception ignored) {}
        req.getRequestDispatcher("/WEB-INF/views/customer/profile.jsp").forward(req, res);
    }
}
