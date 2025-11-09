package gym.vitae.repositories;

import gym.vitae.model.InscripcionesClase;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class InscripcionesClaseRepository implements IRepository<InscripcionesClase> {

  private final EntityManager em;

  public InscripcionesClaseRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public InscripcionesClase save(InscripcionesClase entity) {
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
  public boolean update(InscripcionesClase entity) {
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
    // cancelar subscripcion
    em.createQuery("update InscripcionesClase ic set ic.estado='cancelada' where ic.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<InscripcionesClase> findById(int id) {
    return Optional.ofNullable(em.find(InscripcionesClase.class, id));
  }

  @Override
  public List<InscripcionesClase> findAll() {
    TypedQuery<InscripcionesClase> q =
        em.createQuery("from InscripcionesClase i order by i.id", InscripcionesClase.class);
    return q.getResultList();
  }

  @Override
  public List<InscripcionesClase> findAll(int offset, int limit) {
    TypedQuery<InscripcionesClase> q =
        em.createQuery("from InscripcionesClase i order by i.id", InscripcionesClase.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(i) from InscripcionesClase i", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(InscripcionesClase.class, id) != null;
  }
}
