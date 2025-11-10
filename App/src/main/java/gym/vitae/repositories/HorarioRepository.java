package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Horario;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record HorarioRepository(EntityManager em) implements IRepository<Horario> {

  public HorarioRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Horario save(Horario entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Horario entity) {
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
            em.createQuery("update Horario h set h.activo = false where h.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Horario> findById(int id) {
    return Optional.ofNullable(em.find(Horario.class, id));
  }

  @Override
  public List<Horario> findAll() {
    TypedQuery<Horario> q = em.createQuery("from Horario h order by h.id", Horario.class);
    return q.getResultList();
  }

  @Override
  public List<Horario> findAll(int offset, int limit) {
    TypedQuery<Horario> q = em.createQuery("from Horario h order by h.id", Horario.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(h) from Horario h", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Horario.class, id) != null;
  }
}
