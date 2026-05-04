package com.bloomcart.util;

import com.bloomcart.model.CartItem;
import com.bloomcart.model.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Helpers for reading and writing common session attributes.
 */
public class SessionUtil {

    public static final String SESSION_USER      = "loggedUser";
    public static final String SESSION_CART      = "cart";
    public static final String ATTR_SUCCESS      = "successMsg";
    public static final String ATTR_ERROR        = "errorMsg";

    private SessionUtil() {}

    public static User getLoggedUser(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session == null) return null;
        return (User) session.getAttribute(SESSION_USER);
    }

    public static void setLoggedUser(HttpServletRequest req, User user) {
        req.getSession(true).setAttribute(SESSION_USER, user);
    }

    public static void logout(HttpServletRequest req) {
        HttpSession session = req.getSession(false);
        if (session != null) session.invalidate();
    }

    @SuppressWarnings("unchecked")
    public static List<CartItem> getCart(HttpServletRequest req) {
        HttpSession session = req.getSession(true);
        List<CartItem> cart = (List<CartItem>) session.getAttribute(SESSION_CART);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(SESSION_CART, cart);
        }
        return cart;
    }

    public static void setSuccess(HttpServletRequest req, String msg) {
        req.getSession(true).setAttribute(ATTR_SUCCESS, msg);
    }

    public static void setError(HttpServletRequest req, String msg) {
        req.getSession(true).setAttribute(ATTR_ERROR, msg);
    }

    public static boolean isLoggedIn(HttpServletRequest req) {
        return getLoggedUser(req) != null;
    }

    public static boolean isAdmin(HttpServletRequest req) {
        User u = getLoggedUser(req);
        return u != null && u.isAdmin();
    }
}
