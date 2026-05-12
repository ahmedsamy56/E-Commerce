package Application.Validators;

import Core.Entities.User;
import tr.kontas.fluentvalidation.validation.Validator;

public class UserValidator extends Validator<User> {
    public UserValidator() {
        ruleFor(User::getName)
                .notNull().withMessage("Name is required.")
                .notEmpty().withMessage("Name cannot be empty.")
                .must(name -> !name.trim().isEmpty(),
                        "Name cannot contain only spaces.");

        ruleFor(User::getEmail)
                .notNull().withMessage("Email is required.")
                .notEmpty().withMessage("Email cannot be empty.")
                .must(email -> email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"),
                        "Invalid email format.");
        ruleFor(User::getPhone)
                .notNull().withMessage("Phone number is required.")
                .notEmpty().withMessage("Phone number cannot be empty.")
                .must(phone -> !phone.trim().isEmpty(),
                        "Phone number cannot contain only spaces.")
                .must(phone -> phone.matches("^01[0125][0-9]{8}$"),
                        "Invalid Egyptian phone number.");
        ruleFor(User::getPassword)
                .notNull().withMessage("Password is required.")
                .notEmpty().withMessage("Password cannot be empty.")
                .must(password -> !password.trim().isEmpty(),
                        "Password cannot contain only spaces.")
                .must(password -> password.length() >= 8,
                        "Password must be at least 8 characters long.");

    }
}
