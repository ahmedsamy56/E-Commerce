package Core.Interfaces.Repositories;

import Core.Entities.Order;
import java.util.List;

public interface IOrderRepository extends IGenericRepository<Order> {
    List<Order> findByUserId(int userId);
}
