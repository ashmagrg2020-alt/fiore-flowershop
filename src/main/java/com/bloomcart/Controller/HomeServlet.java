package com.bloomcart.controller;

import com.bloomcart.service.CategoryService;
import com.bloomcart.service.FlowerService;
import com.bloomcart.service.OccasionService;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;

@WebServlet({"/", "/home"})
public class HomeServlet extends HttpServlet {

    private final FlowerService flowerService = new FlowerService();
    private final CategoryService categoryService = new CategoryService();
    private final OccasionService occasionService = new OccasionService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {
        try {
            req.setAttribute("featuredFlowers", flowerService.searchFlowers(null, null, null, null, null));
            req.setAttribute("categories", categoryService.getAllCategories());
            req.setAttribute("occasions", occasionService.getAllOccasions());
        } catch (Exception e) {
            req.setAttribute("error", "Could not load flowers.");
        }
        req.getRequestDispatcher("/WEB-INF/views/public/home.jsp").forward(req, res);
    }
}
