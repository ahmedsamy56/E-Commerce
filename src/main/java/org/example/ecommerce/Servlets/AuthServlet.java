package org.example.ecommerce.Servlets;

import Application.DTOs.LoginDto;
import Application.DTOs.AuthResponseDto;
import Application.Services.AuthService;
import Application.Services.UserService;
import Core.Entities.User;
import Infrastructure.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;

@WebServlet("/api/auth/*")
public class AuthServlet extends HttpServlet {

    private UserService userService;
    private AuthService authService;
    private ObjectMapper objectMapper;
    private ResponseHandler responseHandler;

    @Override
    public void init() throws ServletException {
        UserRepository userRepository = new UserRepository();
        userService = new UserService(userRepository);
        authService = new AuthService(userRepository);
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        responseHandler = new ResponseHandler();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if ("/register".equals(pathInfo)) {
            User user = objectMapper.readValue(req.getReader(), User.class);
            userService.createAccount(user);

            user.setPassword(null);
            responseHandler.send(resp, responseHandler.created(user));
        } else if ("/login".equals(pathInfo)) {
            LoginDto loginDto = objectMapper.readValue(req.getReader(), LoginDto.class);
            AuthResponseDto authResponse = authService.login(loginDto.getEmail(), loginDto.getPassword());

            responseHandler.send(resp, responseHandler.success(authResponse));
        } else {
            responseHandler.send(resp, responseHandler.notFound("Endpoint not found"));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if ("/delete-account".equals(pathInfo)) {
            Integer userId = authService.getCurrentUserId(req);
            userService.delete(userId);
            responseHandler.send(resp, responseHandler.deleted());
        } else {
            responseHandler.send(resp, responseHandler.notFound("Endpoint not found"));
        }
    }
}
