package com.fiore.controller.admin;

import com.fiore.service.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * AdminDashboardServlet - Loads summary statistics for the admin dashboard.
 */
public class AdminDashboardServlet extends HttpServlet {

    private final UserService    userService    = new UserService();
    private final BouquetService bouquetService = new BouquetService();
    private final OrderService   orderService   = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("totalUsers",    userService.countUsers());
        req.setAttribute("totalBouquets", bouquetService.countBouquets());
        req.setAttribute("totalOrders",   orderService.countOrders());
        req.setAttribute("totalRevenue",  orderService.totalRevenue());
        req.setAttribute("pendingUsers",  userService.getPending());
        req.setAttribute("recentOrders",  orderService.getAllOrders());
        req.setAttribute("topSellers",    bouquetService.getTopSellers(5));

        req.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(req, resp);
    }
}
