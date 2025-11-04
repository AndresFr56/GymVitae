package gym.vitae.repositories;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * @param <T> Generico que representa Tipo de entidad que maneja el repositorio
 */
public interface IRepository<T> {
  /**
     * Guarda una nueva entidad en la base de datos.
     *
     * @param entity Entidad a guardar
     * @return Entidad guardada con su ID generado
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    T save(T entity) throws SQLException;

    /**
     * Actualiza una entidad existente en la base de datos.
     *
     * @param entity Entidad con los datos actualizados
     * @return true si la actualización fue exitosa, false en caso contrario
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    boolean update(T entity) throws SQLException;

    /**
     * Elimina una entidad de la base de datos por su ID.
     * Este método realiza una eliminación física.
     *
     * @param id Identificador de la entidad a eliminar
     * @return true si la eliminación fue exitosa, false en caso contrario
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    boolean delete(int id) throws SQLException;

    /**
     * Busca una entidad por su ID.
     *
     * @param id Identificador de la entidad a buscar
     * @return Optional conteniendo la entidad si existe, Optional.empty() si no existe
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    Optional<T> findById(int id) throws SQLException;

    /**
     * Obtiene todas las entidades de la tabla.
     *
     * @return Lista con todas las entidades. Lista vacía si no hay registros
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    List<T> findAll() throws SQLException;

    /**
     * Cuenta el número total de registros en la tabla.
     *
     * @return Número total de registros
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    long count() throws SQLException;

    /**
     * Verifica si existe una entidad con el ID especificado.
     *
     * @param id Identificador a verificar
     * @return true si existe, false en caso contrario
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    boolean existsById(int id) throws SQLException;

    /**
     * Obtiene registros con paginación.
     *
     * @param offset Número de registros a saltar
     * @param limit Número máximo de registros a retornar
     * @return Lista paginada de entidades
     * @throws SQLException Si ocurre un error en la operación de base de datos
     */
    List<T> findAll(int offset, int limit) throws SQLException;
}
