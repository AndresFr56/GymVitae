package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Proveedore;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record ProveedoreRepository(EntityManager em) implements IRepository<Proveedore> {

  public ProveedoreRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Proveedore save(Proveedore entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Proveedore entity) {
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
            em.createQuery("update Proveedore p set p.activo = false where p.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Proveedore> findById(int id) {
    return Optional.ofNullable(em.find(Proveedore.class, id));
  }

  @Override
  public List<Proveedore> findAll() {
    TypedQuery<Proveedore> q = em.createQuery("from Proveedore p order by p.id", Proveedore.class);
    return q.getResultList();
  }

  @Override
  public List<Proveedore> findAll(int offset, int limit) {
    TypedQuery<Proveedore> q = em.createQuery("from Proveedore p order by p.id", Proveedore.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(p) from Proveedore p", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Proveedore.class, id) != null;
  }
}
