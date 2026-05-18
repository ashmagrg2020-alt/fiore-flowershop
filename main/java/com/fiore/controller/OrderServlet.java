package com.fiore.controller;

import com.fiore.entity.User;
import com.fiore.service.OrderService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * OrderServlet - Handles /order (place) and /orders (list user orders).
 */
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("loggedInUser");
        req.setAttribute("orders", orderService.getUserOrders(user.getId()));
        req.getRequestDispatcher("/WEB-INF/views/user/my-orders.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        User user = (User) req.getSession().getAttribute("loggedInUser");

        try {
            int    bouquetId = Integer.parseInt(req.getParameter("bouquetId"));
            int    quantity  = Integer.parseInt(req.getParameter("quantity"));
            String note      = req.getParameter("specialNote");

            String error = orderService.placeOrder(user.getId(), bouquetId, quantity, note);

            if (error != null) {
                req.getSession().setAttribute("flashError", error);
            } else {
                req.getSession().setAttribute("flashSuccess", "Order placed successfully! 🌸");
            }
            resp.sendRedirect(req.getContextPath() + "/orders");

        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid order data.");
        }
    }
}
