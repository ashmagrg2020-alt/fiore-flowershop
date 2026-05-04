package com.bloomcart.service;

import com.bloomcart.dao.ContactDAO;
import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.ContactMessage;

import java.sql.SQLException;
import java.util.List;

public class ContactService {

    private final ContactDAO contactDAO = new ContactDAO();

    public void submitMessage(ContactMessage msg) throws ValidationException, SQLException {
        if (msg.getName() == null || msg.getName().isBlank())
            throw new ValidationException("Name is required.");
        if (msg.getEmail() == null || !msg.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$"))
            throw new ValidationException("Valid email is required.");
        if (msg.getSubject() == null || msg.getSubject().isBlank())
            throw new ValidationException("Subject is required.");
        if (msg.getMessage() == null || msg.getMessage().isBlank())
            throw new ValidationException("Message is required.");

        msg.setStatus("UNREAD");
        contactDAO.insert(msg);
    }

    public List<ContactMessage> getAllMessages() throws SQLException {
        return contactDAO.findAll();
    }

    public ContactMessage getMessageById(int id) throws SQLException {
        return contactDAO.findById(id);
    }

    public void markAsRead(int id) throws SQLException {
        contactDAO.updateStatus(id, "READ");
    }

    public void deleteMessage(int id) throws SQLException {
        contactDAO.delete(id);
    }
}
