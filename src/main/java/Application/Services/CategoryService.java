package Application.Services;

import Application.Validators.CategoryValidator;
import Core.Entities.Category;
import Core.Interfaces.Repositories.ICategoryRepository;
import Core.Interfaces.Services.ICategoryService;
import Core.Interfaces.Services.ICacheService;
import tr.kontas.fluentvalidation.validation.ValidationResult;

import java.util.List;

public class CategoryService implements ICategoryService {

    private final ICategoryRepository categoryRepository;
    private final CategoryValidator categoryValidator;
    private final ICacheService cacheService;

    public CategoryService(ICategoryRepository categoryRepository, ICacheService cacheService) {
        this.categoryRepository = categoryRepository;
        this.categoryValidator = new CategoryValidator();
        this.cacheService = cacheService;
    }

    @Override
    public Category getById(int id) {
        return categoryRepository.findById(id);
    }

    @Override
    public List<Category> getAll() {
        String key = "categories:all";
        List<Category> categories = cacheService.get(key, List.class);
        if (categories != null) return categories;

        categories = categoryRepository.findAll();
        cacheService.set(key, categories, 30);
        return categories;
    }

    @Override
    public void add(Category category) {
        ValidationResult result = categoryValidator.validate(category);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        categoryRepository.Add(category);
        cacheService.delete("categories:all");
    }

    @Override
    public void update(Category category) {
        ValidationResult result = categoryValidator.validate(category);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        categoryRepository.update(category);
        cacheService.delete("categories:all");
    }

    @Override
    public void delete(int id) {
        categoryRepository.delete(id);
        cacheService.delete("categories:all");
    }
}
