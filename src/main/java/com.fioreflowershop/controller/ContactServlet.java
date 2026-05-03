package com.fioreflowershop.controller;

import com.fioreflowershop.util.DBConnection;
import com.fioreflowershop.util.ValidationUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * ContactServlet - Handles the public contact/inquiry page.
 *
 * GET  /contact → Displays the contact form.
 * POST /contact → Saves the inquiry to the database.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class ContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/common/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String name    = req.getParameter("name");
        String email   = req.getParameter("email");
        String subject = req.getParameter("subject");
        String message = req.getParameter("message");

        // Basic Validation
        if (ValidationUtil.isNullOrEmpty(name) || !ValidationUtil.isValidName(name)) {
            req.setAttribute("errorMessage", "Please enter a valid full name.");
            req.getRequestDispatcher("/WEB-INF/views/common/contact.jsp").forward(req, resp);
            return;
        }
        if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("errorMessage", "Please enter a valid email address.");
            req.getRequestDispatcher("/WEB-INF/views/common/contact.jsp").forward(req, resp);
            return;
        }
        if (ValidationUtil.isNullOrEmpty(message)) {
            req.setAttribute("errorMessage", "Message cannot be empty.");
            req.getRequestDispatcher("/WEB-INF/views/common/contact.jsp").forward(req, resp);
            return;
        }

        try {
            String sql = "INSERT INTO contact_inquiries (name, email, subject, message) VALUES (?, ?, ?, ?)";
            Connection conn = DBConnection.getConnection();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, ValidationUtil.sanitize(name));
                ps.setString(2, ValidationUtil.sanitize(email));
                ps.setString(3, ValidationUtil.sanitize(subject));
                ps.setString(4, ValidationUtil.sanitize(message));
                ps.executeUpdate();
            }
            req.setAttribute("successMessage", "Thank you! Your message has been sent. We'll get back to you soon.");
        } catch (SQLException e) {
            System.err.println("ContactServlet SQL error: " + e.getMessage());
            req.setAttribute("errorMessage", "Failed to send your message. Please try again.");
        }

        req.getRequestDispatcher("/WEB-INF/views/common/contact.jsp").forward(req, resp);
    }
}