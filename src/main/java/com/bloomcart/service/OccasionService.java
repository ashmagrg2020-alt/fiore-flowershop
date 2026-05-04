package com.bloomcart.service;

import com.bloomcart.dao.OccasionDAO;
import com.bloomcart.exception.ValidationException;
import com.bloomcart.model.Occasion;

import java.sql.SQLException;
import java.util.List;

public class OccasionService {

    private final OccasionDAO occasionDAO = new OccasionDAO();

    public List<Occasion> getAllOccasions() throws SQLException {
        return occasionDAO.findAll();
    }

    public Occasion getOccasionById(int id) throws SQLException {
        return occasionDAO.findById(id);
    }

    public void addOccasion(Occasion occasion) throws ValidationException, SQLException {
        validate(occasion);
        occasionDAO.insert(occasion);
    }

    public void updateOccasion(Occasion occasion) throws ValidationException, SQLException {
        validate(occasion);
        occasionDAO.update(occasion);
    }

    public void deleteOccasion(int id) throws SQLException {
        occasionDAO.delete(id);
    }

    private void validate(Occasion o) throws ValidationException {
        if (o.getName() == null || o.getName().isBlank())
            throw new ValidationException("Occasion name is required.");
    }
}
