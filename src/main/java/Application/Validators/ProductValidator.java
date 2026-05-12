package Application.Validators;

import Core.Entities.Product;
import tr.kontas.fluentvalidation.validation.Validator;
import java.math.BigDecimal;

public class ProductValidator extends Validator<Product> {
    public ProductValidator() {

        ruleFor(Product::getName)
                .notNull().withMessage("Product name is required.")
                .notEmpty().withMessage("Product name cannot be empty.")
                .must(name -> !name.trim().isEmpty(),
                        "Product name cannot contain only spaces.");

        ruleFor(Product::getPrice)
                .notNull().withMessage("Price is required.")
                .must(price -> price.compareTo(BigDecimal.ZERO) > 0,
                        "Price must be greater than zero.");

        ruleFor(Product::getStock)
                .notNull().withMessage("Stock is required.")
                .must(stock -> stock >= 0,
                        "Stock cannot be negative.");

        ruleFor(Product::getCategoryId)
                .notNull().withMessage("Category ID is required.");
    }
}
