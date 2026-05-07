package Core.Interfaces.Repositories;

import Core.Entities.OrderItem;
import java.util.List;

public interface IOrderItemRepository extends IGenericRepository<OrderItem> {
    List<OrderItem> findByOrderId(int orderId);
}
