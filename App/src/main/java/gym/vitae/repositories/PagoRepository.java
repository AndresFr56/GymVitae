package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Pago;
import gym.vitae.model.enums.EstadoNomina;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record PagoRepository(EntityManager em) implements IRepository<Pago> {

  public PagoRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Pago save(Pago entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Pago entity) {
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
            em.createQuery("update Pago p set p.estado = :estado where p.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoNomina.ANULADA)
                .executeUpdate());
  }

  @Override
  public Optional<Pago> findById(int id) {
    return Optional.ofNullable(em.find(Pago.class, id));
  }

  @Override
  public List<Pago> findAll() {
    TypedQuery<Pago> q = em.createQuery("from Pago p order by p.id", Pago.class);
    return q.getResultList();
  }

  @Override
  public List<Pago> findAll(int offset, int limit) {
    TypedQuery<Pago> q = em.createQuery("from Pago p order by p.id", Pago.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(p) from Pago p", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Pago.class, id) != null;
  }
}
