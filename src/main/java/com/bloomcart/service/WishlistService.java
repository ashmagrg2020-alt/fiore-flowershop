package com.bloomcart.service;

import com.bloomcart.model.Flower;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class WishlistService {

    private static final String WISHLIST_KEY = "wishlist";

    @SuppressWarnings("unchecked")
    public List<Flower> getWishlist(HttpSession session) {
        List<Flower> list = (List<Flower>) session.getAttribute(WISHLIST_KEY);
        if (list == null) {
            list = new ArrayList<>();
            session.setAttribute(WISHLIST_KEY, list);
        }
        return list;
    }

    public void addToWishlist(HttpSession session, Flower flower) {
        List<Flower> list = getWishlist(session);
        boolean exists = list.stream().anyMatch(f -> f.getId() == flower.getId());
        if (!exists) list.add(flower);
    }

    public void removeFromWishlist(HttpSession session, int flowerId) {
        getWishlist(session).removeIf(f -> f.getId() == flowerId);
    }

    public boolean isInWishlist(HttpSession session, int flowerId) {
        return getWishlist(session).stream().anyMatch(f -> f.getId() == flowerId);
    }

    public void clearWishlist(HttpSession session) {
        session.removeAttribute(WISHLIST_KEY);
    }
}
