package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Iva;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record IvaRepository(EntityManager em) implements IRepository<Iva> {

  public IvaRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Iva save(Iva entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Iva entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    // Soft-delete: mark inactive
    TransactionalExecutor.inTransaction(
        em,
        () ->
            em.createQuery("update Iva i set i.activo = false where i.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Iva> findById(int id) {
    return Optional.ofNullable(em.find(Iva.class, id));
  }

  @Override
  public List<Iva> findAll() {
    TypedQuery<Iva> q = em.createQuery("from Iva i order by i.id", Iva.class);
    return q.getResultList();
  }

  @Override
  public List<Iva> findAll(int offset, int limit) {
    TypedQuery<Iva> q = em.createQuery("from Iva i order by i.id", Iva.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(i) from Iva i", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Iva.class, id) != null;
  }
}
