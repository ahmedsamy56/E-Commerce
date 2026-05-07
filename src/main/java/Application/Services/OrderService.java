package Application.Services;

import Application.DTOs.CreateOrderDto;
import Application.DTOs.OrderDetailDto;
import Application.DTOs.OrderItemDto;
import Application.DTOs.OrderSummaryDto;
import Application.DTOs.ShippingDto;
import Core.Entities.Order;
import Core.Entities.OrderItem;
import Core.Entities.OrderStatus;
import Core.Entities.Product;
import Core.Entities.Shipment;
import Core.Interfaces.Repositories.IOrderItemRepository;
import Core.Interfaces.Repositories.IOrderRepository;
import Core.Interfaces.Repositories.IProductRepository;
import Core.Interfaces.Repositories.IShipmentRepository;
import Core.Interfaces.Services.IOrderService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderItemRepository orderItemRepository;
    private final IShipmentRepository shipmentRepository;
    private final IProductRepository productRepository;

    public OrderService(IOrderRepository orderRepository, IOrderItemRepository orderItemRepository,
                        IShipmentRepository shipmentRepository, IProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.shipmentRepository = shipmentRepository;
        this.productRepository = productRepository;
    }

    @Override
    public void createOrder(int userId, CreateOrderDto dto) {
        if (dto.getItems() == null || dto.getItems().isEmpty()) {
            throw new IllegalArgumentException("Order must contain at least one item.");
        }
        if (dto.getShipping() == null) {
            throw new IllegalArgumentException("Shipping details are required.");
        }

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (OrderItemDto itemDto : dto.getItems()) {
            if (itemDto.getProductId() == null) {
                throw new IllegalArgumentException("Product ID is required for each item.");
            }
            if (itemDto.getQuantity() == null || itemDto.getQuantity() <= 0) {
                throw new IllegalArgumentException("Invalid quantity for product ID " + itemDto.getProductId());
            }
            Product product = productRepository.findById(itemDto.getProductId());
            if (product == null) {
                throw new IllegalArgumentException("Product with ID " + itemDto.getProductId() + " not found.");
            }
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemDto.getQuantity())));
        }

        // Create Order
        Order order = new Order();
        order.setUserId(userId);
        order.setDate(LocalDateTime.now());
        order.setTotalPrice(totalPrice);
        order.setStatus(OrderStatus.Pending);

        int orderId = orderRepository.addAndReturnId(order);
        if (orderId <= 0) {
            throw new RuntimeException("Failed to create order.");
        }

        // Save Order Items
        for (OrderItemDto itemDto : dto.getItems()) {
            Product product = productRepository.findById(itemDto.getProductId());
            OrderItem item = new OrderItem();
            item.setOrderId(orderId);
            item.setProductId(itemDto.getProductId());
            item.setQuantity(itemDto.getQuantity());
            item.setPrice(product.getPrice());
            orderItemRepository.Add(item);
        }

        // Save Shipment
        ShippingDto shippingDto = dto.getShipping();
        Shipment shipment = new Shipment();
        shipment.setOrderId(orderId);
        shipment.setDate(LocalDateTime.now());
        shipment.setAddress(shippingDto.getAddress());
        shipment.setCity(shippingDto.getCity());
        shipment.setState(shippingDto.getState());
        shipment.setCountry(shippingDto.getCountry());
        shipment.setZipCode(shippingDto.getZipCode());
        shipmentRepository.Add(shipment);
    }

    @Override
    public List<OrderSummaryDto> getAllOrders(Integer statusOrdinal) {
        return orderRepository.findAllWithUser(statusOrdinal);
    }

    @Override
    public List<OrderSummaryDto> getMyOrders(int userId) {
        return orderRepository.findByUserIdWithDetails(userId);
    }

    @Override
    public OrderDetailDto getOrderById(int orderId, int currentUserId, boolean isAdmin) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }
        if (!isAdmin && !order.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("You are not authorized to view this order.");
        }

        List<Core.Entities.OrderItem> items = orderItemRepository.findByOrderId(orderId);
        Core.Entities.Shipment shipment = shipmentRepository.findByOrderId(orderId);

        return new OrderDetailDto(
                order.getId(),
                order.getDate(),
                order.getTotalPrice(),
                order.getStatus().name(),
                items,
                shipment
        );
    }
    @Override
    public void updateOrderStatus(int orderId, int statusOrdinal) {
        Order order = orderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("Order not found.");
        }
        if (statusOrdinal < 0 || statusOrdinal >= OrderStatus.values().length) {
            throw new IllegalArgumentException("Invalid status ordinal.");
        }
        OrderStatus status = OrderStatus.values()[statusOrdinal];
        orderRepository.updateStatus(orderId, status);
    }
}
