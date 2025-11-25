package gym.vitae.core;

import java.util.function.Function;
import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

/**
 * TransactionHandler es una clase utilitaria que facilita la gestión de transacciones con
 * EntityTransaction en JPA.
 *
 * <p>Esta clase gestiona automáticamente el ciclo de vida de transacciones y EntityManagers,
 * incluyendo commit, rollback y cierre de recursos.
 *
 * <p><b>IMPORTANTE:</b> Esta versión NO utiliza reflexión ni inicialización forzada de relaciones
 * lazy. Las queries deben usar JOIN FETCH o DTOs para cargar las relaciones necesarias.
 */
public class TransactionHandler {

  private TransactionHandler() {}

  /**
   * Ejecuta trabajo en una transacción usando un EntityManager existente.
   *
   * @param em EntityManager a usar
   * @param work Supplier con el trabajo a realizar
   * @return Resultado del trabajo
   * @param <T> Tipo de retorno
   */
  public static <T> T inTransaction(EntityManager em, Supplier<T> work) {

    if (em == null || !em.isOpen()) {
      DatabaseUnavailableException ex =
          new DatabaseUnavailableException(
              "Conexión a base de datos no disponible - No se puede iniciar transacción");
      ErrorHandler.handleDatabaseError(ex);
      throw ex;
    }

    EntityTransaction tx = em.getTransaction();

    try {

      if (!tx.isActive()) {
        tx.begin();
      }

      T result = work.get();
      tx.commit();

      return result;
    } catch (RuntimeException ex) {
      if (tx.isActive()) {
        tx.rollback();
      }

      if (ex instanceof PersistenceException
          || ex instanceof DatabaseUnavailableException
          || ex instanceof IllegalStateException) {
        ErrorHandler.handleDatabaseError(ex);
      }

      throw ex;
    }
  }

  /**
   * Ejecuta trabajo en una transacción sin retornar valor.
   *
   * @param em EntityManager a usar
   * @param work Runnable con el trabajo a realizar
   */
  public static void inTransaction(EntityManager em, Runnable work) {
    inTransaction(
        em,
        () -> {
          work.run();
          return null;
        });
  }

  /**
   * Ejecuta trabajo en una transacción creando y cerrando un EntityManager automáticamente.
   *
   * <p><b>NOTA:</b> El EntityManager se cierra automáticamente al finalizar. Si necesitas
   * relaciones lazy, debes cargarlas con JOIN FETCH en la query o usar DTOs.
   *
   * @param db DBConnectionManager
   * @param work Function que recibe el EntityManager y retorna el resultado
   * @return Resultado del trabajo
   * @param <T> Tipo de retorno
   */
  public static <T> T inTransaction(DBConnectionManager db, Function<EntityManager, T> work) {
    EntityManager em = null;
    try {
      em = db.getEntityManager();
      EntityManager finalEm = em;
      return inTransaction(em, () -> work.apply(finalEm));
    } finally {
      if (em != null && em.isOpen()) {
        try {
          em.close();
        } catch (Exception e) {
          // Ignorar excepciones al cerrar el EntityManager
        }
      }
    }
  }

  /**
   * Ejecuta trabajo en una transacción usando Supplier.
   *
   * @param db DBConnectionManager
   * @param work Supplier con el trabajo a realizar
   * @return Resultado del trabajo
   * @param <T> Tipo de retorno
   */
  public static <T> T inTransaction(DBConnectionManager db, Supplier<T> work) {
    return inTransaction(db, (EntityManager em) -> work.get());
  }

  /**
   * Ejecuta trabajo en una transacción sin retornar valor.
   *
   * @param db DBConnectionManager
   * @param work Runnable con el trabajo a realizar
   */
  public static void inTransaction(DBConnectionManager db, Runnable work) {
    inTransaction(
        db,
        () -> {
          work.run();
          return null;
        });
  }
}
