package repository.custom.impl;

import entity.CustomerEntity;
import entity.EmployeeEntity;
import entity.ItemEntity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.hibernate.Session;
import repository.custom.ItemDao;
import util.HibernateUtil;

import java.util.List;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean save(ItemEntity item) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(item);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean update(ItemEntity item) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        ItemEntity itemToUpdate = session.get(ItemEntity.class, item.getItemId());

        if (itemToUpdate != null) {
            itemToUpdate.setItemId(item.getItemId());
            itemToUpdate.setName(item.getName());
            itemToUpdate.setSupId(item.getSupId());
            itemToUpdate.setPrice(item.getPrice());
            itemToUpdate.setSize(item.getSize());
            itemToUpdate.setQty(item.getQty());
            session.update(itemToUpdate);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Item not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public ItemEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        ItemEntity item = session.get(ItemEntity.class, id);

        session.getTransaction().commit();
        session.close();
        if (item != null) {
            return item;
        } else {
            System.out.println("Item not found!");
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        ItemEntity itemToDelete = session.get(ItemEntity.class, id);

        if (itemToDelete != null) {
            session.delete(itemToDelete);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Item not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public List<ItemEntity> getAll() {
        ObservableList<ItemEntity> itemList = FXCollections.observableArrayList();

        try (Session session = HibernateUtil.getSession()) {
            itemList.addAll(session.createQuery("FROM ItemEntity", ItemEntity.class).list());
        }
        return itemList;
    }

    public ObservableList<String> getItemIds(){
        List<ItemEntity> all = getAll();
        ObservableList<String> itemIds = FXCollections.observableArrayList();
        all.forEach(item->{
            itemIds.add(item.getItemId());
        });
        return itemIds;
    }
}
