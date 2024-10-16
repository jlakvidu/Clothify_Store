package service.custom;

import dto.Order;
import dto.OrderDetails;
import entity.OrderDetailsEntity;
import org.modelmapper.internal.bytebuddy.implementation.bind.annotation.Super;
import service.SuperService;

import java.util.List;

public interface OrderDetailsService extends SuperService {
    boolean addOrderDetail(OrderDetailsEntity orderDetailsEntity);
}
