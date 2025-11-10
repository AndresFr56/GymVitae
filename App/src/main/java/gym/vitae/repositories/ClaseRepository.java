package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Clase;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record ClaseRepository(EntityManager em) implements IRepository<Clase> {

  public ClaseRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Clase save(Clase entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Clase entity) {
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
            em.createQuery("update Clase c set c.activa = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Clase> findById(int id) {
    return Optional.ofNullable(em.find(Clase.class, id));
  }

  @Override
  public List<Clase> findAll() {
    TypedQuery<Clase> q = em.createQuery("from Clase c order by c.id", Clase.class);
    return q.getResultList();
  }

  @Override
  public List<Clase> findAll(int offset, int limit) {
    TypedQuery<Clase> q = em.createQuery("from Clase c order by c.id", Clase.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(c) from Clase c", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Clase.class, id) != null;
  }

  // Model-specific logic examples
  public List<Clase> findActive() {
    TypedQuery<Clase> q =
        em.createQuery("from Clase c where c.activa = true order by c.nombre", Clase.class);
    return q.getResultList();
  }
}
