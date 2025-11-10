package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Cargo;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record CargoRepository(EntityManager em) implements IRepository<Cargo> {

  @Override
  public Cargo save(Cargo entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Cargo entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionalExecutor.inTransaction(
        em,
        () ->
            em.createQuery("update Cargo C set C.activo = false where C.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Cargo> findById(int id) {
    return Optional.ofNullable(em.find(Cargo.class, id));
  }

  @Override
  public List<Cargo> findAll() {
    TypedQuery<Cargo> q = em.createQuery("from Cargo c order by c.id", Cargo.class);
    return q.getResultList();
  }

  @Override
  public List<Cargo> findAll(int offset, int limit) {
    TypedQuery<Cargo> q = em.createQuery("from Cargo c order by c.id", Cargo.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(c) from Cargo c", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Cargo.class, id) != null;
  }
}
