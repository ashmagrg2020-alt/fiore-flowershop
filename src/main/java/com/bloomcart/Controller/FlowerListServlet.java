package com.bloomcart.controller;

import com.bloomcart.service.CategoryService;
import com.bloomcart.service.FlowerService;
import com.bloomcart.service.OccasionService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet("/flowers")
public class FlowerListServlet extends HttpServlet {

    private final FlowerService flowerService = new FlowerService();
    private final CategoryService categoryService = new CategoryService();
    private final OccasionService occasionService = new OccasionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        String keyword = req.getParameter("keyword");
        String catParam = req.getParameter("categoryId");
        String occParam = req.getParameter("occasionId");
        String minParam = req.getParameter("minPrice");
        String maxParam = req.getParameter("maxPrice");

        Integer categoryId = parseIntOrNull(catParam);
        Integer occasionId = parseIntOrNull(occParam);
        Double minPrice = parseDoubleOrNull(minParam);
        Double maxPrice = parseDoubleOrNull(maxParam);

        try {
            req.setAttribute("flowers", flowerService.searchFlowers(keyword, categoryId, occasionId, minPrice, maxPrice));
            req.setAttribute("categories", categoryService.getAllCategories());
            req.setAttribute("occasions", occasionService.getAllOccasions());
        } catch (Exception e) {
            req.setAttribute("error", "Could not load flowers.");
        }
        req.getRequestDispatcher("/WEB-INF/views/public/flowers.jsp").forward(req, res);
    }

    private Integer parseIntOrNull(String s) {
        try { return (s != null && !s.isBlank()) ? Integer.parseInt(s) : null; }
        catch (NumberFormatException e) { return null; }
    }

    private Double parseDoubleOrNull(String s) {
        try { return (s != null && !s.isBlank()) ? Double.parseDouble(s) : null; }
        catch (NumberFormatException e) { return null; }
    }
}
