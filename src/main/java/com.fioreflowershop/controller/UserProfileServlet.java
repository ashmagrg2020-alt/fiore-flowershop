package com.fioreflowershop.controller.user;

import com.fioreflowershop.model.User;
import com.fioreflowershop.service.UserService;
import com.fioreflowershop.service.UserService.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * UserProfileServlet - Handles user profile viewing and editing.
 *
 * GET  /user/profile          → Show profile page
 * POST /user/profile?action=update   → Update profile info
 * POST /user/profile?action=password → Change password
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class UserProfileServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("loggedInUser");
        req.setAttribute("user", user);
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("loggedInUser");

        if ("update".equals(action)) {
            ServiceResult result = userService.updateProfile(
                    user.getUserId(),
                    req.getParameter("fullName"),
                    req.getParameter("phone"),
                    req.getParameter("address"),
                    req.getParameter("dateOfBirth")
            );
            if (result.isSuccess()) {
                // Update session with new user data
                session.setAttribute("loggedInUser", result.getData());
                session.setAttribute("userName", ((User) result.getData()).getFullName());
                req.setAttribute("successMessage", result.getMessage());
            } else {
                req.setAttribute("errorMessage", result.getMessage());
            }

        } else if ("password".equals(action)) {
            ServiceResult result = userService.changePassword(
                    user.getUserId(),
                    req.getParameter("currentPassword"),
                    req.getParameter("newPassword"),
                    req.getParameter("confirmNewPassword")
            );
            if (result.isSuccess()) {
                req.setAttribute("successMessage", result.getMessage());
            } else {
                req.setAttribute("errorMessage", result.getMessage());
            }
        }

        req.setAttribute("user", session.getAttribute("loggedInUser"));
        req.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(req, resp);
    }
}