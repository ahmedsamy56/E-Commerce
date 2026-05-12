package org.example.Filters;

import Core.Utils.JwtUtil;
import Infrastructure.Caching.RedisService;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

    private static final String ALLOWED_ORIGIN = "http://localhost:4200";
    private JwtUtil jwtUtil;
    private ResponseHandler responseHandler;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        jwtUtil = new JwtUtil();
        responseHandler = new ResponseHandler();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        // 1. CORS Headers
        setCorsHeaders(res);
        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        // 2. Rate Limiting
        String clientIp = req.getRemoteAddr();
        String rateLimitKey = "ratelimit:" + clientIp;
        if (!RedisService.getInstance().isAllowed(rateLimitKey, 60, 60)) {
            System.out.println("[RATE LIMIT] Blocked IP: " + clientIp);
            responseHandler.send(res, responseHandler.badRequest("Too Many Requests. Please try again later."));
            return;
        }

        // 3. Authentication
        String path = req.getRequestURI();

        if (path.contains("/api/auth/login") || path.contains("/api/auth/register")) {
            chain.doFilter(request, response);
            return;
        }

        String authHeader = req.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            responseHandler.send(res, responseHandler.unauthorized("Missing or invalid Authorization header"));
            return;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            responseHandler.send(res, responseHandler.unauthorized("Invalid or expired token"));
            return;
        }

        req.setAttribute("token", token);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        response.setHeader("Vary", "Origin");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, PATCH, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type");
    }
}
