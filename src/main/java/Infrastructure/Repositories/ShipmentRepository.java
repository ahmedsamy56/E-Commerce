package Infrastructure.Repositories;

import Core.Entities.Shipment;
import Core.Interfaces.Repositories.IShipmentRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ShipmentRepository extends BaseRepository implements IShipmentRepository {

    private Shipment mapResultSetToShipment(ResultSet rs) throws SQLException {
        Shipment shipment = new Shipment();
        shipment.setId(rs.getInt("id"));
        shipment.setOrderId(rs.getInt("order_id"));
        Timestamp ts = rs.getTimestamp("date");
        if (ts != null) shipment.setDate(ts.toLocalDateTime());
        shipment.setAddress(rs.getString("address"));
        shipment.setCity(rs.getString("city"));
        shipment.setState(rs.getString("state"));
        shipment.setCountry(rs.getString("country"));
        shipment.setZipCode(rs.getString("zip_code"));
        return shipment;
    }

    @Override
    public Shipment findById(int id) {
        return null;
    }

    @Override
    public Shipment findByOrderId(int orderId) {
        try {
            String sql = "SELECT * FROM shipments WHERE order_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToShipment(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Shipment> findAll() {
        return new ArrayList<>();
    }

    @Override
    public void Add(Shipment shipment) {
        try {
            String sql = "INSERT INTO shipments(order_id, date, address, city, state, country, zip_code) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, shipment.getOrderId());
            if (shipment.getDate() != null) {
                stmt.setTimestamp(2, Timestamp.valueOf(shipment.getDate()));
            } else {
                stmt.setTimestamp(2, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            stmt.setString(3, shipment.getAddress());
            stmt.setString(4, shipment.getCity());
            stmt.setString(5, shipment.getState());
            stmt.setString(6, shipment.getCountry());
            stmt.setString(7, shipment.getZipCode());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Shipment shipment) {
    }

    @Override
    public void delete(int id) {
    }
}
