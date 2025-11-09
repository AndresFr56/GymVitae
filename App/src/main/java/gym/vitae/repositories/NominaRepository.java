package gym.vitae.repositories;

import gym.vitae.model.Nomina;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class NominaRepository implements IRepository<Nomina> {

  private final EntityManager em;

  public NominaRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Nomina save(Nomina entity) {
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
  public boolean update(Nomina entity) {
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
    // Soft-delete: mark payroll as anulada
    em.createQuery("update Nomina n set n.estado = 'anulada' where n.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<Nomina> findById(int id) {
    return Optional.ofNullable(em.find(Nomina.class, id));
  }

  @Override
  public List<Nomina> findAll() {
    TypedQuery<Nomina> q = em.createQuery("from Nomina n order by n.id", Nomina.class);
    return q.getResultList();
  }

  @Override
  public List<Nomina> findAll(int offset, int limit) {
    TypedQuery<Nomina> q = em.createQuery("from Nomina n order by n.id", Nomina.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(n) from Nomina n", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Nomina.class, id) != null;
  }

  // Convenience finder: by empleado, mes y anio
  public Optional<Nomina> findByEmpleadoAndPeriodo(int empleadoId, byte mes, int anio) {
    TypedQuery<Nomina> q =
        em.createQuery(
            "from Nomina n where n.empleado.id = :empId and n.mes = :mes and n.anio = :anio",
            Nomina.class);
    q.setParameter("empId", empleadoId);
    q.setParameter("mes", mes);
    q.setParameter("anio", anio);
    List<Nomina> res = q.getResultList();
    return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
  }
}
