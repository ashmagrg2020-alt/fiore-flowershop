package com.bloomcart.controller;

import com.bloomcart.model.Flower;
import com.bloomcart.service.FlowerService;
import com.bloomcart.service.WishlistService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/customer/wishlist")
public class WishlistServlet extends HttpServlet {

    private final WishlistService wishlistService = new WishlistService();
    private final FlowerService flowerService = new FlowerService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setAttribute("wishlist", wishlistService.getWishlist(req.getSession()));
        req.getRequestDispatcher("/WEB-INF/views/customer/wishlist.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        HttpSession session = req.getSession();

        try {
            int flowerId = Integer.parseInt(req.getParameter("flowerId"));
            if ("add".equals(action)) {
                Flower flower = flowerService.getFlowerById(flowerId);
                if (flower != null) wishlistService.addToWishlist(session, flower);
            } else if ("remove".equals(action)) {
                wishlistService.removeFromWishlist(session, flowerId);
            }
        } catch (Exception ignored) {}

        String referer = req.getHeader("Referer");
        res.sendRedirect(referer != null ? referer : req.getContextPath() + "/customer/wishlist");
    }
}
