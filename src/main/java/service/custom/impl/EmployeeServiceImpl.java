package service.custom.impl;

import dto.Customer;
import dto.Employee;
import entity.CustomerEntity;
import entity.EmployeeEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.CustomerDao;
import repository.custom.EmployeeDao;
import repository.custom.impl.CustomerDaoImpl;
import repository.custom.impl.EmployeeDaoImpl;
import service.custom.EmployeeService;
import util.DaoType;

import java.util.List;

public class EmployeeServiceImpl implements EmployeeService {
    @Override
    public boolean addEmployee(Employee employee) {
        EmployeeEntity employeeEntity = new ModelMapper().map(employee, EmployeeEntity.class);
        EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        return employeeDao.save(employeeEntity);
    }

    @Override
    public boolean updateEmployee(Employee employee) {
        EmployeeEntity employeeEntity = new ModelMapper().map(employee, EmployeeEntity.class);
        EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        return employeeDao.update(employeeEntity);
    }

    @Override
    public Employee searchEmployee(String id) {
        EmployeeDao employeeDao =  DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        EmployeeEntity employeeEntity = employeeDao.search(id);
        Employee employees = new ModelMapper().map(employeeEntity, Employee.class);
        return employees;
    }

    @Override
    public boolean deleteEmployee(String id) {
        EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        return employeeDao.delete(id);
    }

    @Override
    public ObservableList<Employee> getAll() {
        EmployeeDao employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        List<EmployeeEntity> all = employeeDao.getAll();
        ModelMapper modelMapper = new ModelMapper();
        ObservableList<Employee> employees = FXCollections.observableArrayList();
        for (EmployeeEntity entity : all) {
            Employee employee = modelMapper.map(entity, Employee.class);
            employees.add(employee);
        }
        return employees;
    }

    @Override
    public ObservableList<String> getEmployeeIds() {
        EmployeeDaoImpl employeeDao = DaoFactory.getInstance().getDaoType(DaoType.EMPLOYEE);
        return employeeDao.getEmployeeIds();
    }
}
