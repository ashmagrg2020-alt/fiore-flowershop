package com.bloomcart.service;

import com.bloomcart.model.DashboardStats;

import java.sql.SQLException;

public class DashboardService {

    private final FlowerService flowerService = new FlowerService();
    private final UserService userService = new UserService();
    private final OrderService orderService = new OrderService();

    public DashboardStats getStats() throws SQLException {
        DashboardStats stats = new DashboardStats();
        stats.setTotalFlowers(flowerService.countFlowers());
        stats.setTotalUsers(userService.countUsers());
        stats.setTotalOrders(orderService.countOrders());
        stats.setPendingOrders(orderService.countPendingOrders());
        stats.setTotalRevenue(orderService.getTotalRevenue());
        return stats;
    }
}
