package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.User;
import com.bloomcart.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        User user = new User();
        user.setFullName(req.getParameter("fullName"));
        user.setEmail(req.getParameter("email"));
        user.setPhone(req.getParameter("phone"));
        user.setPassword(req.getParameter("password"));
        user.setAddress(req.getParameter("address"));

        try {
            userService.registerUser(user);
            res.sendRedirect(req.getContextPath() + "/login?success=registered");
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("user", user);
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("error", "Registration failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/auth/register.jsp").forward(req, res);
        }
    }
}
