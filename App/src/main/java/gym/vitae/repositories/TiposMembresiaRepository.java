package gym.vitae.repositories;

import gym.vitae.model.TiposMembresia;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class TiposMembresiaRepository implements IRepository<TiposMembresia> {

  private final EntityManager em;

  public TiposMembresiaRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public TiposMembresia save(TiposMembresia entity) {
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
  public boolean update(TiposMembresia entity) {
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
    em.createQuery("update TiposMembresia t set t.activo = false where t.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<TiposMembresia> findById(int id) {
    return Optional.ofNullable(em.find(TiposMembresia.class, id));
  }

  @Override
  public List<TiposMembresia> findAll() {
    TypedQuery<TiposMembresia> q =
        em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
    return q.getResultList();
  }

  @Override
  public List<TiposMembresia> findAll(int offset, int limit) {
    TypedQuery<TiposMembresia> q =
        em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(t) from TiposMembresia t", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(TiposMembresia.class, id) != null;
  }
}
