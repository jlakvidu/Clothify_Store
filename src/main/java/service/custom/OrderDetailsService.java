package service.custom;

import dto.OrderDetails;
import entity.OrderDetailsEntity;
import javafx.collections.ObservableList;
import service.SuperService;

public interface OrderDetailsService extends SuperService {
    boolean addOrderDetail(OrderDetailsEntity orderDetailsEntity);
    double getTotalSalesPrice();
    Integer getTotalSoldItems();
    ObservableList<OrderDetails> getAll();
}
