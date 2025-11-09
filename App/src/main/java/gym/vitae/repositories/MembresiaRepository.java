package gym.vitae.repositories;

import gym.vitae.model.Membresia;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class MembresiaRepository implements IRepository<Membresia> {

  private final EntityManager em;

  public MembresiaRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Membresia save(Membresia entity) {
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
  public boolean update(Membresia entity) {
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
    em.createQuery("update Membresia m set m.estado='cancelada' where m.id = :id")
        .setParameter("id", id)
        .executeUpdate();
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
