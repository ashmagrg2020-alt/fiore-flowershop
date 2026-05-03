package com.fioreflowershop.service;

import com.fioreflowershop.dao.UserDAO;
import com.fioreflowershop.model.User;
import com.fioreflowershop.util.PasswordUtil;
import com.fioreflowershop.util.ValidationUtil;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

/**
 * UserService - Service class encapsulating business logic for user management.
 * Acts as an intermediary between the Controller and UserDAO layers.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class UserService {

    private final UserDAO userDAO = new UserDAO();

    /**
     * Registers a new user after validating all input fields.
     * Checks for duplicate email and phone before saving.
     *
     * @param fullName    the user's full name
     * @param email       the user's email address
     * @param phone       the user's phone number
     * @param password    the plain-text password
     * @param confirmPwd  the confirmation password
     * @param dobStr      date of birth as string (yyyy-MM-dd)
     * @param address     the user's address
     * @return ServiceResult with success/error message
     */
    public ServiceResult register(String fullName, String email, String phone,
                                  String password, String confirmPwd,
                                  String dobStr, String address) {
        try {
            // ---- Input Validation ----
            if (!ValidationUtil.isValidName(fullName)) {
                return ServiceResult.error(ValidationUtil.getNameErrorMessage());
            }
            if (!ValidationUtil.isValidEmail(email)) {
                return ServiceResult.error("Please enter a valid email address.");
            }
            if (!ValidationUtil.isValidPhone(phone)) {
                return ServiceResult.error("Phone number must be exactly 10 digits.");
            }
            if (!ValidationUtil.isValidPassword(password)) {
                return ServiceResult.error(ValidationUtil.getPasswordErrorMessage());
            }
            if (!ValidationUtil.passwordsMatch(password, confirmPwd)) {
                return ServiceResult.error("Passwords do not match. Please re-enter.");
            }

            // ---- Duplicate Checks ----
            if (userDAO.emailExists(email)) {
                return ServiceResult.error("An account with this email already exists.");
            }
            if (userDAO.phoneExists(phone)) {
                return ServiceResult.error("An account with this phone number already exists.");
            }

            // ---- Build User Object ----
            Date dob = null;
            if (dobStr != null && !dobStr.isEmpty()) {
                dob = Date.valueOf(dobStr);
            }

            String hashedPassword = PasswordUtil.hashPassword(password);
            User user = new User(
                    ValidationUtil.sanitize(fullName),
                    ValidationUtil.sanitize(email).toLowerCase(),
                    ValidationUtil.sanitize(phone),
                    hashedPassword,
                    dob,
                    ValidationUtil.sanitize(address)
            );

            boolean success = userDAO.registerUser(user);
            if (success) {
                return ServiceResult.success("Registration successful! Your account is pending admin approval.");
            } else {
                return ServiceResult.error("Registration failed. Please try again.");
            }

        } catch (IllegalArgumentException e) {
            return ServiceResult.error("Invalid date format. Please use YYYY-MM-DD.");
        } catch (SQLException e) {
            System.err.println("UserService.register() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred. Please try again later.");
        }
    }

    /**
     * Authenticates a user by email and password.
     * Checks account status (must be APPROVED to log in).
     *
     * @param email    the user's email address
     * @param password the plain-text password
     * @return ServiceResult containing the User object on success
     */
    public ServiceResult login(String email, String password) {
        try {
            if (ValidationUtil.isNullOrEmpty(email) || ValidationUtil.isNullOrEmpty(password)) {
                return ServiceResult.error("Email and password are required.");
            }

            User user = userDAO.findByEmail(email.trim().toLowerCase());

            if (user == null) {
                return ServiceResult.error("No account found with this email address.");
            }

            if (!PasswordUtil.verifyPassword(password, user.getPasswordHash())) {
                return ServiceResult.error("Incorrect password. Please try again.");
            }

            if ("PENDING".equals(user.getStatus())) {
                return ServiceResult.error("Your account is awaiting admin approval. Please check back later.");
            }

            if ("REJECTED".equals(user.getStatus())) {
                return ServiceResult.error("Your registration has been rejected. Please contact support.");
            }

            return ServiceResult.success("Login successful.").withData(user);

        } catch (SQLException e) {
            System.err.println("UserService.login() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred. Please try again later.");
        }
    }

    /**
     * Updates user profile information.
     *
     * @param userId   the user ID
     * @param fullName updated full name
     * @param phone    updated phone number
     * @param address  updated address
     * @param dobStr   updated date of birth
     * @return ServiceResult with success/error message
     */
    public ServiceResult updateProfile(int userId, String fullName, String phone,
                                       String address, String dobStr) {
        try {
            if (!ValidationUtil.isValidName(fullName)) {
                return ServiceResult.error(ValidationUtil.getNameErrorMessage());
            }
            if (!ValidationUtil.isValidPhone(phone)) {
                return ServiceResult.error("Phone number must be exactly 10 digits.");
            }

            User existing = userDAO.findById(userId);
            if (existing == null) {
                return ServiceResult.error("User not found.");
            }

            // Check phone uniqueness excluding self
            User phoneOwner = userDAO.findByPhone(phone.trim());
            if (phoneOwner != null && phoneOwner.getUserId() != userId) {
                return ServiceResult.error("This phone number is already in use by another account.");
            }

            Date dob = null;
            if (dobStr != null && !dobStr.isEmpty()) {
                dob = Date.valueOf(dobStr);
            }

            existing.setFullName(ValidationUtil.sanitize(fullName));
            existing.setPhone(ValidationUtil.sanitize(phone));
            existing.setAddress(ValidationUtil.sanitize(address));
            existing.setDateOfBirth(dob);

            boolean success = userDAO.updateProfile(existing);
            if (success) {
                return ServiceResult.success("Profile updated successfully.").withData(existing);
            } else {
                return ServiceResult.error("Update failed. Please try again.");
            }

        } catch (IllegalArgumentException e) {
            return ServiceResult.error("Invalid date format.");
        } catch (SQLException e) {
            System.err.println("UserService.updateProfile() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    /**
     * Changes a user's password after verifying the current one.
     *
     * @param userId      the user ID
     * @param currentPwd  the current plain-text password
     * @param newPwd      the new plain-text password
     * @param confirmPwd  confirmation of new password
     * @return ServiceResult with success/error message
     */
    public ServiceResult changePassword(int userId, String currentPwd, String newPwd, String confirmPwd) {
        try {
            User user = userDAO.findById(userId);
            if (user == null) return ServiceResult.error("User not found.");

            if (!PasswordUtil.verifyPassword(currentPwd, user.getPasswordHash())) {
                return ServiceResult.error("Current password is incorrect.");
            }
            if (!ValidationUtil.isValidPassword(newPwd)) {
                return ServiceResult.error(ValidationUtil.getPasswordErrorMessage());
            }
            if (!ValidationUtil.passwordsMatch(newPwd, confirmPwd)) {
                return ServiceResult.error("New passwords do not match.");
            }

            String newHash = PasswordUtil.hashPassword(newPwd);
            boolean success = userDAO.updatePassword(userId, newHash);
            return success
                    ? ServiceResult.success("Password changed successfully.")
                    : ServiceResult.error("Failed to update password.");

        } catch (SQLException e) {
            System.err.println("UserService.changePassword() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    /**
     * Retrieves all users for admin management.
     *
     * @return list of all users
     */
    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    /**
     * Approves or rejects a user account (admin action).
     *
     * @param userId the user ID
     * @param status the new status (APPROVED or REJECTED)
     * @return ServiceResult
     */
    public ServiceResult updateUserStatus(int userId, String status) {
        try {
            if (!"APPROVED".equals(status) && !"REJECTED".equals(status)) {
                return ServiceResult.error("Invalid status value.");
            }
            boolean success = userDAO.updateStatus(userId, status);
            return success
                    ? ServiceResult.success("User status updated to " + status + ".")
                    : ServiceResult.error("Failed to update user status.");
        } catch (SQLException e) {
            System.err.println("UserService.updateUserStatus() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    /**
     * Deletes a user account (admin action).
     *
     * @param userId the user ID to delete
     * @return ServiceResult
     */
    public ServiceResult deleteUser(int userId) {
        try {
            boolean success = userDAO.deleteUser(userId);
            return success
                    ? ServiceResult.success("User deleted successfully.")
                    : ServiceResult.error("Failed to delete user.");
        } catch (SQLException e) {
            System.err.println("UserService.deleteUser() SQL error: " + e.getMessage());
            return ServiceResult.error("A database error occurred.");
        }
    }

    // ===================== Inner Result Class =====================

    /**
     * ServiceResult - Simple wrapper class to carry success/error data from service methods.
     */
    public static class ServiceResult {
        private final boolean success;
        private final String  message;
        private Object        data;

        private ServiceResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public static ServiceResult success(String message) { return new ServiceResult(true, message); }
        public static ServiceResult error(String message)   { return new ServiceResult(false, message); }

        public ServiceResult withData(Object data) { this.data = data; return this; }

        public boolean isSuccess()  { return success; }
        public String  getMessage() { return message; }
        public Object  getData()    { return data; }
    }
}