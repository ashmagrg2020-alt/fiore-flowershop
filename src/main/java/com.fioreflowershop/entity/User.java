package com.fioreflowershop.model;

import java.sql.Date;
import java.sql.Timestamp;

/**
 * User - Model class representing a user/customer in the system.
 * Maps to the 'users' table in the database.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class User {

    private int       userId;
    private String    fullName;
    private String    email;
    private String    phone;
    private String    passwordHash;
    private Date      dateOfBirth;
    private String    address;
    private int       roleId;
    private String    roleName;
    private String    status;     // PENDING, APPROVED, REJECTED
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // ===================== Constructors =====================

    public User() {}

    public User(String fullName, String email, String phone,
                String passwordHash, Date dateOfBirth, String address) {
        this.fullName     = fullName;
        this.email        = email;
        this.phone        = phone;
        this.passwordHash = passwordHash;
        this.dateOfBirth  = dateOfBirth;
        this.address      = address;
        this.roleId       = 2;       // Default: USER
        this.status       = "PENDING";
    }

    // ===================== Getters & Setters =====================

    public int getUserId()                       { return userId; }
    public void setUserId(int userId)            { this.userId = userId; }

    public String getFullName()                  { return fullName; }
    public void setFullName(String fullName)     { this.fullName = fullName; }

    public String getEmail()                     { return email; }
    public void setEmail(String email)           { this.email = email; }

    public String getPhone()                     { return phone; }
    public void setPhone(String phone)           { this.phone = phone; }

    public String getPasswordHash()              { return passwordHash; }
    public void setPasswordHash(String hash)     { this.passwordHash = hash; }

    public Date getDateOfBirth()                 { return dateOfBirth; }
    public void setDateOfBirth(Date dob)         { this.dateOfBirth = dob; }

    public String getAddress()                   { return address; }
    public void setAddress(String address)       { this.address = address; }

    public int getRoleId()                       { return roleId; }
    public void setRoleId(int roleId)            { this.roleId = roleId; }

    public String getRoleName()                  { return roleName; }
    public void setRoleName(String roleName)     { this.roleName = roleName; }

    public String getStatus()                    { return status; }
    public void setStatus(String status)         { this.status = status; }

    public Timestamp getCreatedAt()              { return createdAt; }
    public void setCreatedAt(Timestamp createdAt){ this.createdAt = createdAt; }

    public Timestamp getUpdatedAt()              { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt){ this.updatedAt = updatedAt; }

    /**
     * Checks if this user has admin role.
     * @return true if roleId is 1 (ADMIN)
     */
    public boolean isAdmin() {
        return this.roleId == 1;
    }

    /**
     * Checks if this user account is approved.
     * @return true if status is APPROVED
     */
    public boolean isApproved() {
        return "APPROVED".equals(this.status);
    }

    @Override
    public String toString() {
        return "User{userId=" + userId + ", email='" + email + "', role=" + roleId + ", status='" + status + "'}";
    }
}