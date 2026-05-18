package com.fiore.service;

import com.fiore.dao.UserDAO;
import com.fiore.entity.User;
import com.fiore.util.PasswordUtil;
import com.fiore.util.ValidationUtil;

import java.time.LocalDate;
import java.util.List;

/**
 * UserService - Encapsulates all business logic related to users.
 * Controllers call this; this class calls the DAO.
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    // ── Registration ───────────────────────────────────────────────────────

    /**
     * Validates inputs and registers a new user.
     *
     * @return null on success, or an error message string on failure
     */
    public String registerUser(String fullName, String email, String phone,
                               String password, String confirmPassword,
                               String address, String dob) {

        // --- Validation ---
        if (!ValidationUtil.isValidName(fullName))
            return "Full name must contain only letters and spaces (2–80 characters).";

        if (!ValidationUtil.isValidEmail(email))
            return "Please enter a valid email address.";

        if (!ValidationUtil.isValidPhone(phone))
            return "Please enter a valid phone number.";

        if (!ValidationUtil.isValidPassword(password))
            return "Password must be at least 6 characters long.";

        if (!ValidationUtil.passwordsMatch(password, confirmPassword))
            return "Passwords do not match.";

        if (ValidationUtil.isNullOrEmpty(address))
            return "Address is required.";

        if (ValidationUtil.isNullOrEmpty(dob))
            return "Date of birth is required.";

        // --- Duplicate checks ---
        if (userDAO.emailExists(email))
            return "An account with this email already exists.";

        if (userDAO.phoneExists(phone))
            return "An account with this phone number already exists.";

        // --- Build and persist user ---
        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email.trim().toLowerCase());
        user.setPhone(phone.trim());
        user.setPasswordHash(PasswordUtil.hash(password));
        user.setAddress(address.trim());
        user.setDateOfBirth(LocalDate.parse(dob)); // expects yyyy-MM-dd from input[type=date]
        user.setRole("user");
        user.setStatus("pending"); // admin must approve

        boolean saved = userDAO.insertUser(user);
        return saved ? null : "Registration failed due to a server error. Please try again.";
    }

    // ── Login ──────────────────────────────────────────────────────────────

    /**
     * Authenticates a user.
     *
     * @return the User object on success, or null on failure
     */
    public User login(String email, String password) {
        if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password))
            return null;

        User user = userDAO.findByEmail(email.trim().toLowerCase());
        if (user == null) return null;

        if (!PasswordUtil.verify(password, user.getPasswordHash())) return null;

        if (!user.isApproved() && !user.isAdmin()) return null; // pending/rejected users cannot log in

        return user;
    }

    // ── Profile ────────────────────────────────────────────────────────────

    public String updateProfile(int userId, String fullName, String phone,
                                String address, String dob) {
        if (!ValidationUtil.isValidName(fullName))
            return "Full name must contain only letters and spaces (2–80 characters).";
        if (!ValidationUtil.isValidPhone(phone))
            return "Please enter a valid phone number.";
        if (ValidationUtil.isNullOrEmpty(address))
            return "Address is required.";

        User u = new User();
        u.setId(userId);
        u.setFullName(fullName.trim());
        u.setPhone(phone.trim());
        u.setAddress(address.trim());
        if (!ValidationUtil.isNullOrEmpty(dob))
            u.setDateOfBirth(LocalDate.parse(dob));

        return userDAO.updateProfile(u) ? null : "Failed to update profile.";
    }

    public String changePassword(int userId, String currentPw, String newPw, String confirmPw) {
        User u = userDAO.findById(userId);
        if (u == null) return "User not found.";
        if (!PasswordUtil.verify(currentPw, u.getPasswordHash()))
            return "Current password is incorrect.";
        if (!ValidationUtil.isValidPassword(newPw))
            return "New password must be at least 6 characters.";
        if (!ValidationUtil.passwordsMatch(newPw, confirmPw))
            return "New passwords do not match.";

        return userDAO.updatePassword(userId, PasswordUtil.hash(newPw))
               ? null : "Failed to update password.";
    }

    // ── Admin ──────────────────────────────────────────────────────────────

    public List<User> getAllUsers()    { return userDAO.findAllUsers(); }
    public List<User> getPending()    { return userDAO.findPendingUsers(); }
    public User       getById(int id) { return userDAO.findById(id); }
    public int        countUsers()    { return userDAO.countUsers(); }

    public boolean approveUser(int userId) { return userDAO.updateStatus(userId, "approved"); }
    public boolean rejectUser(int userId)  { return userDAO.updateStatus(userId, "rejected"); }
    public boolean deleteUser(int userId)  { return userDAO.deleteUser(userId); }
}
