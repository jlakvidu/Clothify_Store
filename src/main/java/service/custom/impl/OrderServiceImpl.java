package service.custom.impl;

import dto.Order;
import dto.OrderDetails;
import entity.OrderDetailsEntity;
import entity.OrderEntity;
import entity.ItemEntity;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.ItemDao;
import repository.custom.OrderDao;
import service.ServiceFactory;
import service.custom.ItemService;
import service.custom.OrderService;
import util.DaoType;
import util.HibernateUtil;
import util.ServiceType;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderServiceImpl implements OrderService {

    private final ModelMapper modelMapper = new ModelMapper();

    public boolean placeOrder(Order order) {
        OrderEntity orderEntity = modelMapper.map(order, OrderEntity.class);

        Set<OrderDetailsEntity> orderDetailsEntities = order.getOrderDetailsList().stream()
                .map(detailsDTO -> modelMapper.map(detailsDTO, OrderDetailsEntity.class))
                .collect(Collectors.toSet());

        orderEntity.setOrderDetails(orderDetailsEntities);

        OrderDao orderDao = DaoFactory.getInstance().getDaoType(DaoType.ORDER);
        orderDao.save(orderEntity);

        return true;
    }

    @Override
    public ObservableList<Order> getAll() {
        return null;
    }
}
