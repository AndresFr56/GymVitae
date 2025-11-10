package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Empleado;
import gym.vitae.model.enums.EstadoEmpleado;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record EmpleadoRepository(EntityManager em) implements IRepository<Empleado> {

  public EmpleadoRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Empleado save(Empleado entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Empleado entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    // actualizando el estado a inactivo to'inactivo' ENUM ('activo', 'inactivo', 'vacaciones')
    TransactionalExecutor.inTransaction(
        em,
        () ->
            em.createQuery("update Empleado e set e.estado = :estado where e.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoEmpleado.INACTIVO)
                .executeUpdate());
  }

  @Override
  public Optional<Empleado> findById(int id) {
    return Optional.ofNullable(em.find(Empleado.class, id));
  }

  @Override
  public List<Empleado> findAll() {
    TypedQuery<Empleado> q = em.createQuery("from Empleado e order by e.id", Empleado.class);
    return q.getResultList();
  }

  @Override
  public List<Empleado> findAll(int offset, int limit) {
    TypedQuery<Empleado> q = em.createQuery("from Empleado e order by e.id", Empleado.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(e) from Empleado e", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Empleado.class, id) != null;
  }
}
