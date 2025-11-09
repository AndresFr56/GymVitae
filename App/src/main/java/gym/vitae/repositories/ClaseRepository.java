package gym.vitae.repositories;

import gym.vitae.model.Clase;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class ClaseRepository implements IRepository<Clase> {

  private final EntityManager em;

  public ClaseRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Clase save(Clase entity) {
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
  public boolean update(Clase entity) {
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
    em.createQuery("update Clase c set c.activa = false where c.id = :id")
        .setParameter("id", id)
        .executeUpdate();
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
