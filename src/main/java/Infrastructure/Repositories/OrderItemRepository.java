package Infrastructure.Repositories;

import Core.Entities.OrderItem;
import Core.Interfaces.Repositories.IOrderItemRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderItemRepository extends BaseRepository implements IOrderItemRepository {

    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem item = new OrderItem();
        item.setId(rs.getInt("id"));
        item.setOrderId(rs.getInt("order_id"));
        item.setProductId(rs.getInt("product_id"));
        item.setQuantity(rs.getInt("quantity"));
        item.setPrice(rs.getBigDecimal("price"));
        return item;
    }

    @Override
    public OrderItem findById(int id) {
        try {
            String sql = "SELECT * FROM orderitems WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToOrderItem(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<OrderItem> findAll() {
        return new ArrayList<>();
    }

    @Override
    public void Add(OrderItem item) {
        try {
            String sql = "INSERT INTO orderitems(order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, item.getOrderId());
            stmt.setInt(2, item.getProductId());
            stmt.setInt(3, item.getQuantity());
            stmt.setBigDecimal(4, item.getPrice());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(OrderItem item) {

    }

    @Override
    public void delete(int id) {

    }

    @Override
    public List<OrderItem> findByOrderId(int orderId) {
        List<OrderItem> items = new ArrayList<>();
        try {
            String sql = "SELECT * FROM orderitems WHERE order_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.add(mapResultSetToOrderItem(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }
}
