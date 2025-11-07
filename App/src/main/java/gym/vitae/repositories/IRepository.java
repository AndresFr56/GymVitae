package gym.vitae.repositories;

import java.util.List;
import java.util.Optional;

/**
 * Lightweight repository contract for model-specific repositories.
 *
 * Notes:
 * - Implementations are expected to use JPA/Hibernate (EntityManager) or any other
 *   persistence mechanism. No checked SQL exceptions are declared to keep the API
 *   friendly for JPA usage.
 * - The delete(int id) method is intentionally left to concrete implementations.
 *   Per project convention, physical deletes should be avoided; repositories may
 *   implement soft-delete behavior if required.
 *
 * @param <T> entity type
 */
public interface IRepository<T> {

    T save(T entity);

    boolean update(T entity);

    /**
     * Delete should be implemented by the concrete repository. By default
     * repositories can choose to make this a no-op (physical deletes aren't
     * performed in this project).
     *
     * @param id identifier to delete
     */
    void delete(int id);

    Optional<T> findById(int id);

    List<T> findAll();

    List<T> findAll(int offset, int limit);

    long count();

    boolean existsById(int id);

}
