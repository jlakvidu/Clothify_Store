package repository.custom.impl;

import dto.Employee;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.EmployeeDao;
import util.HibernateUtil;

import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    @Override
    public boolean save(EmployeeEntity employee) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(employee);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean update(EmployeeEntity employee) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        EmployeeEntity employeeToUpdate = session.get(EmployeeEntity.class, employee.getEmployeeId());

        if (employeeToUpdate != null) {
            employeeToUpdate.setEmployeeId(employee.getEmployeeId());
            employeeToUpdate.setEmployeeName(employee.getEmployeeName());
            employeeToUpdate.setEmployeeTitle(employee.getEmployeeTitle());
            employeeToUpdate.setEmployeeAddress(employee.getEmployeeAddress());
            employeeToUpdate.setEmployeeEmailAddress(employee.getEmployeeEmailAddress());
            employeeToUpdate.setContactNumber(employee.getContactNumber());
            session.update(employeeToUpdate);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Employee not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public EmployeeEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        EmployeeEntity employee = session.get(EmployeeEntity.class, id);

        session.getTransaction().commit();
        session.close();
        if (employee != null) {
            return employee;
        } else {
            System.out.println("Employee not found!");
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        EmployeeEntity employeeToDelete = session.get(EmployeeEntity.class, id);

        if (employeeToDelete != null) {
            session.delete(employeeToDelete);
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
    public List<EmployeeEntity> getAll() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<EmployeeEntity> employeeList = session.createQuery("FROM EmployeeEntity", EmployeeEntity.class).list();

        session.getTransaction().commit();
        session.close();

        return employeeList;
    }

    public ObservableList<String> getEmployeeIds() {
        List<EmployeeEntity> all = getAll();
        ObservableList<String> employeeIds = FXCollections.observableArrayList();
        all.forEach(employee->{
            employeeIds.add(employee.getEmployeeId());
        });
        return employeeIds;
    }
}
