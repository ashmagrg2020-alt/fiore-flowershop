package com.bloomcart.service;

import com.bloomcart.dao.FlowerDAO;
import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.Flower;

import java.sql.SQLException;
import java.util.List;

public class FlowerService {

    private final FlowerDAO flowerDAO = new FlowerDAO();

    public List<Flower> getAllFlowers() throws SQLException {
        return flowerDAO.findAll();
    }

    public List<Flower> searchFlowers(String keyword, Integer categoryId, Integer occasionId,
                                      Double minPrice, Double maxPrice) throws SQLException {
        return flowerDAO.search(keyword, categoryId, occasionId, minPrice, maxPrice);
    }

    public Flower getFlowerById(int id) throws SQLException {
        return flowerDAO.findById(id);
    }

    public void addFlower(Flower flower) throws ValidationException, SQLException {
        validate(flower);
        flowerDAO.insert(flower);
    }

    public void updateFlower(Flower flower) throws ValidationException, SQLException {
        validate(flower);
        flowerDAO.update(flower);
    }

    public void deleteFlower(int id) throws SQLException {
        flowerDAO.delete(id);
    }

    public long countFlowers() throws SQLException {
        return flowerDAO.count();
    }

    private void validate(Flower f) throws ValidationException {
        if (f.getName() == null || f.getName().isBlank())
            throw new ValidationException("Flower name is required.");
        if (f.getPrice() <= 0)
            throw new ValidationException("Price must be positive.");
        if (f.getStockQuantity() < 0)
            throw new ValidationException("Stock quantity cannot be negative.");
    }
}
