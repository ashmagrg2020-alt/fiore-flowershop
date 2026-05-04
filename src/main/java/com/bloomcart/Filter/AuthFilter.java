package com.bloomcart.filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter(urlPatterns = {"/admin/*", "/customer/*"})
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        HttpSession session = request.getSession(false);

        String uri = request.getRequestURI();
        String contextPath = request.getContextPath();

        boolean loggedIn = (session != null && session.getAttribute("loggedInUser") != null);
        String role = loggedIn ? (String) session.getAttribute("userRole") : null;

        if (uri.startsWith(contextPath + "/admin/")) {
            if (!loggedIn || !"ADMIN".equals(role)) {
                response.sendRedirect(contextPath + "/login?error=unauthorized");
                return;
            }
        } else if (uri.startsWith(contextPath + "/customer/")) {
            if (!loggedIn || !"CUSTOMER".equals(role)) {
                response.sendRedirect(contextPath + "/login?error=unauthorized");
                return;
            }
        }

        chain.doFilter(req, res);
    }

    @Override public void init(FilterConfig fc) {}
    @Override public void destroy() {}
}
