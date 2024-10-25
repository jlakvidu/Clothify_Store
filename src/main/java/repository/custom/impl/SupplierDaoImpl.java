package repository.custom.impl;

import entity.SupplierEntity;
import org.hibernate.Session;
import repository.custom.SupplierDao;
import util.HibernateUtil;

import java.util.List;

public class SupplierDaoImpl implements SupplierDao {
    @Override
    public boolean save(SupplierEntity supplier) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(supplier);
        session.getTransaction().commit();
        session.close();

        return true;
    }

    @Override
    public boolean update(SupplierEntity supplier) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        SupplierEntity supplierToUpdate = session.get(SupplierEntity.class, supplier.getSupplierId());

        if (supplierToUpdate != null) {
            supplierToUpdate.setSupplierId(supplier.getSupplierId());
            supplierToUpdate.setSupplierTitle(supplier.getSupplierTitle());
            supplierToUpdate.setSupplierName(supplier.getSupplierName());
            supplierToUpdate.setSupplierItem(supplier.getSupplierItem());
            supplierToUpdate.setSupplierCompany(supplier.getSupplierCompany());
            supplierToUpdate.setSupplierContactNumber(supplier.getSupplierContactNumber());
            supplierToUpdate.setSupplierEmailAddress(supplier.getSupplierEmailAddress());
            session.update(supplierToUpdate);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Supplier not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public SupplierEntity search(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        SupplierEntity supplier = session.get(SupplierEntity.class, id);

        session.getTransaction().commit();
        session.close();
        if (supplier != null) {
            return supplier;
        } else {
            System.out.println("Supplier not found!");
            return null;
        }
    }

    @Override
    public boolean delete(String id) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        SupplierEntity supplierToDelete = session.get(SupplierEntity.class, id);

        if (supplierToDelete != null) {
            session.delete(supplierToDelete);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("Supplier not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public List<SupplierEntity> getAll() {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();

        List<SupplierEntity> supplierList = session.createQuery("FROM SupplierEntity", SupplierEntity.class).list();

        session.getTransaction().commit();
        session.close();

        return supplierList;
    }
}
