package entity;

import dto.OrderDetails;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "orders")
public class OrderEntity {
    @Id
    private String orderId;

    private LocalDateTime dateTime;


    private String empId;

    private String cusEmail;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetailsEntity> orderDetails;
}
