package com.bloomcart.controller;

import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.ContactMessage;
import com.bloomcart.service.ContactService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/contact")
public class ContactServlet extends HttpServlet {

    private final ContactService contactService = new ContactService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/public/contact.jsp").forward(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        ContactMessage msg = new ContactMessage();
        msg.setName(req.getParameter("name"));
        msg.setEmail(req.getParameter("email"));
        msg.setSubject(req.getParameter("subject"));
        msg.setMessage(req.getParameter("message"));

        try {
            contactService.submitMessage(msg);
            req.setAttribute("success", "Your message has been sent successfully!");
        } catch (ValidationException e) {
            req.setAttribute("error", e.getMessage());
            req.setAttribute("msg", msg);
        } catch (Exception e) {
            req.setAttribute("error", "Failed to send message. Please try again.");
        }
        req.getRequestDispatcher("/WEB-INF/views/public/contact.jsp").forward(req, res);
    }
}
