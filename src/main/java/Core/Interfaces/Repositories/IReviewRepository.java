package Core.Interfaces.Repositories;

import Core.Entities.Review;
import java.util.List;

public interface IReviewRepository extends IGenericRepository<Review> {
    List<Review> findByProductId(int productId);
    List<Review> findByUserId(int userId);
}
