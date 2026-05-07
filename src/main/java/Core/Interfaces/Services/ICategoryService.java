package Core.Interfaces.Services;

import Core.Entities.Category;
import java.util.List;

public interface ICategoryService {
    Category getById(int id);
    List<Category> getAll();
    void add(Category category);
    void update(Category category);
    void delete(int id);
}
