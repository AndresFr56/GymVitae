package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Pago;
import gym.vitae.model.enums.EstadoNomina;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record PagoRepository(DBConnectionManager db) implements IRepository<Pago> {

  public PagoRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Pago save(Pago entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Pago entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.merge(entity);
          em.flush();
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update Pago p set p.estado = :estado where p.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoNomina.ANULADA)
                .executeUpdate());
  }

  @Override
  public Optional<Pago> findById(int id) {
    return TransactionHandler.inTransaction(db, em -> Optional.ofNullable(em.find(Pago.class, id)));
  }

  @Override
  public List<Pago> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Pago> q = em.createQuery("from Pago p order by p.id", Pago.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Pago> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Pago> q = em.createQuery("from Pago p order by p.id", Pago.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return q.getResultList();
        });
  }

  @Override
  public long count() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Long> q = em.createQuery("select count(p) from Pago p", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Pago.class, id) != null);
  }
}
