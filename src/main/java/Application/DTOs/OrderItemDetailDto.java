package Application.DTOs;

import Core.Entities.Product;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

public class OrderItemDetailDto {
    private Integer id;
    
    @JsonProperty("product_id")
    private Integer productId;
    
    private Integer quantity;
    private BigDecimal price;
    private Product product;

    public OrderItemDetailDto() {}

    public OrderItemDetailDto(Integer id, Integer productId, Integer quantity, BigDecimal price, Product product) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
        this.product = product;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }
}
