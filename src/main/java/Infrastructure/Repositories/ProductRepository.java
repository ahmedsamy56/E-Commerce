package Infrastructure.Repositories;

import Core.Entities.Product;
import Core.Interfaces.Repositories.IProductRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository extends BaseRepository implements IProductRepository {

    private Product mapResultSetToProduct(ResultSet rs) throws SQLException {
        Product product = new Product();
        product.setId(rs.getInt("id"));
        product.setName(rs.getString("name"));
        product.setDescription(rs.getString("description"));
        product.setPrice(rs.getBigDecimal("price"));
        product.setStock(rs.getInt("stock"));
        product.setImageUrl(rs.getString("image_url"));
        product.setIsActive(rs.getBoolean("is_active"));
        
        Timestamp createdAtTimestamp = rs.getTimestamp("created_at");
        if (createdAtTimestamp != null) {
            product.setCreatedAt(createdAtTimestamp.toLocalDateTime());
        }
        product.setCategoryId(rs.getInt("category_id"));
        return product;
    }

    @Override
    public Product findById(int id) {
        try {
            String sql = "SELECT * FROM products WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProduct(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> findAll() {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void Add(Product product) {
        try {
            String sql = "INSERT INTO products(name, description, price, stock, image_url, is_active, created_at, category_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setBoolean(6, product.getIsActive() != null ? product.getIsActive() : true);
            
            if (product.getCreatedAt() != null) {
                stmt.setTimestamp(7, Timestamp.valueOf(product.getCreatedAt()));
            } else {
                stmt.setTimestamp(7, Timestamp.valueOf(java.time.LocalDateTime.now()));
            }
            
            stmt.setObject(8, product.getCategoryId());
            stmt.executeUpdate();

            // استرجاع الـ ID المُولد تلقائياً من قاعدة البيانات
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                product.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Product product) {
        try {
            String sql = "UPDATE products SET name=?, description=?, price=?, stock=?, image_url=?, is_active=?, category_id=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setBigDecimal(3, product.getPrice());
            stmt.setInt(4, product.getStock());
            stmt.setString(5, product.getImageUrl());
            stmt.setBoolean(6, product.getIsActive() != null ? product.getIsActive() : true);
            stmt.setObject(7, product.getCategoryId());
            stmt.setInt(8, product.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM products WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Product> findByCategoryId(int categoryId) {
        List<Product> products = new ArrayList<>();
        try {
            String sql = "SELECT * FROM products WHERE category_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, categoryId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                products.add(mapResultSetToProduct(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
}
