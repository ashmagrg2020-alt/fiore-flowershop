package com.fiore.filter;

import com.fiore.entity.User;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminFilter - Protects all /admin/* URLs.
 * Redirects non-admins to the login page or a 403 error page.
 */
public class AdminFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session    = req.getSession(false);
        User        loggedUser = session != null ? (User) session.getAttribute("loggedInUser") : null;

        if (loggedUser != null && loggedUser.isAdmin()) {
            chain.doFilter(request, response);
        } else if (loggedUser == null) {
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            // Logged in but not admin
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }

    @Override
    public void destroy() {}
}
