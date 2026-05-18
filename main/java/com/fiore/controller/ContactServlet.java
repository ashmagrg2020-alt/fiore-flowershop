package com.fiore.controller;

import com.fiore.util.ValidationUtil;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.IOException;

/**
 * ContactServlet - Shows the contact page and handles the inquiry form.
 */
public class ContactServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        String name    = req.getParameter("name");
        String email   = req.getParameter("email");
        String subject = req.getParameter("subject");
        String message = req.getParameter("message");

        // --- Validation ---
        if (!ValidationUtil.isValidName(name)) {
            req.setAttribute("error", "Please enter a valid name (letters only).");
        } else if (!ValidationUtil.isValidEmail(email)) {
            req.setAttribute("error", "Please enter a valid email address.");
        } else if (ValidationUtil.isNullOrEmpty(subject)) {
            req.setAttribute("error", "Subject is required.");
        } else if (ValidationUtil.isNullOrEmpty(message)) {
            req.setAttribute("error", "Message is required.");
        } else {
            // In a real system you would email this to hello@fiore.com
            req.setAttribute("success", "Thank you for your message! We will get back to you soon. 🌸");
        }

        // Repopulate form fields for convenience
        req.setAttribute("formName",    name);
        req.setAttribute("formEmail",   email);
        req.setAttribute("formSubject", subject);
        req.setAttribute("formMessage", message);

        req.getRequestDispatcher("/WEB-INF/views/contact.jsp").forward(req, resp);
    }
}
