package service;

import service.custom.impl.*;
import util.ServiceType;

public class ServiceFactory {
    private static ServiceFactory instance;
    private UserServiceImpl userServiceImpl;

    private ServiceFactory(){}

    public static ServiceFactory getInstance() {
        return instance == null ? instance = new ServiceFactory() : instance;
    }

    public <T extends SuperService> T getServiceType(ServiceType type) {
        switch (type) {
            case CUSTOMER: return (T) new CustomerServiceImpl();
            case EMPLOYEE: return (T) new EmployeeServiceImpl();
            case SUPPLIER: return (T) new SupplierServiceImpl();
            case ITEM: return (T) new ItemServiceImpl();
            case ORDER: return (T) new OrderServiceImpl();
            case ORDERDETAILS: return (T) new OrderDetailsServiceImpl();
            case USER:
                if (userServiceImpl == null) {
                    userServiceImpl = new UserServiceImpl();
                }
                return (T) userServiceImpl;
        }
        return null;
    }
}
