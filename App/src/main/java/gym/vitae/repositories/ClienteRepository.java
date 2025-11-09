package gym.vitae.repositories;

import gym.vitae.model.Cliente;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;

public class ClienteRepository implements IRepository<Cliente> {

  private final EntityManager em;

  public ClienteRepository(EntityManager em) {
    this.em = em;
  }

  @Override
  public Cliente save(Cliente entity) {
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
  public boolean update(Cliente entity) {
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
    em.createQuery("update Cliente c set c.estado = 'suspendido' where c.id = :id")
        .setParameter("id", id)
        .executeUpdate();
  }

  @Override
  public Optional<Cliente> findById(int id) {
    return Optional.ofNullable(em.find(Cliente.class, id));
  }

  @Override
  public List<Cliente> findAll() {
    TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
    return q.getResultList();
  }

  @Override
  public List<Cliente> findAll(int offset, int limit) {
    TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(c) from Cliente c", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Cliente.class, id) != null;
  }

  // Model-specific helper: find by codigoCliente
  public Optional<Cliente> findByCodigoCliente(String codigo) {
    TypedQuery<Cliente> q =
        em.createQuery("from Cliente c where c.codigoCliente = :codigo", Cliente.class);
    q.setParameter("codigo", codigo);
    List<Cliente> res = q.getResultList();
    return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
  }
}
