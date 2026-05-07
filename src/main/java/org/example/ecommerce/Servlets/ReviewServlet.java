package org.example.ecommerce.Servlets;

import Application.Services.AuthService;
import Application.Services.ReviewService;
import Core.Entities.Review;
import Infrastructure.Repositories.ReviewRepository;
import Infrastructure.Repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/reviews/*")
public class ReviewServlet extends HttpServlet {

    private ReviewService reviewService;
    private AuthService authService;
    private ObjectMapper objectMapper;
    private ResponseHandler responseHandler;

    @Override
    public void init() throws ServletException {
        reviewService = new ReviewService(new ReviewRepository());
        authService = new AuthService(new UserRepository());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        responseHandler = new ResponseHandler();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo != null) {
            if (pathInfo.startsWith("/product/")) {
                int productId = Integer.parseInt(pathInfo.substring("/product/".length()));
                List<Review> reviews = reviewService.getByProductId(productId);
                responseHandler.send(resp, responseHandler.success(reviews));
            } else if (pathInfo.equals("/user")) {
                Integer userId = authService.getCurrentUserId(req);
                if (userId == null) {
                    responseHandler.send(resp, responseHandler.unauthorized("User not authenticated"));
                    return;
                }
                List<Review> reviews = reviewService.getByUserId(userId);
                responseHandler.send(resp, responseHandler.success(reviews));
            } else {
                int id = Integer.parseInt(pathInfo.substring(1));
                Review review = reviewService.getById(id);
                if (review == null) {
                    responseHandler.send(resp, responseHandler.notFound("Review not found"));
                } else {
                    responseHandler.send(resp, responseHandler.success(review));
                }
            }
        }else{
            responseHandler.send(resp, responseHandler.badRequest("Invalid review endpoint"));

        }
        
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentUserId = authService.getCurrentUserId(req);
        if (currentUserId == null) {
            responseHandler.send(resp, responseHandler.unauthorized("User not authenticated"));
            return;
        }

        Review review = objectMapper.readValue(req.getReader(), Review.class);
        review.setUserId(currentUserId); 
        
        reviewService.add(review);
        responseHandler.send(resp, responseHandler.created(review));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentUserId = authService.getCurrentUserId(req);
        if (currentUserId == null) {
            responseHandler.send(resp, responseHandler.unauthorized("User not authenticated"));
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            responseHandler.send(resp, responseHandler.badRequest("Review ID is required for update"));
            return;
        }

        int id = Integer.parseInt(pathInfo.substring(1));
        Review review = objectMapper.readValue(req.getReader(), Review.class);
        review.setId(id);
        review.setUserId(currentUserId);

        reviewService.update(review, currentUserId);
        responseHandler.send(resp, responseHandler.success(review));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer currentUserId = authService.getCurrentUserId(req);
        if (currentUserId == null) {
            responseHandler.send(resp, responseHandler.unauthorized("User not authenticated"));
            return;
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            responseHandler.send(resp, responseHandler.badRequest("Review ID is required for deletion"));
            return;
        }

        int id = Integer.parseInt(pathInfo.substring(1));
        reviewService.delete(id, currentUserId);
        responseHandler.send(resp, responseHandler.deleted());
    }
}
