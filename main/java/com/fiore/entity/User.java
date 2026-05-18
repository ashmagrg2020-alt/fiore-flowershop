package com.fiore.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * User - Entity representing a registered user or administrator.
 */
public class User {

    // ── Fields ─────────────────────────────────────────────────────────────
    private int         id;
    private String      fullName;
    private String      email;
    private String      phone;
    private String      passwordHash;
    private String      address;
    private LocalDate   dateOfBirth;
    private String      role;           // "admin" | "user"
    private String      status;         // "pending" | "approved" | "rejected"
    private LocalDateTime createdAt;

    // ── Constructors ───────────────────────────────────────────────────────

    public User() {}

    public User(String fullName, String email, String phone,
                String passwordHash, String address,
                LocalDate dateOfBirth, String role, String status) {
        this.fullName     = fullName;
        this.email        = email;
        this.phone        = phone;
        this.passwordHash = passwordHash;
        this.address      = address;
        this.dateOfBirth  = dateOfBirth;
        this.role         = role;
        this.status       = status;
    }

    // ── Getters & Setters ──────────────────────────────────────────────────

    public int getId()                         { return id; }
    public void setId(int id)                  { this.id = id; }

    public String getFullName()                { return fullName; }
    public void setFullName(String fullName)   { this.fullName = fullName; }

    public String getEmail()                   { return email; }
    public void setEmail(String email)         { this.email = email; }

    public String getPhone()                   { return phone; }
    public void setPhone(String phone)         { this.phone = phone; }

    public String getPasswordHash()            { return passwordHash; }
    public void setPasswordHash(String h)      { this.passwordHash = h; }

    public String getAddress()                 { return address; }
    public void setAddress(String address)     { this.address = address; }

    public LocalDate getDateOfBirth()          { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dob)  { this.dateOfBirth = dob; }

    public String getRole()                    { return role; }
    public void setRole(String role)           { this.role = role; }

    public String getStatus()                  { return status; }
    public void setStatus(String status)       { this.status = status; }

    public LocalDateTime getCreatedAt()        { return createdAt; }
    public void setCreatedAt(LocalDateTime t)  { this.createdAt = t; }

    // ── Helpers ────────────────────────────────────────────────────────────

    public boolean isAdmin()    { return "admin".equalsIgnoreCase(role); }
    public boolean isApproved() { return "approved".equalsIgnoreCase(status); }
    public boolean isPending()  { return "pending".equalsIgnoreCase(status); }

    @Override
    public String toString() {
        return "User{id=" + id + ", email=" + email + ", role=" + role + ", status=" + status + "}";
    }
}
