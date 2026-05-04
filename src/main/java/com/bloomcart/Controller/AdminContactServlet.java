package com.bloomcart.controller;

import com.bloomcart.service.ContactService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/admin/contacts")
public class AdminContactServlet extends HttpServlet {

    private final ContactService contactService = new ContactService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String action = req.getParameter("action");
        try {
            if ("read".equals(action)) {
                contactService.markAsRead(Integer.parseInt(req.getParameter("id")));
                res.sendRedirect(req.getContextPath() + "/admin/contacts?success=read");
                return;
            }
            if ("delete".equals(action)) {
                contactService.deleteMessage(Integer.parseInt(req.getParameter("id")));
                res.sendRedirect(req.getContextPath() + "/admin/contacts?success=deleted");
                return;
            }
            if ("view".equals(action)) {
                int id = Integer.parseInt(req.getParameter("id"));
                req.setAttribute("message", contactService.getMessageById(id));
                contactService.markAsRead(id);
                req.getRequestDispatcher("/WEB-INF/views/admin/contactDetail.jsp").forward(req, res);
                return;
            }
            req.setAttribute("messages", contactService.getAllMessages());
        } catch (Exception e) {
            req.setAttribute("error", e.getMessage());
        }
        req.getRequestDispatcher("/WEB-INF/views/admin/contacts.jsp").forward(req, res);
    }
}
