package Core.Entities;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private Integer id;
    private Integer userId;
    private LocalDateTime date;
    private BigDecimal totalPrice;
    private Integer status;

    public Order() {
    }

    public Order(Integer id, Integer userId, LocalDateTime date, BigDecimal totalPrice, Integer status) {
        this.id = id;
        this.userId = userId;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
