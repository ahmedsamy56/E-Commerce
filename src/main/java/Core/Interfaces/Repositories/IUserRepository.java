package Core.Interfaces.Repositories;

import Core.Entities.User;

public interface IUserRepository extends IGenericRepository<User> {
    User findByEmail(String email);
}