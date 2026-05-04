package com.bloomcart.service;

import com.bloomcart.model.CartItem;
import com.bloomcart.model.Flower;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

public class CartService {

    private static final String CART_KEY = "cart";

    @SuppressWarnings("unchecked")
    public List<CartItem> getCart(HttpSession session) {
        List<CartItem> cart = (List<CartItem>) session.getAttribute(CART_KEY);
        if (cart == null) {
            cart = new ArrayList<>();
            session.setAttribute(CART_KEY, cart);
        }
        return cart;
    }

    public void addToCart(HttpSession session, Flower flower, int quantity) {
        List<CartItem> cart = getCart(session);
        for (CartItem item : cart) {
            if (item.getFlower().getId() == flower.getId()) {
                int newQty = item.getQuantity() + quantity;
                if (newQty > flower.getStockQuantity()) newQty = flower.getStockQuantity();
                item.setQuantity(newQty);
                return;
            }
        }
        int qty = Math.min(quantity, flower.getStockQuantity());
        if (qty > 0) cart.add(new CartItem(flower, qty));
    }

    public void updateQuantity(HttpSession session, int flowerId, int quantity) {
        List<CartItem> cart = getCart(session);
        cart.removeIf(item -> item.getFlower().getId() == flowerId && quantity <= 0);
        for (CartItem item : cart) {
            if (item.getFlower().getId() == flowerId && quantity > 0) {
                item.setQuantity(Math.min(quantity, item.getFlower().getStockQuantity()));
            }
        }
    }

    public void removeFromCart(HttpSession session, int flowerId) {
        getCart(session).removeIf(item -> item.getFlower().getId() == flowerId);
    }

    public void clearCart(HttpSession session) {
        session.removeAttribute(CART_KEY);
    }

    public double getTotal(HttpSession session) {
        return getCart(session).stream()
                .mapToDouble(i -> i.getFlower().getPrice() * i.getQuantity())
                .sum();
    }

    public int getItemCount(HttpSession session) {
        return getCart(session).stream().mapToInt(CartItem::getQuantity).sum();
    }
}
