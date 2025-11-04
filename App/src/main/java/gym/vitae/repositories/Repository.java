package gym.vitae.repositories;

import gym.vitae.core.DatabaseConnector;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * Base repository que provee acceso a la conexión JDBC mediante {@link DatabaseConnector}.
 * Las implementaciones concretas deben implementar los métodos CRUD.
 */
public class Repository<T> implements IRepository<T> {

    protected Connection getConnection() throws SQLException {
        return DatabaseConnector.getConnection();
    }

    @Override
    public T save(T entity) throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public boolean update(T entity) throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public boolean delete(int id) throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public Optional<T> findById(int id) throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public List<T> findAll() throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public long count() throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }

    @Override
    public List<T> findAll(int offset, int limit) throws SQLException {
        throw new UnsupportedOperationException("Use specific repository implementation");
    }
}
