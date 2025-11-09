package gym.vitae.repositories;

import gym.vitae.model.Beneficio;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class BeneficioRepository implements IRepository<Beneficio> {

  private final EntityManager em;

  public BeneficioRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Beneficio save(Beneficio entity) {
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
  public boolean update(Beneficio entity) {
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
    em.createQuery("update Beneficio c set c.activo = false where c.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<Beneficio> findById(int id) {
    return Optional.ofNullable(em.find(Beneficio.class, id));
  }

  @Override
  public List<Beneficio> findAll() {
    TypedQuery<Beneficio> q = em.createQuery("from Beneficio b order by b.id", Beneficio.class);
    return q.getResultList();
  }

  @Override
  public List<Beneficio> findAll(int offset, int limit) {
    TypedQuery<Beneficio> q = em.createQuery("from Beneficio b order by b.id", Beneficio.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(b) from Beneficio b", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Beneficio.class, id) != null;
  }
}
