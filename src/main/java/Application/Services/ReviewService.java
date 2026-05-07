package Application.Services;

import Application.Validators.ReviewValidator;
import Core.Entities.Review;
import Core.Interfaces.Repositories.IReviewRepository;
import Core.Interfaces.Services.IReviewService;
import tr.kontas.fluentvalidation.validation.ValidationResult;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewService implements IReviewService {

    private final IReviewRepository reviewRepository;
    private final ReviewValidator reviewValidator;

    public ReviewService(IReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
        this.reviewValidator = new ReviewValidator();
    }

    @Override
    public Review getById(int id) {
        return reviewRepository.findById(id);
    }

    @Override
    public List<Review> getByProductId(int productId) {
        return reviewRepository.findByProductId(productId);
    }

    @Override
    public List<Review> getByUserId(int userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Override
    public void add(Review review) {
        ValidationResult result = reviewValidator.validate(review);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }

        // Check if user already reviewed this product
        Review existingReview = reviewRepository.findByUserIdAndProductId(review.getUserId(), review.getProductId());
        if (existingReview != null) {
            throw new IllegalArgumentException("User has already reviewed this product.");
        }

        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.Add(review);
    }

    @Override
    public void update(Review review, int currentUserId) {
        ValidationResult result = reviewValidator.validate(review);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }

        Review existingReview = reviewRepository.findById(review.getId());
        if (existingReview == null) {
            throw new IllegalArgumentException("Review not found.");
        }

        if (!existingReview.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("You can only update your own reviews.");
        }

        // We only allow updating rating and comment
        existingReview.setRating(review.getRating());
        existingReview.setComment(review.getComment());
        
        reviewRepository.update(existingReview);
    }

    @Override
    public void delete(int id, int currentUserId) {
        Review existingReview = reviewRepository.findById(id);
        if (existingReview == null) {
            throw new IllegalArgumentException("Review not found.");
        }

        if (!existingReview.getUserId().equals(currentUserId)) {
            throw new IllegalArgumentException("You can only delete your own reviews.");
        }

        reviewRepository.delete(id);
    }
}
