package Application.Services;

import Application.Validators.ProductValidator;
import Core.Entities.Product;
import Core.Interfaces.Repositories.IProductRepository;
import Core.Interfaces.Services.IProductService;
import Core.Interfaces.Services.ICacheService;
import tr.kontas.fluentvalidation.validation.ValidationResult;

import java.util.List;

public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ICacheService cacheService;

    public ProductService(IProductRepository productRepository, ICacheService cacheService) {
        this.productRepository = productRepository;
        this.productValidator = new ProductValidator();
        this.cacheService = cacheService;
    }

    @Override
    public Product getById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAll() {
        String key = "products:all";
        List<Product> products = cacheService.get(key, List.class);
        if (products != null) return products;

        products = productRepository.findAll();
        cacheService.set(key, products, 10);
        return products;
    }

    @Override
    public List<Product> getByCategoryId(int categoryId) {
        String key = "products:category:" + categoryId;
        List<Product> products = cacheService.get(key, List.class);
        if (products != null) return products;

        products = productRepository.findByCategoryId(categoryId);
        cacheService.set(key, products, 10);
        return products;
    }

    @Override
    public void add(Product product) {
        ValidationResult result = productValidator.validate(product);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        productRepository.Add(product);
        clearCache();
    }

    @Override
    public void update(Product product) {
        ValidationResult result = productValidator.validate(product);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        productRepository.update(product);
        clearCache();
    }

    @Override
    public void delete(int id) {
        productRepository.delete(id);
        clearCache();
    }

    private void clearCache() {
        cacheService.delete("products:all");
        cacheService.deleteByPrefix("products:category:");
    }
}
