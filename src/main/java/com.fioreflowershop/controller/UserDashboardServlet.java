package com.fioreflowershop.controller.user;

import com.fioreflowershop.model.User;
import com.fioreflowershop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * UserDashboardServlet - Displays the user's personal dashboard.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class UserDashboardServlet extends HttpServlet {

    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            User user = (User) req.getSession().getAttribute("loggedInUser");
            req.setAttribute("featuredProducts", productService.getFeaturedProducts());
            req.setAttribute("user", user);
        } catch (SQLException e) {
            System.err.println("UserDashboardServlet error: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/user/dashboard.jsp").forward(req, resp);
    }
}