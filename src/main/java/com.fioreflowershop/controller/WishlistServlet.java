package com.fioreflowershop.controller.user;

import com.fioreflowershop.model.Product;
import com.fioreflowershop.service.ProductService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * WishlistServlet - Manages the user's wishlist using both session and database.
 * Session is used to maintain the wishlist state across pages.
 *
 * GET  /user/wishlist              → View wishlist
 * POST /user/wishlist?action=add&id=X    → Add item to wishlist
 * POST /user/wishlist?action=remove&id=X → Remove item from wishlist
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class WishlistServlet extends HttpServlet {

    private final ProductService productService = new ProductService();
    private static final String WISHLIST_KEY = "wishlistIds";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        HttpSession session = req.getSession();
        List<Integer> wishlistIds = getWishlistFromSession(session);

        List<Product> wishlistProducts = new ArrayList<>();
        try {
            for (int id : wishlistIds) {
                Product p = productService.getProductById(id);
                if (p != null) wishlistProducts.add(p);
            }
        } catch (SQLException e) {
            System.err.println("WishlistServlet error: " + e.getMessage());
        }

        req.setAttribute("wishlistProducts", wishlistProducts);
        req.getRequestDispatcher("/WEB-INF/views/user/wishlist.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String action = req.getParameter("action");
        HttpSession session = req.getSession();
        List<Integer> wishlistIds = getWishlistFromSession(session);

        try {
            int productId = Integer.parseInt(req.getParameter("id"));

            if ("add".equals(action)) {
                if (!wishlistIds.contains(productId)) {
                    wishlistIds.add(productId);
                    req.getSession().setAttribute("successMessage", "Added to wishlist!");
                }
            } else if ("remove".equals(action)) {
                wishlistIds.remove(Integer.valueOf(productId));
                req.getSession().setAttribute("successMessage", "Removed from wishlist.");
            }

            session.setAttribute(WISHLIST_KEY, wishlistIds);
        } catch (NumberFormatException e) {
            // Ignore invalid IDs
        }

        // Redirect back to the page they came from, or wishlist
        String referer = req.getHeader("Referer");
        if (referer != null && !referer.isEmpty()) {
            resp.sendRedirect(referer);
        } else {
            resp.sendRedirect(req.getContextPath() + "/user/wishlist");
        }
    }

    /**
     * Retrieves the wishlist list of product IDs from the current session.
     * Creates a new empty list if none exists.
     *
     * @param session the current HTTP session
     * @return List of product IDs in the wishlist
     */
    @SuppressWarnings("unchecked")
    private List<Integer> getWishlistFromSession(HttpSession session) {
        List<Integer> ids = (List<Integer>) session.getAttribute(WISHLIST_KEY);
        if (ids == null) {
            ids = new ArrayList<>();
            session.setAttribute(WISHLIST_KEY, ids);
        }
        return ids;
    }
}