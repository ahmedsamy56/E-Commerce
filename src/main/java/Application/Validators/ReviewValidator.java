package Application.Validators;

import Core.Entities.Review;
import tr.kontas.fluentvalidation.validation.Validator;

public class ReviewValidator extends Validator<Review> {
    public ReviewValidator() {
        ruleFor(Review::getRating)
                .notNull().withMessage("Rating is required.")
                .must(rating -> rating != null && rating >= 1 && rating <= 5, "Rating must be between 1 and 5.");

        ruleFor(Review::getProductId)
                .notNull().withMessage("Product ID is required.");
        
        ruleFor(Review::getUserId)
                .notNull().withMessage("User ID is required.");
    }
}
