package com.fioreflowershop.controller;

import com.fioreflowershop.model.User;
import com.fioreflowershop.service.UserService;
import com.fioreflowershop.service.UserService.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * LoginServlet - Controller for user login functionality.
 *
 * GET  /login → Displays the login page.
 * POST /login → Authenticates user credentials.
 *               On success: creates session, sets cookie, redirects to appropriate dashboard.
 *               On failure: redisplays login with error message.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class LoginServlet extends HttpServlet {

    private final UserService userService = new UserService();

    // Cookie name for remembering email
    private static final String REMEMBER_COOKIE = "fiore_remember_email";

    /**
     * Handles GET request — displays the login form.
     * Populates remembered email from cookie if present.
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        // If already logged in, redirect to appropriate dashboard
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute("loggedInUser") != null) {
            User user = (User) session.getAttribute("loggedInUser");
            redirectToDashboard(user, req, resp);
            return;
        }

        // Check for "remember me" cookie
        String rememberedEmail = "";
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (REMEMBER_COOKIE.equals(c.getName())) {
                    rememberedEmail = c.getValue();
                }
            }
        }

        req.setAttribute("rememberedEmail", rememberedEmail);
        req.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(req, resp);
    }

    /**
     * Handles POST request — processes login form submission.
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String email      = req.getParameter("email");
        String password   = req.getParameter("password");
        String rememberMe = req.getParameter("rememberMe");

        ServiceResult result = userService.login(email, password);

        if (result.isSuccess()) {
            User user = (User) result.getData();

            // Create new session and store user
            HttpSession session = req.getSession(true);
            session.setAttribute("loggedInUser", user);
            session.setAttribute("userId", user.getUserId());
            session.setAttribute("userRole", user.getRoleName());
            session.setAttribute("userName", user.getFullName());

            // Handle "Remember Me" cookie
            if ("on".equals(rememberMe)) {
                Cookie cookie = new Cookie(REMEMBER_COOKIE, user.getEmail());
                cookie.setMaxAge(7 * 24 * 60 * 60); // 7 days
                cookie.setPath(req.getContextPath());
                resp.addCookie(cookie);
            } else {
                // Clear cookie if not checked
                Cookie cookie = new Cookie(REMEMBER_COOKIE, "");
                cookie.setMaxAge(0);
                cookie.setPath(req.getContextPath());
                resp.addCookie(cookie);
            }

            redirectToDashboard(user, req, resp);

        } else {
            req.setAttribute("errorMessage", result.getMessage());
            req.setAttribute("emailValue", email);
            req.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(req, resp);
        }
    }

    /**
     * Redirects user to the appropriate dashboard based on their role.
     *
     * @param user the authenticated user
     * @param req  the HTTP request
     * @param resp the HTTP response
     */
    private void redirectToDashboard(User user, HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        if (user.isAdmin()) {
            resp.sendRedirect(req.getContextPath() + "/admin/dashboard");
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/dashboard");
        }
    }
}