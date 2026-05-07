package Application.Services;

import Application.Validators.CategoryValidator;
import Core.Entities.Category;
import Core.Interfaces.Repositories.ICategoryRepository;
import Core.Interfaces.Services.ICategoryService;
import tr.kontas.fluentvalidation.validation.ValidationResult;

import java.util.List;

public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;

    public CategoryService(ICategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = new CategoryValidator();
    }

    @Override
    public Category getById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }

    @Override
    public void add(Category category) {
        ValidationResult result = categoryValidator.validate(category);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        categoryRepository.Add(category);
    }

    @Override
    public void update(Category category) {
        ValidationResult result = categoryValidator.validate(category);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        categoryRepository.update(category);
    }

    @Override
    public void delete(int id) {
        categoryRepository.delete(id);
    }
}
