package com.fiore.controller.admin;

import com.fiore.service.BouquetService;
import com.fiore.service.OrderService;
import com.fiore.service.UserService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/** AdminReportServlet - Generates sales and availability reports for admin. */
public class AdminReportServlet extends HttpServlet {

    private final BouquetService bouquetService = new BouquetService();
    private final OrderService   orderService   = new OrderService();
    private final UserService    userService    = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setAttribute("allBouquets",  bouquetService.getAll());
        req.setAttribute("topSellers",   bouquetService.getTopSellers(10));
        req.setAttribute("totalRevenue", orderService.totalRevenue());
        req.setAttribute("totalOrders",  orderService.countOrders());
        req.setAttribute("totalUsers",   userService.countUsers());

        req.getRequestDispatcher("/WEB-INF/views/admin/reports.jsp").forward(req, resp);
    }
}
