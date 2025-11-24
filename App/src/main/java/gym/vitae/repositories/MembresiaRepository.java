package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Membresia;
import gym.vitae.model.enums.EstadoMembresia;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record MembresiaRepository(DBConnectionManager db) implements IRepository<Membresia> {

  public MembresiaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Membresia save(Membresia entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Membresia entity) {
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
            em.createQuery("update Membresia m set m.estado = :estado where m.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoMembresia.CANCELADA)
                .executeUpdate());
  }

  @Override
  public Optional<Membresia> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Membresia.class, id)));
  }

  @Override
  public List<Membresia> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Membresia> q =
              em.createQuery("from Membresia m order by m.id", Membresia.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Membresia> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Membresia> q =
              em.createQuery("from Membresia m order by m.id", Membresia.class);
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
          TypedQuery<Long> q = em.createQuery("select count(m) from Membresia m", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Membresia.class, id) != null);
  }
}
