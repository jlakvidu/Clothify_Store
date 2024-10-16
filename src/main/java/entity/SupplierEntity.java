package entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class SupplierEntity {
    @Id
    private String supplierId;
    private String supplierTitle;
    private String supplierName;
    private String supplierItem;
    private String supplierCompany;
    private String supplierContactNumber;
    private String supplierEmailAddress;
}
