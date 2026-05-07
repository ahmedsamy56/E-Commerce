package org.example.Response;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseHandler {

    private final ObjectMapper objectMapper;

    public ResponseHandler() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
    }

    public void send(HttpServletResponse resp, Response<?> response) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        resp.setStatus(response.getStatusCode());
        resp.getWriter().write(this.objectMapper.writeValueAsString(response));
    }

    public <T> Response<T> deleted() {
        Response<T> response = new Response<>();
        response.setStatusCode(200); // OK
        response.setSucceeded(true);
        response.setMessage("Deleted Successfully");
        return response;
    }

    public <T> Response<T> success(T entity) {
        return success(entity, null);
    }

    public <T> Response<T> success(T entity, Object meta) {
        Response<T> response = new Response<>();
        response.setData(entity);
        response.setStatusCode(200); // OK
        response.setSucceeded(true);
        response.setMessage("Executed Successfully");
        response.setMeta(meta);
        return response;
    }

    public <T> Response<T> unauthorized() {
        return unauthorized(null);
    }

    public <T> Response<T> unauthorized(String message) {
        Response<T> response = new Response<>();
        response.setStatusCode(401); // Unauthorized
        response.setSucceeded(false); 
        
        response.setMessage(message == null ? "UnAuthorized" : message);
        return response;
    }

    public <T> Response<T> badRequest() {
        return badRequest(null);
    }

    public <T> Response<T> badRequest(String message) {
        Response<T> response = new Response<>();
        response.setStatusCode(400); // BadRequest
        response.setSucceeded(false);
        response.setMessage(message == null ? "Bad Request" : message);
        return response;
    }

    public <T> Response<T> notFound() {
        return notFound(null);
    }

    public <T> Response<T> notFound(String message) {
        Response<T> response = new Response<>();
        response.setStatusCode(404); // NotFound
        response.setSucceeded(false);
        response.setMessage(message == null ? "Not Found" : message);
        return response;
    }

    public <T> Response<T> created(T entity) {
        return created(entity, null, null);
    }

    public <T> Response<T> created(T entity, String message) {
        return created(entity, message, null);
    }

    public <T> Response<T> created(T entity, String message, Object meta) {
        Response<T> response = new Response<>();
        response.setData(entity);
        response.setStatusCode(201); // Created
        response.setSucceeded(true);
        response.setMessage(message == null ? "Created Successfully" : message);
        response.setMeta(meta);
        return response;
    }
}
