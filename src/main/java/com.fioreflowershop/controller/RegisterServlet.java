package com.fioreflowershop.controller;

import com.fioreflowershop.service.UserService;
import com.fioreflowershop.service.UserService.ServiceResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * RegisterServlet - Controller for user registration.
 *
 * GET  /register → Displays registration form.
 * POST /register → Processes registration; redirects to login on success.
 *
 * @author Fiore Flowershop Team
 * @version 1.0
 */
public class RegisterServlet extends HttpServlet {

    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/common/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String fullName   = req.getParameter("fullName");
        String email      = req.getParameter("email");
        String phone      = req.getParameter("phone");
        String password   = req.getParameter("password");
        String confirmPwd = req.getParameter("confirmPassword");
        String dob        = req.getParameter("dateOfBirth");
        String address    = req.getParameter("address");

        ServiceResult result = userService.register(fullName, email, phone,
                password, confirmPwd, dob, address);
        if (result.isSuccess()) {
            req.getSession().setAttribute("successMessage", result.getMessage());
            resp.sendRedirect(req.getContextPath() + "/login");
        } else {
            req.setAttribute("errorMessage", result.getMessage());
            // Preserve filled values for convenience
            req.setAttribute("fullNameValue", fullName);
            req.setAttribute("emailValue", email);
            req.setAttribute("phoneValue", phone);
            req.setAttribute("dobValue", dob);
            req.setAttribute("addressValue", address);
            req.getRequestDispatcher("/WEB-INF/views/common/register.jsp").forward(req, resp);
        }
    }
}