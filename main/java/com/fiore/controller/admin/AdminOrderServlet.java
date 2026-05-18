package com.fiore.controller.admin;

import com.fiore.service.OrderService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/** AdminOrderServlet - Admin views and manages all orders. */
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("orders", orderService.getAllOrders());
        req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int    id     = Integer.parseInt(req.getParameter("id"));
        String status = req.getParameter("status");
        orderService.updateStatus(id, status);
        req.getSession().setAttribute("flashSuccess", "Order status updated.");
        resp.sendRedirect(req.getContextPath() + "/admin/orders");
    }
}
