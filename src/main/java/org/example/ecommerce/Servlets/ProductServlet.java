package org.example.ecommerce.Servlets;

import Application.Services.ProductService;
import Core.Entities.Product;
import Infrastructure.Repositories.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/products/*")
public class ProductServlet extends HttpServlet {

    private ProductService productService;
    private ObjectMapper objectMapper;
    private ResponseHandler responseHandler;

    @Override
    public void init() throws ServletException {
        productService = new ProductService(new ProductRepository());
        objectMapper = new ObjectMapper();
        responseHandler = new ResponseHandler();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Check if there is a categoryId query parameter for filtering
            String categoryIdParam = req.getParameter("categoryId");
            if (categoryIdParam != null && !categoryIdParam.isEmpty()) {
                try {
                    int categoryId = Integer.parseInt(categoryIdParam);
                    List<Product> products = productService.getByCategoryId(categoryId);
                    responseHandler.send(resp, responseHandler.success(products));
                } catch (NumberFormatException e) {
                    responseHandler.send(resp, responseHandler.badRequest("Invalid categoryId format"));
                }
            } else {
                List<Product> products = productService.getAll();
                responseHandler.send(resp, responseHandler.success(products));
            }
        } else {
            try {
                int id = Integer.parseInt(pathInfo.substring(1));
                Product product = productService.getById(id);
                if (product == null) {
                    responseHandler.send(resp, responseHandler.notFound("Product not found"));
                } else {
                    responseHandler.send(resp, responseHandler.success(product));
                }
            } catch (NumberFormatException e) {
                responseHandler.send(resp, responseHandler.badRequest("Invalid ID format"));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Product product = objectMapper.readValue(req.getReader(), Product.class);
            productService.add(product);
            responseHandler.send(resp, responseHandler.created(product));
        } catch (IllegalArgumentException e) {
            responseHandler.send(resp, responseHandler.badRequest(e.getMessage()));
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            responseHandler.send(resp, responseHandler.badRequest("Product ID is required for update"));
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            Product product = objectMapper.readValue(req.getReader(), Product.class);
            product.setId(id);
            productService.update(product);
            
            responseHandler.send(resp, responseHandler.success(product));
        } catch (NumberFormatException e) {
            responseHandler.send(resp, responseHandler.badRequest("Invalid ID format"));
        } catch (IllegalArgumentException e) {
            responseHandler.send(resp, responseHandler.badRequest(e.getMessage()));
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            responseHandler.send(resp, responseHandler.badRequest("Product ID is required for deletion"));
            return;
        }

        try {
            int id = Integer.parseInt(pathInfo.substring(1));
            productService.delete(id);
            responseHandler.send(resp, responseHandler.deleted());
        } catch (NumberFormatException e) {
            responseHandler.send(resp, responseHandler.badRequest("Invalid ID format"));
        }
    }
}
