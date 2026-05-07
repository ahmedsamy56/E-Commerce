package Core.Interfaces.Services;

import Application.DTOs.CreateOrderDto;
import Application.DTOs.OrderDetailDto;
import Application.DTOs.OrderSummaryDto;
import java.util.List;

public interface IOrderService {
    void createOrder(int userId, CreateOrderDto createOrderDto);
    List<OrderSummaryDto> getAllOrders(Integer statusOrdinal);
    List<OrderSummaryDto> getMyOrders(int userId);
    OrderDetailDto getOrderById(int orderId, int currentUserId, boolean isAdmin);
    void updateOrderStatus(int orderId, int statusOrdinal);
}
