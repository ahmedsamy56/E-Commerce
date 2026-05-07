package org.example.ecommerce.Servlets;

import Application.DTOs.CreateOrderDto;
import Application.DTOs.OrderDetailDto;
import Application.DTOs.OrderSummaryDto;
import Application.Services.AuthService;
import Application.Services.OrderService;
import Infrastructure.Repositories.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {

    private OrderService orderService;
    private AuthService authService;
    private ObjectMapper objectMapper;
    private ResponseHandler responseHandler;

    @Override
    public void init() throws ServletException {
        orderService = new OrderService(
                new OrderRepository(),
                new OrderItemRepository(),
                new ShipmentRepository(),
                new ProductRepository()
        );
        authService = new AuthService(new UserRepository());
        objectMapper = new ObjectMapper();
        responseHandler = new ResponseHandler();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentUserId = authService.getCurrentUserId(req);
        if (currentUserId == null) {
            responseHandler.send(resp, responseHandler.unauthorized("User not authenticated"));
            return;
        }

        CreateOrderDto createOrderDto = objectMapper.readValue(req.getReader(), CreateOrderDto.class);
        orderService.createOrder(currentUserId, createOrderDto);
        responseHandler.send(resp, responseHandler.created("Order placed successfully."));
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentUserId = authService.getCurrentUserId(req);
        if (currentUserId == null) {
            responseHandler.send(resp, responseHandler.unauthorized("User not authenticated"));
            return;
        }

        String pathInfo = req.getPathInfo();
        boolean isAdmin = authService.isAdmin(req);

        if ("/my".equals(pathInfo)) {
            List<OrderSummaryDto> orders = orderService.getMyOrders(currentUserId);
            responseHandler.send(resp, responseHandler.success(orders));

        } else if (pathInfo != null && !pathInfo.equals("/") && !pathInfo.isEmpty()) {
            int orderId = Integer.parseInt(pathInfo.substring(1));
            OrderDetailDto order = orderService.getOrderById(orderId, currentUserId, isAdmin);
            responseHandler.send(resp, responseHandler.success(order));

        } else {
            if (!isAdmin) {
                responseHandler.send(resp, responseHandler.unauthorized("Admin role required"));
                return;
            }
            String statusParam = req.getParameter("status");
            Integer statusOrdinal = null;
            if (statusParam != null && !statusParam.isEmpty()) {
                statusOrdinal = Integer.parseInt(statusParam);
            }
            List<OrderSummaryDto> orders = orderService.getAllOrders(statusOrdinal);
            responseHandler.send(resp, responseHandler.success(orders));
        }
    }
    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (!authService.isAdmin(req)) {
            responseHandler.send(resp, responseHandler.unauthorized("Admin role required"));
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.isEmpty()) {
            responseHandler.send(resp, responseHandler.badRequest("Order ID is required"));
            return;
        }

        int orderId = Integer.parseInt(pathInfo.substring(1));
        
        Map<String, Object> body = objectMapper.readValue(req.getReader(), Map.class);
        if (!body.containsKey("status")) {
            responseHandler.send(resp, responseHandler.badRequest("Status is required"));
            return;
        }

        int statusOrdinal = (int) body.get("status");
        orderService.updateOrderStatus(orderId, statusOrdinal);
        responseHandler.send(resp, responseHandler.success("Order status updated successfully."));
    }
}
