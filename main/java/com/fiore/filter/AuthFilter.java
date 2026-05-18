package com.fiore.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AuthFilter - Protects user-only URLs.
 * Redirects unauthenticated requests to the login page.
 */
public class AuthFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest  req  = (HttpServletRequest)  request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        boolean loggedIn    = session != null && session.getAttribute("loggedInUser") != null;

        if (loggedIn) {
            chain.doFilter(request, response);
        } else {
            // Save the originally requested URL so we can redirect after login
            String requestedUrl = req.getRequestURI();
            resp.sendRedirect(req.getContextPath() + "/login?redirect=" + requestedUrl);
        }
    }

    @Override
    public void destroy() {}
}
