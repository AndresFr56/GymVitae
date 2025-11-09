package gym.vitae.repositories;

import gym.vitae.model.Factura;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class FacturaRepository implements IRepository<Factura> {

  private final EntityManager em;

  public FacturaRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Factura save(Factura entity) {
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
  public boolean update(Factura entity) {
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
    em.createQuery("update Factura f set f.estado='anulada' where f.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<Factura> findById(int id) {
    return Optional.ofNullable(em.find(Factura.class, id));
  }

  @Override
  public List<Factura> findAll() {
    TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
    return q.getResultList();
  }

  @Override
  public List<Factura> findAll(int offset, int limit) {
    TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(f) from Factura f", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Factura.class, id) != null;
  }
}
