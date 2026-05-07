package Application.Validators;

import Core.Entities.Category;
import tr.kontas.fluentvalidation.validation.Validator;

public class CategoryValidator extends Validator<Category> {
    public CategoryValidator() {
        ruleFor(Category::getName)
            .notNull().withMessage("Category name is required.")
            .notEmpty().withMessage("Category name cannot be empty.");
    }
}
