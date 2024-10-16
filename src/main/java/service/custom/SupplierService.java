package service.custom;

import dto.Employee;
import dto.Supplier;
import javafx.collections.ObservableList;
import service.SuperService;

public interface SupplierService extends SuperService {
    boolean addSupplier(Supplier supplier);
    boolean updateSupplier(Supplier supplier);
    Supplier searchSupplier(String  id);
    boolean deleteSupplier(String id);
    ObservableList<Supplier> getAll();
}
