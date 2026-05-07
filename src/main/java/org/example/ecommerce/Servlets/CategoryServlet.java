package org.example.ecommerce.Servlets;

import Application.Services.CategoryService;
import Core.Entities.Category;
import Infrastructure.Repositories.CategoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/categories/*")
public class CategoryServlet extends HttpServlet {

    private CategoryService categoryService;
    private ObjectMapper objectMapper;
    private ResponseHandler responseHandler;

    @Override
    public void init() throws ServletException {
        categoryService = new CategoryService(new CategoryRepository());
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        responseHandler = new ResponseHandler();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        if (pathInfo == null || pathInfo.equals("/")) {
            List<Category> categories = categoryService.getAll();
            responseHandler.send(resp, responseHandler.success(categories));
        } else {
            int id = Integer.parseInt(pathInfo.substring(1));
            Category category = categoryService.getById(id);
            if (category == null) {
                responseHandler.send(resp, responseHandler.notFound("Category not found"));
            } else {
                responseHandler.send(resp, responseHandler.success(category));
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Category category = objectMapper.readValue(req.getReader(), Category.class);
        categoryService.add(category);
        responseHandler.send(resp, responseHandler.created(category));
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            responseHandler.send(resp, responseHandler.badRequest("Category ID is required for update"));
            return;
        }

        int id = Integer.parseInt(pathInfo.substring(1));
        Category category = objectMapper.readValue(req.getReader(), Category.class);
        category.setId(id);
        categoryService.update(category);

        responseHandler.send(resp, responseHandler.success(category));
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            responseHandler.send(resp, responseHandler.badRequest("Category ID is required for deletion"));
            return;
        }

        int id = Integer.parseInt(pathInfo.substring(1));
        categoryService.delete(id);
        responseHandler.send(resp, responseHandler.deleted());
    }

}
