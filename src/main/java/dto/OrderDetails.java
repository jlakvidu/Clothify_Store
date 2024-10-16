package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetails {
    private Long id;
    private String orderId;
    private String itemId;
    private Integer qty;
    private Double price;

    public OrderDetails(String orderId, String itemId, Integer qty, Double price) {
        this.orderId = orderId;
        this.itemId = itemId;
        this.qty = qty;
        this.price = price;
    }
}
