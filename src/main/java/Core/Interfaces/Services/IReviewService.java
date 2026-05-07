package Core.Interfaces.Services;

import Core.Entities.Review;
import java.util.List;

public interface IReviewService {
    Review getById(int id);
    List<Review> getByProductId(int productId);
    List<Review> getByUserId(int userId);
    void add(Review review);
    void update(Review review, int currentUserId);
    void delete(int id, int currentUserId);
}
