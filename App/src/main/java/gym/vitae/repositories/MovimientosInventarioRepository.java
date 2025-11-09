package gym.vitae.repositories;

import gym.vitae.model.MovimientosInventario;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class MovimientosInventarioRepository implements IRepository<MovimientosInventario> {

  private final EntityManager em;

  public MovimientosInventarioRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public MovimientosInventario save(MovimientosInventario entity) {
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
  public boolean update(MovimientosInventario entity) {
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
    em.remove(em.find(MovimientosInventario.class, id));
  }

  @Override
  public Optional<MovimientosInventario> findById(int id) {
    return Optional.ofNullable(em.find(MovimientosInventario.class, id));
  }

  @Override
  public List<MovimientosInventario> findAll() {
    TypedQuery<MovimientosInventario> q =
        em.createQuery("from MovimientosInventario m order by m.id", MovimientosInventario.class);
    return q.getResultList();
  }

  @Override
  public List<MovimientosInventario> findAll(int offset, int limit) {
    TypedQuery<MovimientosInventario> q =
        em.createQuery("from MovimientosInventario m order by m.id", MovimientosInventario.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(m) from MovimientosInventario m", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(MovimientosInventario.class, id) != null;
  }
}
