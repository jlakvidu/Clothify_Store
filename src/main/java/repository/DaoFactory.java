package repository;

import repository.custom.impl.*;
import service.custom.impl.OrderDetailsServiceImpl;
import service.custom.impl.OrderServiceImpl;
import util.DaoType;

public class DaoFactory {
    private static DaoFactory instance;
    private DaoFactory(){}

    public static DaoFactory getInstance(){
        return instance == null ? instance = new DaoFactory() : instance ;
    }

    public <T extends SuperDao>T getDaoType(DaoType type){
        switch (type){
            case CUSTOMER:return (T) new CustomerDaoImpl();
            case EMPLOYEE:return  (T) new EmployeeDaoImpl();
            case SUPPLIER:return (T) new SupplierDaoImpl();
            case ITEM:return (T) new ItemDaoImpl();
            case ORDER:return (T) new OrderDaoImpl();
            case ORDERDETAILS:return (T) new OrderDetailsDaoImpl();
            case USER:return (T) new UserDaoImpl();
        }
        return null;
    }
}
