package com.bloomcart.model;

import java.math.BigDecimal;

public class DashboardStats {
    private int totalFlowers;
    private int totalUsers;
    private int totalOrders;
    private int pendingOrders;
    private BigDecimal totalRevenue;
    private int totalCategories;
    private int unreadMessages;

    public DashboardStats() {}

    public int getTotalFlowers() { return totalFlowers; }
    public void setTotalFlowers(int totalFlowers) { this.totalFlowers = totalFlowers; }

    public int getTotalUsers() { return totalUsers; }
    public void setTotalUsers(int totalUsers) { this.totalUsers = totalUsers; }

    public int getTotalOrders() { return totalOrders; }
    public void setTotalOrders(int totalOrders) { this.totalOrders = totalOrders; }

    public int getPendingOrders() { return pendingOrders; }
    public void setPendingOrders(int pendingOrders) { this.pendingOrders = pendingOrders; }

    public BigDecimal getTotalRevenue() { return totalRevenue; }
    public void setTotalRevenue(BigDecimal totalRevenue) { this.totalRevenue = totalRevenue; }

    public int getTotalCategories() { return totalCategories; }
    public void setTotalCategories(int totalCategories) { this.totalCategories = totalCategories; }

    public int getUnreadMessages() { return unreadMessages; }
    public void setUnreadMessages(int unreadMessages) { this.unreadMessages = unreadMessages; }
}
