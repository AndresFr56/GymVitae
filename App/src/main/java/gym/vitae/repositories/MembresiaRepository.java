package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Membresia;
import gym.vitae.model.enums.EstadoMembresia;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record MembresiaRepository(EntityManager em) implements IRepository<Membresia> {

  public MembresiaRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Membresia save(Membresia entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Membresia entity) {
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
            em.createQuery("update Membresia m set m.estado = :estado where m.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoMembresia.CANCELADA)
                .executeUpdate());
  }

  @Override
  public Optional<Membresia> findById(int id) {
    return Optional.ofNullable(em.find(Membresia.class, id));
  }

  @Override
  public List<Membresia> findAll() {
    TypedQuery<Membresia> q = em.createQuery("from Membresia m order by m.id", Membresia.class);
    return q.getResultList();
  }

  @Override
  public List<Membresia> findAll(int offset, int limit) {
    TypedQuery<Membresia> q = em.createQuery("from Membresia m order by m.id", Membresia.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(m) from Membresia m", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Membresia.class, id) != null;
  }
}
