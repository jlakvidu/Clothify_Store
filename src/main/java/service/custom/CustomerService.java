package service.custom;

import entity.CustomerEntity;
import dto.Customer;
import javafx.collections.ObservableList;
import service.SuperService;

import java.util.List;

public interface CustomerService extends SuperService {
    boolean addCustomer(Customer customer);
    boolean updateCustomer(Customer customer);
    Customer searchCustomer(String  id);
    boolean deleteCustomer(String id);
    ObservableList<Customer> getAll();
    ObservableList<String> getCustomerIds();
}
