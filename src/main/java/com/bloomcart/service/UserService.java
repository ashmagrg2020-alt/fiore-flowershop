package com.bloomcart.service;

import com.bloomcart.dao.UserDAO;
import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.User;
import com.bloomcart.util.PasswordUtil;
import com.bloomcart.util.ValidationUtil;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public void registerUser(User user) throws ValidationException, SQLException {
        ValidationUtil.validateFullName(user.getFullName());
        ValidationUtil.validateEmail(user.getEmail());
        ValidationUtil.validatePhone(user.getPhone());
        ValidationUtil.validatePassword(user.getPassword());

        if (userDAO.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email already registered.");
        }
        if (userDAO.findByPhone(user.getPhone()) != null) {
            throw new ValidationException("Phone number already registered.");
        }

        user.setPassword(PasswordUtil.hash(user.getPassword()));
        user.setRoleId(2); // CUSTOMER
        user.setStatus("ACTIVE");
        userDAO.insert(user);
    }

    public User login(String email, String password) throws ValidationException, SQLException {
        if (email == null || email.isBlank()) throw new ValidationException("Email is required.");
        if (password == null || password.isBlank()) throw new ValidationException("Password is required.");

        User user = userDAO.findByEmail(email);
        if (user == null || !PasswordUtil.verify(password, user.getPassword())) {
            throw new ValidationException("Invalid email or password.");
        }
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new ValidationException("Your account is inactive. Contact support.");
        }
        return user;
    }

    public User getUserById(int id) throws SQLException {
        return userDAO.findById(id);
    }

    public List<User> getAllUsers() throws SQLException {
        return userDAO.findAll();
    }

    public void updateProfile(User user) throws ValidationException, SQLException {
        ValidationUtil.validateFullName(user.getFullName());
        ValidationUtil.validatePhone(user.getPhone());
        User existing = userDAO.findByPhone(user.getPhone());
        if (existing != null && existing.getId() != user.getId()) {
            throw new ValidationException("Phone number already in use.");
        }
        userDAO.update(user);
    }

    public void changePassword(int userId, String oldPassword, String newPassword)
            throws ValidationException, SQLException {
        User user = userDAO.findById(userId);
        if (user == null) throw new ValidationException("User not found.");
        if (!PasswordUtil.verify(oldPassword, user.getPassword())) {
            throw new ValidationException("Current password is incorrect.");
        }
        ValidationUtil.validatePassword(newPassword);
        userDAO.updatePassword(userId, PasswordUtil.hash(newPassword));
    }

    public void updateUserStatus(int userId, String status) throws SQLException {
        userDAO.updateStatus(userId, status);
    }

    public void deleteUser(int userId) throws SQLException {
        userDAO.delete(userId);
    }

    public long countUsers() throws SQLException {
        return userDAO.count();
    }
}
