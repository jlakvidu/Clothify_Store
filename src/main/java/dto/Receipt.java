package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString

public class Receipt {
    private String orderId;
    private String customerName;
    private String customerAddress;
    private List<CartTM> items;
    private Double discount;
    private double totalPrice;
    private LocalDateTime orderDate;

}
