package Application.Services;

import Application.Validators.UserValidator;
import Core.Entities.Role;
import Core.Entities.User;
import Core.Interfaces.Repositories.IUserRepository;
import Core.Interfaces.Services.IUserService;
import tr.kontas.fluentvalidation.validation.ValidationResult;
import org.mindrot.jbcrypt.BCrypt;

import java.time.LocalDateTime;
import java.util.List;

public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final UserValidator userValidator;

    public UserService(IUserRepository userRepository) {
        this.userRepository = userRepository;
        this.userValidator = new UserValidator();
    }

    @Override
    public User getById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public void add(User user) {
        ValidationResult result = userValidator.validate(user);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        userRepository.Add(user);
    }

    @Override
    public void update(User user) {
        ValidationResult result = userValidator.validate(user);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        userRepository.update(user);
    }

    @Override
    public void delete(int id) {
        userRepository.delete(id);
    }

    @Override
    public void createAccount(User user) {
        ValidationResult result = userValidator.validate(user);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new IllegalArgumentException("Email already exists.");
        }

        user.setRole(Role.USER);
        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        user.setCreatedAt(LocalDateTime.now());
        
        userRepository.Add(user);
    }
}
