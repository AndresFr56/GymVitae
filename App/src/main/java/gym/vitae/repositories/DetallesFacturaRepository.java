package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.DetallesFactura;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record DetallesFacturaRepository(EntityManager em) implements IRepository<DetallesFactura> {

  public DetallesFacturaRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public DetallesFactura save(DetallesFactura entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(DetallesFactura entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionalExecutor.inTransaction(em, () -> em.remove(findById(id)));
  }

  @Override
  public Optional<DetallesFactura> findById(int id) {
    return Optional.ofNullable(em.find(DetallesFactura.class, id));
  }

  @Override
  public List<DetallesFactura> findAll() {
    TypedQuery<DetallesFactura> q =
        em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
    return q.getResultList();
  }

  @Override
  public List<DetallesFactura> findAll(int offset, int limit) {
    TypedQuery<DetallesFactura> q =
        em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(d) from DetallesFactura d", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(DetallesFactura.class, id) != null;
  }
}
