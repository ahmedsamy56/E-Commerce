package org.example.ecommerce.Servlets;

import Application.DTOs.DashboardDto;
import Application.Services.AuthService;
import Application.Services.DashboardService;
import Infrastructure.Repositories.CategoryRepository;
import Infrastructure.Repositories.OrderRepository;
import Infrastructure.Repositories.ProductRepository;
import Infrastructure.Repositories.ReviewRepository;
import Infrastructure.Repositories.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;

@WebServlet("/api/admin/dashboard")
public class DashboardServlet extends HttpServlet {

    private DashboardService dashboardService;
    private AuthService authService;
    private ResponseHandler responseHandler;

    @Override
    public void init() throws ServletException {
        dashboardService = new DashboardService(
                new ProductRepository(),
                new CategoryRepository(),
                new OrderRepository(),
                new ReviewRepository()
        );
        authService = new AuthService(new UserRepository());
        responseHandler = new ResponseHandler();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!authService.isAdmin(req)) {
            responseHandler.send(resp, responseHandler.unauthorized("Admin access required"));
            return;
        }

        DashboardDto stats = dashboardService.getDashboardStats();
        responseHandler.send(resp, responseHandler.success(stats));
    }
}
