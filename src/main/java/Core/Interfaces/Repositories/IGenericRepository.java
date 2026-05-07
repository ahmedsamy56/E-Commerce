package Core.Interfaces.Repositories;

import java.util.List;

public interface IGenericRepository<T> {

    T findById(int id);

    List<T> findAll();

    void Add(T entity);

    void update(T entity);

    void delete(int id);
}