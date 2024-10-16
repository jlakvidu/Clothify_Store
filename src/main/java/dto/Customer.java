package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Customer {
    private String custId;
    private String custTitle;
    private String custName;
    private LocalDate dob;
    private double salary;
    private String custAddress;
    private String city;
    private String province;
    private String postalCode;
}