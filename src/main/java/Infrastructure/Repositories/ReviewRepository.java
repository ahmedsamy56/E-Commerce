package Infrastructure.Repositories;

import Core.Entities.Review;
import Core.Interfaces.Repositories.IReviewRepository;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository extends BaseRepository implements IReviewRepository {

    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setUserId(rs.getInt("user_id"));
        review.setProductId(rs.getInt("product_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            review.setCreatedAt(ts.toLocalDateTime());
        }
        return review;
    }

    @Override
    public Review findById(int id) {
        try {
            String sql = "SELECT * FROM Reviews WHERE id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToReview(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Review> findAll() {
        List<Review> reviews = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Reviews";
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public void Add(Review review) {
        try {
            String sql = "INSERT INTO Reviews(user_id, product_id, rating, comment) VALUES (?, ?, ?, ?)";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, review.getUserId());
            stmt.setInt(2, review.getProductId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(Review review) {
        try {
            String sql = "UPDATE Reviews SET user_id=?, product_id=?, rating=?, comment=? WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, review.getUserId());
            stmt.setInt(2, review.getProductId());
            stmt.setInt(3, review.getRating());
            stmt.setString(4, review.getComment());
            stmt.setInt(5, review.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try {
            String sql = "DELETE FROM Reviews WHERE id=?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Review> findByProductId(int productId) {
        List<Review> reviews = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Reviews WHERE product_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }

    @Override
    public List<Review> findByUserId(int userId) {
        List<Review> reviews = new ArrayList<>();
        try {
            String sql = "SELECT * FROM Reviews WHERE user_id = ?";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                reviews.add(mapResultSetToReview(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reviews;
    }
}
