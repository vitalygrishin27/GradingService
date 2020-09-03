package app.repositories;

import java.util.List;

public interface CRUDInterface<T> {
    void save(T t);
    T findById(long id);
    List<T> findAll();
    void delete(T t);
    void deleteAll();
}
