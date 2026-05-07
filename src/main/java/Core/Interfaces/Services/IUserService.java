package Core.Interfaces.Services;

import Core.Entities.User;
import java.util.List;

public interface IUserService {
    User getById(int id);
    List<User> getAll();
    void add(User user);
    void update(User user);
    void delete(int id);
    void createAccount(User user);
}
