package com.bloomcart.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Helper for reading and writing cookies.
 * Only the user's email is stored in a cookie (never the password).
 */
public class CookieUtil {

    public static final String REMEMBER_EMAIL_COOKIE = "bc_remember_email";
    private static final int MAX_AGE_DAYS = 7;

    private CookieUtil() {}

    public static void setRememberEmailCookie(HttpServletResponse response, String email) {
        Cookie cookie = new Cookie(REMEMBER_EMAIL_COOKIE, email);
        cookie.setMaxAge(MAX_AGE_DAYS * 24 * 3600);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        response.addCookie(cookie);
    }

    public static void clearRememberEmailCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(REMEMBER_EMAIL_COOKIE, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    public static String getRememberEmail(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) return null;
        for (Cookie c : cookies) {
            if (REMEMBER_EMAIL_COOKIE.equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }
}
