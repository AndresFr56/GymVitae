package gym.vitae.core;

import com.mysql.cj.log.Log;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;
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
   * @param work Function<EntityManager, T> que recibe el EntityManager y retorna el resultado
   * @return
   * @param <T> Tipo de retorno de la transacción
   */
  public static <T> T inTransaction(DBConnectionManager db, Function<EntityManager, T> work) {
    EntityManager em = null;
    try {
      em = db.getEntityManager();
      EntityManager finalEm = em;
      T result = inTransaction(em, () -> work.apply(finalEm));

      // Inicializar todas las relaciones lazy antes de cerrar el EM
      if (result != null) {
        initializeLazyRelations(result);
      }

      return result;
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

  /** Inicializa recursivamente las relaciones lazy de una entidad o colección. */
  private static void initializeLazyRelations(Object obj) {
    if (obj == null) return;

    try {
      if (obj instanceof Collection<?> collection) {
        org.hibernate.Hibernate.initialize(collection);
        collection.forEach(
            item -> {
              if (item != null) {
                initializeEntityFields(item);
              }
            });
      } else if (obj instanceof java.util.Optional) {
        ((java.util.Optional<?>) obj)
            .ifPresent(
                item -> {
                  org.hibernate.Hibernate.initialize(item);
                  initializeEntityFields(item);
                });
      } else {
        org.hibernate.Hibernate.initialize(obj);
        initializeEntityFields(obj);
      }
    } catch (Exception e) {
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  private static void initializeEntityFields(Object entity) {
    if (entity == null) return;

    try {
      Class<?> clazz = entity.getClass();
      // Evitar clases del sistema
      if (clazz.getName().startsWith("java.") || clazz.getName().startsWith("javax.")) {
        return;
      }

      for (Field field : clazz.getDeclaredFields()) {

        field.setAccessible(true);
        Object value = field.get(entity);

        if (value != null) {
          org.hibernate.Hibernate.initialize(value);
        }
      }
    } catch (Exception e) {
        Logger.getLogger(TransactionHandler.class.getName()).log(Level.SEVERE, null, e);
    }
  }

  public static <T> T inTransaction(DBConnectionManager db, Supplier<T> work) {
    return inTransaction(db, (EntityManager em) -> work.get());
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
