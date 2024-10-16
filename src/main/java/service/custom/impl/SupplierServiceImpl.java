package service.custom.impl;

import dto.Employee;
import dto.Supplier;
import entity.EmployeeEntity;
import entity.SupplierEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.EmployeeDao;
import repository.custom.SupplierDao;
import service.custom.SupplierService;
import util.DaoType;

import java.util.List;

public class SupplierServiceImpl implements SupplierService {
    @Override
    public boolean addSupplier(Supplier supplier) {
        SupplierEntity supplierEntity = new ModelMapper().map(supplier, SupplierEntity.class);
        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
        return supplierDao.save(supplierEntity);
    }

    @Override
    public boolean updateSupplier(Supplier supplier) {
        SupplierEntity supplierEntity = new ModelMapper().map(supplier, SupplierEntity.class);
        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
        return supplierDao.update(supplierEntity);
    }

    @Override
    public Supplier searchSupplier(String id) {
        SupplierDao supplierDao =  DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
        SupplierEntity supplierEntity = supplierDao.search(id);
        Supplier supplier = new ModelMapper().map(supplierEntity, Supplier.class);
        return supplier;
    }

    @Override
    public boolean deleteSupplier(String id) {
        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
        return supplierDao.delete(id);
    }

    @Override
    public ObservableList<Supplier> getAll() {
        SupplierDao supplierDao = DaoFactory.getInstance().getDaoType(DaoType.SUPPLIER);
        List<SupplierEntity> all = supplierDao.getAll();
        ModelMapper modelMapper = new ModelMapper();
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        for (SupplierEntity entity : all) {
            Supplier supplier = modelMapper.map(entity, Supplier.class);
            suppliers.add(supplier);
        }
        return suppliers;
    }
}
