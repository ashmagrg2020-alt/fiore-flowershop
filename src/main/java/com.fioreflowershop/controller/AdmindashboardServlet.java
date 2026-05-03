package com.fioreflowershop.controller.admin;

import com.fioreflowershop.dao.UserDAO;
import com.fioreflowershop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * AdminDashboardServlet - Displays the admin dashboard with summary statistics.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class AdminDashboardServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private final UserDAO        userDAO        = new UserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            int[] productStats = productService.getProductStats();
            int pendingUsers   = userDAO.findByStatus("PENDING").size();
            int totalUsers     = userDAO.findAll().size();

            req.setAttribute("totalProducts", productStats[0]);
            req.setAttribute("activeProducts", productStats[1]);
            req.setAttribute("pendingUsers",   pendingUsers);
            req.setAttribute("totalUsers",     totalUsers);
            req.setAttribute("recentProducts", productService.getAllProducts().subList(
                    0, Math.min(5, productService.getAllProducts().size())
            ));
        } catch (SQLException e) {
            System.err.println("AdminDashboardServlet error: " + e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}