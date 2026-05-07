package Application.Validators;

import Core.Entities.User;
import tr.kontas.fluentvalidation.validation.Validator;

public class UserValidator extends Validator<User> {
    public UserValidator() {
        ruleFor(User::getName)
            .notNull().withMessage("Name is required.")
            .notEmpty().withMessage("Name cannot be empty.");
            
        ruleFor(User::getEmail)
            .notNull().withMessage("Email is required.")
            .notEmpty().withMessage("Email cannot be empty.");
            
        ruleFor(User::getPassword)
            .notNull().withMessage("Password is required.")
            .notEmpty().withMessage("Password cannot be empty.");
    }
}
