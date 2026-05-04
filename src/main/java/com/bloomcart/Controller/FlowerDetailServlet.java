package com.bloomcart.controller;

import com.bloomcart.model.Flower;
import com.bloomcart.service.FlowerService;
import com.bloomcart.service.WishlistService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/flower-detail")
public class FlowerDetailServlet extends HttpServlet {

    private final FlowerService flowerService = new FlowerService();
    private final WishlistService wishlistService = new WishlistService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String idParam = req.getParameter("id");
        try {
            int id = Integer.parseInt(idParam);
            Flower flower = flowerService.getFlowerById(id);
            if (flower == null) {
                res.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            req.setAttribute("flower", flower);
            req.setAttribute("inWishlist", wishlistService.isInWishlist(req.getSession(true), id));
        } catch (Exception e) {
            req.setAttribute("error", "Could not load flower details.");
        }
        req.getRequestDispatcher("/WEB-INF/views/public/flowerDetail.jsp").forward(req, res);
    }
}
