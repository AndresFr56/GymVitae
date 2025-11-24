package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Cargo;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record CargoRepository(DBConnectionManager db) implements IRepository<Cargo> {

  public CargoRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Cargo save(Cargo entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Cargo entity) {
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
            em.createQuery("update Cargo C set C.activo = false where C.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Cargo> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Cargo.class, id)));
  }

  @Override
  public List<Cargo> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cargo> q = em.createQuery("from Cargo c order by c.id", Cargo.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Cargo> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cargo> q = em.createQuery("from Cargo c order by c.id", Cargo.class);
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
          TypedQuery<Long> q = em.createQuery("select count(c) from Cargo c", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Cargo.class, id) != null);
  }
}
