package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Supplier {
    private String supplierId;
    private String supplierTitle;
    private String supplierName;
    private String supplierItem;
    private String supplierCompany;
    private String supplierContactNumber;
    private String supplierEmailAddress;
}
