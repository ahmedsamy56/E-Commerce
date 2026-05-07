package Infrastructure.Repositories;

import Core.Entities.Order;
import Core.Interfaces.Repositories.IOrderRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository extends BaseRepository implements IOrderRepository {

    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        
        Timestamp dateTimestamp = rs.getTimestamp("date");
        if (dateTimestamp != null) {
            order.setDate(dateTimestamp.toLocalDateTime());
        }
        order.setTotalPrice(rs.getBigDecimal("total_price"));
        order.setStatus(rs.getInt("status"));
        return order;
    }

    @Override
    public Order findById(int id) {
        try {
            String sql = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Order> findAll() {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM orders";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void Add(Order order) {
        try {
            String sql = "INSERT INTO orders(user_id, date, total_price, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, order.getUserId());
            
            if (order.getDate() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            stmt.setBigDecimal(3, order.getTotalPrice());
            stmt.setInt(4, order.getStatus());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Order order) {
        try {
            String sql = "UPDATE orders SET user_id=?, date=?, total_price=?, status=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, order.getUserId());
            
            if (order.getDate() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            stmt.setBigDecimal(3, order.getTotalPrice());
            stmt.setInt(4, order.getStatus());
            stmt.setInt(5, order.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM orders WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Order> findByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        try {
            String sql = "SELECT * FROM orders WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                orders.add(mapResultSetToOrder(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }
}
