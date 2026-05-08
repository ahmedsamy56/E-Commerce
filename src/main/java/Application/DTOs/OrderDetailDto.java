package Application.DTOs;

import Core.Entities.Shipment;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class OrderDetailDto {
    private Integer id;
    private LocalDateTime date;

    @JsonProperty("total_price")
    private BigDecimal totalPrice;

    private String status;

    private List<OrderItemDetailDto> items;
    private Shipment shipping;

    public OrderDetailDto(Integer id, LocalDateTime date, BigDecimal totalPrice, String status,
                          List<OrderItemDetailDto> items, Shipment shipping) {
        this.id = id;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
        this.items = items;
        this.shipping = shipping;
    }

    public Integer getId() { return id; }
    public LocalDateTime getDate() { return date; }
    public BigDecimal getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
    public List<OrderItemDetailDto> getItems() { return items; }
    public Shipment getShipping() { return shipping; }
}
