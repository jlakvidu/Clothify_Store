package repository.custom.impl;

import dto.User;
import entity.SupplierEntity;
import entity.UserEntity;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import repository.custom.UserDao;
import util.HibernateUtil;

import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(UserEntity user) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        session.persist(user);
        session.getTransaction().commit();
        session.close();

        return true;
    }


    @Override
    public boolean update(UserEntity user) {
        Session session = HibernateUtil.getSession();
        session.beginTransaction();
        UserEntity userToUpdate = session.get(UserEntity.class, user.getId());

        if (userToUpdate != null) {
            userToUpdate.setFirstName(user.getFirstName());
            userToUpdate.setLastName(user.getLastName());
            userToUpdate.setEmailAddress(user.getEmailAddress());
            userToUpdate.setPassword(user.getPassword());
            session.update(userToUpdate);
            session.getTransaction().commit();
            session.close();
            return true;
        } else {
            System.out.println("User not found!");
            session.getTransaction().rollback();
            session.close();
            return false;
        }
    }

    @Override
    public UserEntity search(String id) {
        return null;
    }

    @Override
    public boolean delete(String id) {
        return false;
    }

    @Override
    public List<UserEntity> getAll() {
        return List.of();
    }

    public User  getUserByEmail(String emailAddress) {
        Transaction transaction = null;
        User user = null;

        try (Session session = HibernateUtil.getSession()) {
            transaction = session.beginTransaction();

            Query query = session.createQuery("FROM UserEntity WHERE emailAddress = :emailAddress", UserEntity.class);
            query.setParameter("emailAddress", emailAddress);

            UserEntity userEntity = (UserEntity) ((org.hibernate.query.Query<?>) query).uniqueResult();

            if (userEntity != null) {
                user = convertToUser(userEntity);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }

        return user;
    }

    private User convertToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setEmailAddress(userEntity.getEmailAddress());
        user.setPassword(userEntity.getPassword());

        return user;
    }

}
