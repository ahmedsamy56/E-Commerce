package Core.Interfaces.Services;

import Core.Entities.Product;
import java.util.List;

public interface IProductService {
    Product getById(int id);
    List<Product> getAll();
    List<Product> getByCategoryId(int categoryId);
    void add(Product product);
    void update(Product product);
    void delete(int id);
}
