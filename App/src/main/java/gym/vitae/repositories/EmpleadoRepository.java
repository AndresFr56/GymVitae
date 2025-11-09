package gym.vitae.repositories;

import gym.vitae.model.Empleado;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class EmpleadoRepository implements IRepository<Empleado> {

  private final EntityManager em;

  public EmpleadoRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Empleado save(Empleado entity) {
    EntityTransaction tx = em.getTransaction();
    try {
      if (!tx.isActive()) {
        tx.begin();
      }
      em.persist(entity);
      tx.commit();
      return entity;
    } catch (RuntimeException ex) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw ex;
    }
  }

  @Override
  public boolean update(Empleado entity) {
    EntityTransaction tx = em.getTransaction();
    try {
      if (!tx.isActive()) {
        tx.begin();
      }
      em.merge(entity);
      tx.commit();
      return true;
    } catch (RuntimeException ex) {
      if (tx.isActive()) {
        tx.rollback();
      }
      throw ex;
    }
  }

  @Override
  public void delete(int id) {
    // actualizando el estado a inactivo to'inactivo' ENUM ('activo', 'inactivo', 'vacaciones')
    em.createQuery("update Empleado e set e.estado = 'inactivo' where e.id = :id", Empleado.class);
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
