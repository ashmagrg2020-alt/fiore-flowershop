package com.fiore.controller;

import com.fiore.entity.Bouquet;
import com.fiore.service.BouquetService;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;

/**
 * WishlistServlet - Manages a session-based wishlist of bouquet IDs.
 * GET  /wishlist          -> show wishlist page
 * POST /wishlist?action=add&id=X    -> add to wishlist
 * POST /wishlist?action=remove&id=X -> remove from wishlist
 */
public class WishlistServlet extends HttpServlet {

    private final BouquetService bouquetService = new BouquetService();

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Set<Integer> wishlistIds = (Set<Integer>) session.getAttribute("wishlist");
        List<Bouquet> wishlistItems = new ArrayList<>();

        if (wishlistIds != null && !wishlistIds.isEmpty()) {
            for (int id : wishlistIds) {
                Bouquet b = bouquetService.getById(id);
                if (b != null) wishlistItems.add(b);
            }
        }
        req.setAttribute("wishlistItems", wishlistItems);
        req.getRequestDispatcher("/WEB-INF/views/user/wishlist.jsp").forward(req, resp);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        Set<Integer> wishlist = (Set<Integer>) session.getAttribute("wishlist");
        if (wishlist == null) {
            wishlist = new LinkedHashSet<>();
            session.setAttribute("wishlist", wishlist);
        }

        try {
            String action = req.getParameter("action");
            int    id     = Integer.parseInt(req.getParameter("id"));

            if ("add".equals(action))    wishlist.add(id);
            if ("remove".equals(action)) wishlist.remove(id);

        } catch (NumberFormatException ignored) {}

        // Respond based on whether this was an AJAX-style or form post
        String ref = req.getHeader("Referer");
        resp.sendRedirect(ref != null ? ref : req.getContextPath() + "/shop");
    }
}
