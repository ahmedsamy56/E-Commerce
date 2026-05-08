package Application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DashboardDto {
    @JsonProperty("total_products")
    private long totalProducts;
    
    @JsonProperty("total_categories")
    private long totalCategories;
    
    @JsonProperty("total_orders")
    private long totalOrders;

    @JsonProperty("total_reviews")
    private long totalReviews;

    public DashboardDto(long totalProducts, long totalCategories, long totalOrders, long totalReviews) {
        this.totalProducts = totalProducts;
        this.totalCategories = totalCategories;
        this.totalOrders = totalOrders;
        this.totalReviews = totalReviews;
    }

    public long getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(long totalProducts) {
        this.totalProducts = totalProducts;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public long getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(long totalReviews) {
        this.totalReviews = totalReviews;
    }
}
