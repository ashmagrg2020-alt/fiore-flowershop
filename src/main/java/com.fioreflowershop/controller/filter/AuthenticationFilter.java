package com.fioreflowershop.filter;

import com.fioreflowershop.model.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * AuthFilter - Servlet Filter for authentication and role-based authorization.
 *
 * Intercepts requests to protected /admin/* and /user/* URLs.
 * - Redirects unauthenticated users to the login page.
 * - Redirects unauthorized users (wrong role) to an error page.
 * - Manages session expiry by checking session validity.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Filter initialization — no setup required
    }

    /**
     * Applies authentication and authorization checks to every filtered request.
     *
     * @param request  the incoming servlet request
     * @param response the outgoing servlet response
     * @param chain    the filter chain for passing to next filter/servlet
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;
        HttpSession         session = req.getSession(false);

        String requestURI = req.getRequestURI();

        // Check if a valid session exists with a logged-in user
        boolean isLoggedIn = (session != null && session.getAttribute("loggedInUser") != null);

        if (!isLoggedIn) {
            // No session — redirect to login with a message
            req.getSession().setAttribute("errorMessage", "Please log in to access this page.");
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        // User is logged in — retrieve user from session
        User user = (User) session.getAttribute("loggedInUser");

        // ---- Role-Based Authorization ----
        if (requestURI.contains("/admin/")) {
            // Admin-only area
            if (!user.isAdmin()) {
                resp.sendRedirect(req.getContextPath() + "/WEB-INF/views/common/error403.jsp");
                return;
            }
        } else if (requestURI.contains("/user/")) {
            // User portal — must be a regular user OR admin (admin can view user pages too)
            if (!user.isApproved()) {
                session.invalidate();
                req.getSession().setAttribute("errorMessage", "Your account is not approved.");
                resp.sendRedirect(req.getContextPath() + "/login");
                return;
            }
        }

        // Authorization passed — continue the request
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // Filter cleanup — no teardown required
    }
}