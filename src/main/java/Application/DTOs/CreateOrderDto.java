package Application.DTOs;

import java.util.List;

public class CreateOrderDto {
    private List<OrderItemDto> items;
    private ShippingDto shipping;

    public List<OrderItemDto> getItems() {
        return items;
    }

    public void setItems(List<OrderItemDto> items) {
        this.items = items;
    }

    public ShippingDto getShipping() {
        return shipping;
    }

    public void setShipping(ShippingDto shipping) {
        this.shipping = shipping;
    }
}
