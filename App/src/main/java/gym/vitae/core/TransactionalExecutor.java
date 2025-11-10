package gym.vitae.core;

import java.util.function.Supplier;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

/** */
public class TransactionalExecutor {

    private TransactionalExecutor() {}

    public static <T> T inTransaction(EntityManager em, Supplier<T> work) {
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
            throw ex;
        } finally {
            em.clear();
        }
    }

    public static void inTransaction(EntityManager em, Runnable work) {
        inTransaction(em, () -> {
            work.run();
            return null;
        });
    }

}
