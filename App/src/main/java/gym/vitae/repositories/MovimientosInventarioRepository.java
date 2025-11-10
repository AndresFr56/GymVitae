package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.MovimientosInventario;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record MovimientosInventarioRepository(EntityManager em)
    implements IRepository<MovimientosInventario> {

  public MovimientosInventarioRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public MovimientosInventario save(MovimientosInventario entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(MovimientosInventario entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
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
