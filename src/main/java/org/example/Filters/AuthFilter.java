package org.example.Filters;

import Core.Utils.JwtUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.Response.ResponseHandler;

import java.io.IOException;

@WebFilter("/api/*")
public class AuthFilter implements Filter {

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

        String path = req.getRequestURI();

        // Skip auth endpoints
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

        // Token is valid, set in request attribute for AuthService
        req.setAttribute("token", token);
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
