package gym.vitae.core;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceException;

/**
 * TransactionHandler es una clase utilitaria que facilita la gestión de transacciones con
 * EntityTransaction en JPA.
 */
public class TransactionHandler {

  private TransactionHandler() {}

  /**
   * @param em EntityManager
   * @param work Supplier<T> que contiene el trabajo a realizar en la transacción
   * @return
   * @param <T> Tipo de retorno de la transacción
   */
  public static <T> T inTransaction(EntityManager em, Supplier<T> work) {

    if (em == null || !em.isOpen()) {
      DatabaseUnavailableException ex =
          new DatabaseUnavailableException(
              "Conexión a base de datos no disponible - No se puede iniciar transacción");
      ErrorHandler.showDatabaseError(ex);
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
        ErrorHandler.showDatabaseError(ex);
      }

      throw ex;
    } finally {
      try {
        em.clear();
      } catch (Exception e) {
        // Ignorar excepciones al limpiar el EntityManager
      }
    }
  }

  public static void inTransaction(EntityManager em, Runnable work) {
    inTransaction(
        em,
        () -> {
          work.run();
          return null;
        });
  }

  /**
   * @param db DBConnectionManager
   * @param work Supplier<T> el contenido de la transacción
   * @return
   * @param <T> Tipo de retorno de la transacción
   */
  public static <T> T inTransaction(DBConnectionManager db, Supplier<T> work) {
    EntityManager em = null;
    try {
      em = db.getEntityManager();
      return inTransaction(em, work);
    } finally {
      if (em != null && em.isOpen()) {
        try {
          em.close();
        } catch (Exception ignore) {
        }
      }
    }
  }

  public static void inTransaction(DBConnectionManager db, Runnable work) {
    inTransaction(
        db,
        () -> {
          work.run();
          return null;
        });
  }
}
