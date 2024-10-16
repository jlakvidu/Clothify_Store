package repository.custom.impl;

import entity.CustomerEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.CustomerDao;
import util.HibernateUtil;

import java.util.List;

public class CustomerDaoImpl implements CustomerDao {
    @Override
    public boolean save(CustomerEntity customer) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(customer);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean update(CustomerEntity customer) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        CustomerEntity customerToUpdate = session.get(CustomerEntity.class, customer.getCustId());

        if (customerToUpdate != null) {
            customerToUpdate.setCustId(customer.getCustId());
            customerToUpdate.setCustTitle(customer.getCustTitle());
            customerToUpdate.setCustName(customer.getCustName());
            customerToUpdate.setDob(customer.getDob());
            customerToUpdate.setSalary(customer.getSalary());
            customerToUpdate.setCustAddress(customer.getCustAddress());
            customerToUpdate.setCity(customer.getCity());
            customerToUpdate.setProvince(customer.getProvince());
            customerToUpdate.setPostalCode(customer.getPostalCode());
            session.update(customerToUpdate);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Customer not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public CustomerEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        CustomerEntity customer = session.get(CustomerEntity.class, id);

        session.getTransaction().commit();
        session.close();
        if (customer != null) {
            return customer;
        } else {
            System.out.println("Customer not found!");
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        CustomerEntity customerToDelete = session.get(CustomerEntity.class, id);

        if (customerToDelete != null) {
            session.delete(customerToDelete);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Customer not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public List<CustomerEntity> getAll() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<CustomerEntity> customerList = session.createQuery("FROM CustomerEntity", CustomerEntity.class).list();

        session.getTransaction().commit();
        session.close();

        return customerList;
    }

    public ObservableList<String> getCustomerIds() {
        List<CustomerEntity> all = getAll();
        ObservableList<String> customerIds = FXCollections.observableArrayList();
        all.forEach(customer->{
            customerIds.add(customer.getCustId());
        });
        return customerIds;
    }

}
