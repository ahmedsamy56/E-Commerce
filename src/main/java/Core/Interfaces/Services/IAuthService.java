package Core.Interfaces.Services;

import Application.DTOs.AuthResponseDto;
import jakarta.servlet.http.HttpServletRequest;

public interface IAuthService {
    AuthResponseDto login(String email, String password);
    Integer getCurrentUserId(HttpServletRequest request);
    String getCurrentUserEmail(HttpServletRequest request);
    boolean isAdmin(HttpServletRequest request);
}
