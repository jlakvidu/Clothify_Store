package repository.custom.impl;

import entity.OrderDetailsEntity;
import org.hibernate.Session;
import repository.custom.OrderDetailsDao;
import util.HibernateUtil;

import java.util.List;

public class OrderDetailsDaoImpl implements OrderDetailsDao {
    @Override
    public boolean save(OrderDetailsEntity orderDetails) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(orderDetails);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean update(OrderDetailsEntity orderDetailsEntity) {
        return false;
    }

    @Override
    public OrderDetailsEntity search(String id) {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<OrderDetailsEntity> getAll() {
        return List.of();
    }

    public double getTotalSalesPrice() {
        double totalSalesPrice = 0.0;
        Session session = HibernateUtil.getSession();
        try {
            List<OrderDetailsEntity> orderDetails = session.createQuery("FROM OrderDetailsEntity", OrderDetailsEntity.class).getResultList();
            for (OrderDetailsEntity orderDetail : orderDetails) {
                totalSalesPrice += orderDetail.getPrice();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return totalSalesPrice;
    }

    public Integer getTotalSoldItems() {
        Integer totalSoldItems = 0;
        Session session = HibernateUtil.getSession();
        try {
            List<OrderDetailsEntity> orderDetails = session.createQuery("FROM OrderDetailsEntity", OrderDetailsEntity.class).getResultList();

            for (OrderDetailsEntity orderDetail : orderDetails) {
                totalSoldItems += orderDetail.getQty();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return totalSoldItems;
    }
}
