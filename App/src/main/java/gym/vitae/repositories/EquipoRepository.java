package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Equipo;
import gym.vitae.model.enums.EstadoEquipo;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record EquipoRepository(EntityManager em) implements IRepository<Equipo> {

  public EquipoRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Equipo save(Equipo entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Equipo entity) {
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
            em.createQuery("update Equipo c set c.estado = :estado where c.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoEquipo.FUERA_SERVICIO)
                .executeUpdate());
  }

  @Override
  public Optional<Equipo> findById(int id) {
    return Optional.ofNullable(em.find(Equipo.class, id));
  }

  @Override
  public List<Equipo> findAll() {
    TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
    return q.getResultList();
  }

  @Override
  public List<Equipo> findAll(int offset, int limit) {
    TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(e) from Equipo e", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Equipo.class, id) != null;
  }
}
