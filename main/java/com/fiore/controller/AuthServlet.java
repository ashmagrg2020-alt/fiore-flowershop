package com.fiore.controller;

import com.fiore.entity.User;
import com.fiore.service.UserService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthServlet - Handles /login, /logout, and /register endpoints.
 */
public class AuthServlet extends HttpServlet {

    private final UserService userService = new UserService();

    // ── GET ────────────────────────────────────────────────────────────────

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String path = req.getServletPath();

        if ("/logout".equals(path)) {
            HttpSession session = req.getSession(false);
            if (session != null) session.invalidate();
            resp.sendRedirect(req.getContextPath() + "/home");
            return;
        }

        if ("/register".equals(path)) {
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
        } else {
            // /login
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
        }
    }

    // ── POST ───────────────────────────────────────────────────────────────

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String path = req.getServletPath();

        if ("/register".equals(path)) {
            handleRegister(req, resp);
        } else {
            handleLogin(req, resp);
        }
    }

    // ── Login ──────────────────────────────────────────────────────────────

    private void handleLogin(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email    = req.getParameter("email");
        String password = req.getParameter("password");

        User user = userService.login(email, password);

        if (user == null) {
            req.setAttribute("error", "Invalid email/password, or your account is pending approval.");
            req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
            return;
        }

        // Store user in session
        HttpSession session = req.getSession(true);
        session.setAttribute("loggedInUser", user);
        session.setAttribute("userId",   user.getId());
        session.setAttribute("userRole", user.getRole());
        session.setAttribute("userName", user.getFullName());

        // Redirect based on role
        if (user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            String redirect = req.getParameter("redirect");
            resp.sendRedirect(redirect != null && !redirect.isEmpty()
                              ? redirect
                              : req.getContextPath() + "/home");
        }
    }

    // ── Register ───────────────────────────────────────────────────────────

    private void handleRegister(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fullName  = req.getParameter("fullName");
        String email     = req.getParameter("email");
        String phone     = req.getParameter("phone");
        String password  = req.getParameter("password");
        String confirm   = req.getParameter("confirmPassword");
        String address   = req.getParameter("address");
        String dob       = req.getParameter("dateOfBirth");

        String error = userService.registerUser(fullName, email, phone, password, confirm, address, dob);

        if (error != null) {
            req.setAttribute("error",    error);
            req.setAttribute("fullName", fullName);
            req.setAttribute("email",    email);
            req.setAttribute("phone",    phone);
            req.setAttribute("address",  address);
            req.setAttribute("dob",      dob);
            req.getRequestDispatcher("/WEB-INF/views/register.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("success", "Registration successful! Please wait for admin approval before logging in.");
        req.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(req, resp);
    }
}
