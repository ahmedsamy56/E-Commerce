package Infrastructure.Repositories;

import Application.DTOs.OrderSummaryDto;
import Core.Entities.Order;
import Core.Entities.OrderStatus;
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
        order.setStatus(OrderStatus.values()[rs.getInt("status")]);
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
            stmt.setInt(4, order.getStatus().ordinal());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int addAndReturnId(Order order) {
        int generatedId = -1;
        try {
            String sql = "INSERT INTO orders(user_id, date, total_price, status) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, order.getUserId());
            
            if (order.getDate() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(order.getDate()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            stmt.setBigDecimal(3, order.getTotalPrice());
            stmt.setInt(4, order.getStatus().ordinal());
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                generatedId = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
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
            stmt.setInt(4, order.getStatus().ordinal());
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

    @Override
    public List<OrderSummaryDto> findAllWithUser(Integer statusOrdinal) {
        List<OrderSummaryDto> result = new ArrayList<>();
        try {
            String sql;
            PreparedStatement stmt;
            if (statusOrdinal != null) {
                sql = "SELECT o.id, u.name AS user_name, o.date, o.total_price, o.status " +
                      "FROM orders o JOIN users u ON o.user_id = u.id " +
                      "WHERE o.status = ?";
                stmt = connection.prepareStatement(sql);
                stmt.setInt(1, statusOrdinal);
            } else {
                sql = "SELECT o.id, u.name AS user_name, o.date, o.total_price, o.status " +
                      "FROM orders o JOIN users u ON o.user_id = u.id";
                stmt = connection.prepareStatement(sql);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("date");
                java.time.LocalDateTime date = ts != null ? ts.toLocalDateTime() : null;
                OrderStatus status = OrderStatus.values()[rs.getInt("status")];
                result.add(new OrderSummaryDto(
                        rs.getInt("id"),
                        rs.getString("user_name"),
                        date,
                        rs.getBigDecimal("total_price"),
                        status.name()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public List<OrderSummaryDto> findByUserIdWithDetails(int userId) {
        List<OrderSummaryDto> result = new ArrayList<>();
        try {
            String sql = "SELECT o.id, u.name AS user_name, o.date, o.total_price, o.status " +
                         "FROM orders o JOIN users u ON o.user_id = u.id " +
                         "WHERE o.user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("date");
                java.time.LocalDateTime date = ts != null ? ts.toLocalDateTime() : null;
                OrderStatus status = OrderStatus.values()[rs.getInt("status")];
                result.add(new OrderSummaryDto(
                        rs.getInt("id"),
                        rs.getString("user_name"),
                        date,
                        rs.getBigDecimal("total_price"),
                        status.name()
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void updateStatus(int orderId, OrderStatus status) {
        try {
            String sql = "UPDATE orders SET status = ? WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, status.ordinal());
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
