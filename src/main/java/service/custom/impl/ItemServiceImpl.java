package service.custom.impl;

import dto.Employee;
import dto.Item;
import dto.OrderDetails;
import entity.EmployeeEntity;
import entity.ItemEntity;
import entity.SupplierEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.modelmapper.ModelMapper;
import repository.DaoFactory;
import repository.custom.EmployeeDao;
import repository.custom.ItemDao;
import repository.custom.SupplierDao;
import repository.custom.impl.CustomerDaoImpl;
import repository.custom.impl.ItemDaoImpl;
import service.custom.ItemService;
import util.DaoType;

import java.util.List;

public class ItemServiceImpl implements ItemService {
    @Override
    public boolean addItem(Item item) {
        ItemEntity itemEntity = new ModelMapper().map(item, ItemEntity.class);
        ItemDao itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        return itemDao.save(itemEntity);
    }

    @Override
    public boolean updateItem(Item item) {
        ItemEntity itemEntity = new ModelMapper().map(item, ItemEntity.class);
        ItemDao itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        return itemDao.update(itemEntity);
    }

    @Override
    public Item searchItem(String itemCode) {
        ItemDao itemDao =  DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        ItemEntity itemEntity = itemDao.search(itemCode);
        Item items = new ModelMapper().map(itemEntity, Item.class);
        return items;
    }

    @Override
    public boolean deleteItem(String itemCode) {
        ItemDao itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        return itemDao.delete(itemCode);
    }

    @Override
    public ObservableList<Item> getAll() {
        ItemDao itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        List<ItemEntity> all = itemDao.getAll();
        ModelMapper modelMapper = new ModelMapper();
        ObservableList<Item> items = FXCollections.observableArrayList();
        for (ItemEntity entity : all) {
            Item item = modelMapper.map(entity, Item.class);
            items.add(item);
        }
        return items;
    }

    public ObservableList<String> getItemIds() {
        ItemDaoImpl itemDao = DaoFactory.getInstance().getDaoType(DaoType.ITEM);
        return itemDao.getItemIds();
    }

    @Override
    public boolean updateStock(List<OrderDetails> orderDetails) {
        for (OrderDetails orderDetails1 : orderDetails){
            Boolean isUpdateStock = updateStock((List<OrderDetails>) orderDetails1);
            if(!isUpdateStock){
                return false;
            }
        }
        return true;
    }

}
