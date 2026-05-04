package com.bloomcart.util;

/**
 * Application-wide constants.
 */
public final class AppConstants {

    private AppConstants() {}

    // Roles
    public static final String ROLE_ADMIN    = "ADMIN";
    public static final String ROLE_CUSTOMER = "CUSTOMER";

    // Order statuses
    public static final String[] ORDER_STATUSES = {
        "PENDING", "CONFIRMED", "PROCESSING", "SHIPPED", "DELIVERED", "CANCELLED"
    };

    // Default image
    public static final String DEFAULT_FLOWER_IMAGE = "default.jpg";

    // Paths
    public static final String ADMIN_DASHBOARD   = "/admin/dashboard";
    public static final String CUSTOMER_HOME     = "/customer/profile";
    public static final String LOGIN_PAGE        = "/auth/login";
    public static final String REGISTER_PAGE     = "/auth/register";
    public static final String PUBLIC_HOME       = "/home";

    // Session timeout (minutes)
    public static final int SESSION_TIMEOUT_MIN = 30;
}
