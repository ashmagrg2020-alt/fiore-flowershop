package com.fioreflowershop.controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LogoutServlet - Handles user logout by invalidating the session.
 * Redirects to the login page with a success message.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // Redirect to login with goodbye message
        req.getSession(true).setAttribute("successMessage", "You have been logged out successfully.");
        resp.sendRedirect(req.getContextPath() + "/login");
    }
}