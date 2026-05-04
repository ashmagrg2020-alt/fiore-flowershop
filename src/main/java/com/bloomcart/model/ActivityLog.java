package com.bloomcart.model;

import java.time.LocalDateTime;

public class ActivityLog {
    private int id;
    private Integer userId;
    private String action;
    private String description;
    private String ipAddress;
    private LocalDateTime createdAt;

    public ActivityLog() {}

    public ActivityLog(Integer userId, String action, String description, String ipAddress) {
        this.userId = userId;
        this.action = action;
        this.description = description;
        this.ipAddress = ipAddress;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}
