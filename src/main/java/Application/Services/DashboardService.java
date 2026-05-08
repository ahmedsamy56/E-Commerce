package Application.Services;

import Application.DTOs.DashboardDto;
import Core.Interfaces.Repositories.ICategoryRepository;
import Core.Interfaces.Repositories.IOrderRepository;
import Core.Interfaces.Repositories.IProductRepository;
import Core.Interfaces.Repositories.IReviewRepository;

public class DashboardService {
    private final IProductRepository productRepository;
    private final ICategoryRepository categoryRepository;
    private final IOrderRepository orderRepository;
    private final IReviewRepository reviewRepository;

    public DashboardService(IProductRepository productRepository, 
                            ICategoryRepository categoryRepository, 
                            IOrderRepository orderRepository,
                            IReviewRepository reviewRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderRepository = orderRepository;
        this.reviewRepository = reviewRepository;
    }

    public DashboardDto getDashboardStats() {
        long productsCount = productRepository.findAll().size();
        long categoriesCount = categoryRepository.findAll().size();
        long ordersCount = orderRepository.findAll().size();
        long reviewsCount = reviewRepository.findAll().size();

        return new DashboardDto(productsCount, categoriesCount, ordersCount, reviewsCount);
    }
}
