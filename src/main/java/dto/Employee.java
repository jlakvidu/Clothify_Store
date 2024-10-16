package dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Employee {
    private String employeeId;
    private String employeeName;
    private String employeeTitle;
    private String employeeAddress;
    private String employeeEmailAddress;
    private String contactNumber;
}

