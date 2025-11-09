package gym.vitae.repositories;

import gym.vitae.model.Equipo;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class EquipoRepository implements IRepository<Equipo> {

  private final EntityManager em;

  public EquipoRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Equipo save(Equipo entity) {
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
  public boolean update(Equipo entity) {
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
    em.createQuery("update Equipo c set c.estado = 'fuera_servicio' where c.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<Equipo> findById(int id) {
    return Optional.ofNullable(em.find(Equipo.class, id));
  }

  @Override
  public List<Equipo> findAll() {
    TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
    return q.getResultList();
  }

  @Override
  public List<Equipo> findAll(int offset, int limit) {
    TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(e) from Equipo e", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Equipo.class, id) != null;
  }
}
