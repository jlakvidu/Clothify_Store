package repository.custom.impl;

import entity.OrderEntity;
import org.hibernate.Session;
import repository.custom.OrderDao;
import util.HibernateUtil;

import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public boolean save(OrderEntity order) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(order);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean update(OrderEntity orderEntity) {
        return false;
    }

    @Override
    public OrderEntity search(String id) {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<OrderEntity> getAll() {
        return List.of();
    }
}
