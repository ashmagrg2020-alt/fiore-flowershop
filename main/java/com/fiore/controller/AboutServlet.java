package com.fiore.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/** AboutServlet - Serves the About Us page. */
public class AboutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/about.jsp").forward(req, resp);
    }
}
