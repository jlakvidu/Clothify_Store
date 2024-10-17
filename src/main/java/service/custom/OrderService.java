package service.custom;

import dto.Order;
import dto.Supplier;
import javafx.collections.ObservableList;
import service.SuperService;

public interface OrderService extends SuperService {
    boolean placeOrder(Order order);
    ObservableList<Order> getAll();
}
