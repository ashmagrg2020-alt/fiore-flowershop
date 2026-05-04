package com.bloomcart.service;

import com.bloomcart.dao.CategoryDAO;
import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.Category;

import java.sql.SQLException;
import java.util.List;

public class CategoryService {

    private final CategoryDAO categoryDAO = new CategoryDAO();

    public List<Category> getAllCategories() throws SQLException {
        return categoryDAO.findAll();
    }

    public Category getCategoryById(int id) throws SQLException {
        return categoryDAO.findById(id);
    }

    public void addCategory(Category category) throws ValidationException, SQLException {
        validate(category);
        categoryDAO.insert(category);
    }

    public void updateCategory(Category category) throws ValidationException, SQLException {
        validate(category);
        categoryDAO.update(category);
    }

    public void deleteCategory(int id) throws SQLException {
        categoryDAO.delete(id);
    }

    private void validate(Category c) throws ValidationException {
        if (c.getName() == null || c.getName().isBlank())
            throw new ValidationException("Category name is required.");
    }
}
