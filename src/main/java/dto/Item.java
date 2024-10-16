package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Item {
    private String itemId;
    private String name;
    private String supId;
    private Double price;
    private Integer qty;
    private String size;
}
