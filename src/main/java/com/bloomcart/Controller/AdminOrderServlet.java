package com.bloomcart.controller;

import com.bloomcart.service.OrderService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/orders")
public class AdminOrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("view".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("order", orderService.getOrderById(id));
                req.getRequestDispatcher("/WEB-INF/views/admin/orderDetail.jsp").forward(req, res);
                return;
            }
            req.setAttribute("orders", orderService.getAllOrders());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            int orderId = Integer.parseInt(req.getParameter("orderId"));
            String status = req.getParameter("status");
            orderService.updateOrderStatus(orderId, status);
            res.sendRedirect(req.getContextPath() + "/admin/orders?success=updated");
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/admin/orders?error=failed");
        }
    }
}
