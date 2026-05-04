package com.bloomcart.controller;

import com.bloomcart.service.CartService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/checkout")
public class CheckoutServlet extends HttpServlet {

    private final CartService cartService = new CartService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        HttpSession session = req.getSession();
        var cartItems = cartService.getCart(session);
        if (cartItems.isEmpty()) {
            res.sendRedirect(req.getContextPath() + "/customer/cart?error=empty");
            return;
        }
        req.setAttribute("cartItems", cartItems);
        req.setAttribute("cartTotal", cartService.getTotal(session));
        req.getRequestDispatcher("/WEB-INF/views/customer/checkout.jsp").forward(req, res);
    }
}
