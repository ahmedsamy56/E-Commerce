package Application.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderSummaryDto {
    private Integer id;
    
    @JsonProperty("user_name")
    private String userName;
    
    private LocalDateTime date;
    
    @JsonProperty("total_price")
    private BigDecimal totalPrice;
    
    private String status;

    public OrderSummaryDto(Integer id, String userName, LocalDateTime date, BigDecimal totalPrice, String status) {
        this.id = id;
        this.userName = userName;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    public Integer getId() { return id; }
    public String getUserName() { return userName; }
    public LocalDateTime getDate() { return date; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
}
