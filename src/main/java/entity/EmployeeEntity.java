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
public class EmployeeEntity {
    @Id
    private String employeeId;
    private String employeeName;
    private String employeeTitle;
    private String employeeAddress;
    private String employeeEmailAddress;
    private String contactNumber;
}
