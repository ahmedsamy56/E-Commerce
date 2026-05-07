package Core.Interfaces.Repositories;

import Application.DTOs.OrderSummaryDto;
import Core.Entities.Order;
import Core.Entities.OrderStatus;
import java.util.List;

public interface IOrderRepository extends IGenericRepository<Order> {
    List<Order> findByUserId(int userId);
    int addAndReturnId(Order order);
    List<OrderSummaryDto> findAllWithUser(Integer statusOrdinal);
    List<OrderSummaryDto> findByUserIdWithDetails(int userId);
    void updateStatus(int orderId, OrderStatus status);
}
