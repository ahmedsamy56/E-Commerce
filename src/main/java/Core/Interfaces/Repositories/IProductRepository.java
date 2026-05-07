package Core.Interfaces.Repositories;

import Core.Entities.Product;
import java.util.List;

public interface IProductRepository extends IGenericRepository<Product> {
    List<Product> findByCategoryId(int categoryId);
}
