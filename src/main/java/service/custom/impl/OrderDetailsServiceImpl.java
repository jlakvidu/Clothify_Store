package service.custom.impl;

import dto.OrderDetails;
import entity.OrderDetailsEntity;
import javafx.scene.control.Alert;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.OrderDetailsDao;
import service.custom.OrderDetailsService;
import util.DaoType;
import util.HibernateUtil;

import java.sql.SQLException;
import java.util.List;

public class OrderDetailsServiceImpl implements OrderDetailsService {

    public boolean addOrderDetail(OrderDetailsEntity orderDetailsEntity) {
        OrderDetailsDao orderDetailsDao = DaoFactory.getInstance().getDaoType(DaoType.ORDERDETAILS);

        // Start transaction for adding order details
        Session session = HibernateUtil.getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            boolean result = orderDetailsDao.save(orderDetailsEntity);
            transaction.commit();
            return result;
        } catch (Exception e) {
            if (transaction != null) transaction.rollback(); // Rollback if exception occurs
            new Alert(Alert.AlertType.ERROR, "Error adding order detail: " + e.getMessage()).show();
        } finally {
            session.close(); // Ensure session is closed
        }
        return false; // Return false if adding fails
    }
}
