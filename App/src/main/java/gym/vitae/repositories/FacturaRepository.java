package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Factura;
import gym.vitae.model.enums.EstadoFactura;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record FacturaRepository(EntityManager em) implements IRepository<Factura> {

  public FacturaRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Factura save(Factura entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Factura entity) {
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
            em.createQuery("update Factura f set f.estado = :estado where f.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoFactura.ANULADA)
                .executeUpdate());
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
