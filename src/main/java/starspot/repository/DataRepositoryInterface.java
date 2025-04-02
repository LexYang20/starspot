package starspot.repository;

public interface DataRepositoryInterface<T> {
    void save(T entity);
}
