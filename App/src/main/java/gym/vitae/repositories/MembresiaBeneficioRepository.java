package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.MembresiaBeneficio;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record MembresiaBeneficioRepository(DBConnectionManager db)
    implements IRepository<MembresiaBeneficio> {

  public MembresiaBeneficioRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public MembresiaBeneficio save(MembresiaBeneficio entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(MembresiaBeneficio entity) {
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
            em.find(MembresiaBeneficio.class, id) != null
                ? em.createQuery("delete from MembresiaBeneficio mb where mb.id = :id")
                    .setParameter("id", id)
                    .executeUpdate()
                : 0);
  }

  @Override
  public Optional<MembresiaBeneficio> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(MembresiaBeneficio.class, id)));
  }

  @Override
  public List<MembresiaBeneficio> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
          return q.getResultList();
        });
  }

  @Override
  public List<MembresiaBeneficio> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
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
          TypedQuery<Long> q =
              em.createQuery("select count(mb) from MembresiaBeneficio mb", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> em.find(MembresiaBeneficio.class, id) != null);
  }
}
