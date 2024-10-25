package service.custom.impl;

import dto.Employee;
import dto.OrderDetails;
import entity.EmployeeEntity;
import entity.OrderDetailsEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.EmployeeDao;
import repository.custom.OrderDetailsDao;
import repository.custom.impl.OrderDetailsDaoImpl;
import service.custom.OrderDetailsService;
import util.DaoType;
import util.HibernateUtil;

import java.util.List;

public class OrderDetailsServiceImpl implements OrderDetailsService {

    public boolean addOrderDetail(OrderDetailsEntity orderDetailsEntity) {
        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDERDETAILS);
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            boolean result = orderDetailsDao.save(orderDetailsEntity);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            new Alert(Alert.AlertType.ERROR, "Error adding order detail: " + e.getMessage()).show();
        } finally {
            session.close();
        }
        return false;
    }

    @Override
    public double getTotalSalesPrice() {
        OrderDetailsDaoImpl orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDERDETAILS);
        return orderDetailsDao.getTotalSalesPrice();
    }

    @Override
    public Integer getTotalSoldItems() {
        OrderDetailsDaoImpl orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDERDETAILS);
        return orderDetailsDao.getTotalSoldItems();
    }

    @Override
    public ObservableList<OrderDetails> getAll() {
        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDERDETAILS);
        List<OrderDetailsEntity> all = orderDetailsDao.getAll();
        ModelMapper modelMapper = new ModelMapper();

        // Add explicit mappings to avoid conflicts
        modelMapper.typeMap(OrderDetailsEntity.class, OrderDetails.class).addMappings(mapper -> {
            mapper.map(src -> src.getItem().getItemId(), OrderDetails::setItemId);
            mapper.map(src -> src.getOrder().getOrderId(), OrderDetails::setOrderId);
        });

        ObservableList<OrderDetails> orderDetails = FXCollections.observableArrayList();
        for (OrderDetailsEntity entity : all) {
            OrderDetails orderDetails1 = modelMapper.map(entity, OrderDetails.class);
            orderDetails.add(orderDetails1);
        }
        return orderDetails;
    }
}
