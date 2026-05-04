package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.CartItem;
import com.bloomcart.service.CartService;
import com.bloomcart.service.OrderService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;

@WebServlet("/customer/orders")
public class OrderServlet extends HttpServlet {

    private final OrderService orderService = new OrderService();
    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        int userId = (int) req.getSession().getAttribute("userId");
        try {
            req.setAttribute("orders", orderService.getOrdersByUser(userId));
        } catch (Exception e) {
            req.setAttribute("error", "Could not load orders.");
        }
        req.getRequestDispatcher("/WEB-INF/views/customer/orders.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        int userId = (int) req.getSession().getAttribute("userId");
        String action = req.getParameter("action");

        if ("place".equals(action)) {
            HttpSession session = req.getSession();
            List<CartItem> cartItems = cartService.getCart(session);
            String address = req.getParameter("deliveryAddress");
            String notes = req.getParameter("notes");

            try {
                int orderId = orderService.placeOrder(userId, cartItems, address, notes);
                cartService.clearCart(session);
                res.sendRedirect(req.getContextPath() + "/customer/orders?success=placed&orderId=" + orderId);
            } catch (ValidationException e) {
                req.setAttribute("error", e.getMessage());
                req.setAttribute("cartItems", cartItems);
                req.setAttribute("cartTotal", cartService.getTotal(session));
                req.getRequestDispatcher("/WEB-INF/views/customer/checkout.jsp").forward(req, res);
            } catch (Exception e) {
                req.setAttribute("error", "Order placement failed. Try again.");
                req.getRequestDispatcher("/WEB-INF/views/customer/checkout.jsp").forward(req, res);
            }
        }
    }
}
