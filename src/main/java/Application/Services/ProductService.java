package Application.Services;

import Application.Validators.ProductValidator;
import Core.Entities.Product;
import Core.Interfaces.Repositories.IProductRepository;
import Core.Interfaces.Services.IProductService;
import tr.kontas.fluentvalidation.validation.ValidationResult;

import java.util.List;

public class ProductService implements IProductService {

    private final IProductRepository productRepository;
    private final ProductValidator productValidator;

    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
        this.productValidator = new ProductValidator();
    }

    @Override
    public Product getById(int id) {
        return productRepository.findById(id);
    }

    @Override
    public List<Product> getAll() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> getByCategoryId(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }

    @Override
    public void add(Product product) {
        ValidationResult result = productValidator.validate(product);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        productRepository.Add(product);
    }

    @Override
    public void update(Product product) {
        ValidationResult result = productValidator.validate(product);
        if (!result.isValid()) {
            throw new IllegalArgumentException(result.getErrors().get(0).message());
        }
        productRepository.update(product);
    }

    @Override
    public void delete(int id) {
        productRepository.delete(id);
    }
}
