package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.User;
import com.bloomcart.service.UserService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        // Pre-fill email from cookie
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if ("rememberedEmail".equals(c.getName())) {
                    req.setAttribute("rememberedEmail", c.getValue());
                }
            }
        }
        req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("remember");

        try {
            User user = userService.login(email, password);

            HttpSession session = req.getSession(true);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getId());
            session.setAttribute("userRole", user.getRole());
            session.setAttribute("userName", user.getFullName());

            // Cookie: remember email only
            if ("on".equals(remember)) {
                Cookie emailCookie = new Cookie("rememberedEmail", email);
                emailCookie.setMaxAge(7 * 24 * 60 * 60);
                emailCookie.setPath(req.getContextPath());
                res.addCookie(emailCookie);
            } else {
                Cookie remove = new Cookie("rememberedEmail", "");
                remove.setMaxAge(0);
                remove.setPath(req.getContextPath());
                res.addCookie(remove);
            }

            if ("ADMIN".equals(user.getRole())) {
                res.sendRedirect(req.getContextPath() + "/admin/dashboard");
            } else {
                res.sendRedirect(req.getContextPath() + "/customer/home");
            }

        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("rememberedEmail", email);
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
        } catch (Exception e) {
            req.setAttribute("error", "Login failed. Please try again.");
            req.getRequestDispatcher("/WEB-INF/views/auth/login.jsp").forward(req, res);
        }
    }
}
