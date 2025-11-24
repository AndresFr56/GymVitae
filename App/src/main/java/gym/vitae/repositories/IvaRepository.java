package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Iva;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record IvaRepository(DBConnectionManager db) implements IRepository<Iva> {

  public IvaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Iva save(Iva entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Iva entity) {
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
    // Soft-delete: mark inactive
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update Iva i set i.activo = false where i.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Iva> findById(int id) {
    return TransactionHandler.inTransaction(db, em -> Optional.ofNullable(em.find(Iva.class, id)));
  }

  @Override
  public List<Iva> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Iva> q = em.createQuery("from Iva i order by i.id", Iva.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Iva> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Iva> q = em.createQuery("from Iva i order by i.id", Iva.class);
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
          TypedQuery<Long> q = em.createQuery("select count(i) from Iva i", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Iva.class, id) != null);
  }
}
