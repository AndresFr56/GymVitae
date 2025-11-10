package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.TiposMembresia;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record TiposMembresiaRepository(EntityManager em) implements IRepository<TiposMembresia> {

  public TiposMembresiaRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public TiposMembresia save(TiposMembresia entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(TiposMembresia entity) {
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
            em.createQuery("update TiposMembresia t set t.activo = false where t.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<TiposMembresia> findById(int id) {
    return Optional.ofNullable(em.find(TiposMembresia.class, id));
  }

  @Override
  public List<TiposMembresia> findAll() {
    TypedQuery<TiposMembresia> q =
        em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
    return q.getResultList();
  }

  @Override
  public List<TiposMembresia> findAll(int offset, int limit) {
    TypedQuery<TiposMembresia> q =
        em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(t) from TiposMembresia t", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(TiposMembresia.class, id) != null;
  }
}
