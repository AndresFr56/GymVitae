package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Producto;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record ProductoRepository(EntityManager em) implements IRepository<Producto> {

  public ProductoRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Producto save(Producto entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Producto entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionalExecutor.inTransaction(
        em,
        () ->
            em.createQuery("update Producto p set p.activo = false where p.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Producto> findById(int id) {
    return Optional.ofNullable(em.find(Producto.class, id));
  }

  @Override
  public List<Producto> findAll() {
    TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
    return q.getResultList();
  }

  @Override
  public List<Producto> findAll(int offset, int limit) {
    TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(p) from Producto p", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Producto.class, id) != null;
  }
}
