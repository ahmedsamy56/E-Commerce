package Application.Services;

import Application.DTOs.AuthResponseDto;
import Core.Entities.User;
import Core.Interfaces.Repositories.IUserRepository;
import Core.Interfaces.Services.IAuthService;
import Core.Utils.JwtUtil;
import org.mindrot.jbcrypt.BCrypt;

public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthService(IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.jwtUtil = new JwtUtil();
    }

    @Override
    public AuthResponseDto login(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        if (!BCrypt.checkpw(password, user.getPassword())) {
            throw new IllegalArgumentException("Invalid email or password.");
        }

        String token = jwtUtil.generateToken(user);
        return new AuthResponseDto(token, user.getName(), user.getEmail(), user.getPhone());
    }

    @Override
    public Integer getCurrentUserId(jakarta.servlet.http.HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        if (token != null) {
            return jwtUtil.extractIdFromToken(token);
        }
        return null;
    }

    @Override
    public String getCurrentUserEmail(jakarta.servlet.http.HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        if (token != null) {
            return jwtUtil.extractEmailFromToken(token);
        }
        return null;
    }

    @Override
    public boolean isAdmin(jakarta.servlet.http.HttpServletRequest request) {
        String token = (String) request.getAttribute("token");
        if (token != null) {
            String role = jwtUtil.extractRoleFromToken(token);
            return "ADMIN".equalsIgnoreCase(role);
        }
        return false;
    }
}
