package service.custom;

import dto.Item;
import dto.OrderDetails;
import dto.Supplier;
import javafx.collections.ObservableList;
import repository.DaoFactory;
import repository.custom.impl.CustomerDaoImpl;
import service.SuperService;

import java.util.List;

public interface ItemService extends SuperService {
    boolean addItem(Item item);
    boolean updateItem(Item item);
    Item searchItem(String  itemCode);
    boolean deleteItem(String itemCode);
    ObservableList<Item> getAll();
    ObservableList<String> getItemIds();
    boolean updateStock(List<OrderDetails> items);
}
