package Core.Middleware;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.Response;

import java.io.IOException;

@WebFilter("/*")
public class ErrorHandlerFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            chain.doFilter(request, response);
        } catch (Exception ex) {
            handleException((HttpServletResponse) response, ex);
        }
    }

    private void handleException(HttpServletResponse response, Exception error) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Response<String> responseModel = new Response<>();
        responseModel.setSucceeded(false);
        responseModel.setMessage(error.getMessage());

        int statusCode = 500;

        Throwable cause = error;
        if (error instanceof ServletException && error.getCause() != null) {
            cause = error.getCause();
            responseModel.setMessage(cause.getMessage());
        }

        String exceptionName = cause.getClass().getSimpleName();

        switch (exceptionName) {
            case "UnauthorizedAccessException":
            case "SecurityException":
                statusCode = 401; // Unauthorized
                break;
                
            case "ValidationException":
            case "IllegalArgumentException":
                statusCode = 422; // Unprocessable Entity
                break;
                
            case "KeyNotFoundException":
            case "NoSuchElementException":
                statusCode = 404; // Not Found
                break;
                
            case "DbUpdateException":
            case "SQLException":
                statusCode = 400; // Bad Request
                break;
                
            case "ApiException":
                statusCode = 400; // Bad Request
                if (cause.getCause() != null) {
                    responseModel.setMessage(cause.getMessage() + "\n" + cause.getCause().getMessage());
                }
                break;
                
            default:
                statusCode = 500; 
                if (cause.getCause() != null) {
                    responseModel.setMessage(cause.getMessage() + "\n" + cause.getCause().getMessage());
                }
                break;
        }

        responseModel.setStatusCode(statusCode);
        response.setStatus(statusCode);

        String jsonResponse = objectMapper.writeValueAsString(responseModel);
        response.getWriter().write(jsonResponse);
    }

    @Override
    public void destroy() {
    }
}
