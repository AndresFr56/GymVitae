package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Proveedore;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record ProveedoreRepository(DBConnectionManager db) implements IRepository<Proveedore> {

  public ProveedoreRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Proveedore save(Proveedore entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Proveedore entity) {
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
            em.createQuery("update Proveedore p set p.activo = false where p.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Proveedore> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Proveedore.class, id)));
  }

  @Override
  public List<Proveedore> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery("from Proveedore p order by p.id", Proveedore.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Proveedore> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery("from Proveedore p order by p.id", Proveedore.class);
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
          TypedQuery<Long> q = em.createQuery("select count(p) from Proveedore p", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Proveedore.class, id) != null);
  }
}
