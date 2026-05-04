package com.bloomcart.controller;

import com.bloomcart.model.Flower;
import com.bloomcart.service.CartService;
import com.bloomcart.service.FlowerService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/cart")
public class CartServlet extends HttpServlet {

    private final CartService cartService = new CartService();
    private final FlowerService flowerService = new FlowerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        req.setAttribute("cartItems", cartService.getCart(session));
        req.setAttribute("cartTotal", cartService.getTotal(session));
        req.getRequestDispatcher("/WEB-INF/views/customer/cart.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        try {
            if ("add".equals(action)) {
                int flowerId = Integer.parseInt(req.getParameter("flowerId"));
                int qty = Integer.parseInt(req.getParameter("quantity"));
                Flower flower = flowerService.getFlowerById(flowerId);
                if (flower != null) cartService.addToCart(session, flower, qty);
                res.sendRedirect(req.getContextPath() + "/customer/cart?success=added");

            } else if ("update".equals(action)) {
                int flowerId = Integer.parseInt(req.getParameter("flowerId"));
                int qty = Integer.parseInt(req.getParameter("quantity"));
                cartService.updateQuantity(session, flowerId, qty);
                res.sendRedirect(req.getContextPath() + "/customer/cart");

            } else if ("remove".equals(action)) {
                int flowerId = Integer.parseInt(req.getParameter("flowerId"));
                cartService.removeFromCart(session, flowerId);
                res.sendRedirect(req.getContextPath() + "/customer/cart");

            } else if ("clear".equals(action)) {
                cartService.clearCart(session);
                res.sendRedirect(req.getContextPath() + "/customer/cart");
            }
        } catch (Exception e) {
            res.sendRedirect(req.getContextPath() + "/customer/cart?error=failed");
        }
    }
}
