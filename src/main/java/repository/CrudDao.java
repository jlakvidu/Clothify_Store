package repository;

import java.util.List;

public interface CrudDao<T> extends SuperDao {
    boolean save(T t);
    boolean update(T t);
    T search(String  id);
    boolean delete(String id);
    List<T> getAll();
}
