package service.custom;

import dto.Order;
import service.SuperService;

public interface OrderService extends SuperService {
    boolean placeOrder(Order order);
}
