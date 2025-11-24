package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Factura;
import gym.vitae.model.enums.EstadoFactura;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record FacturaRepository(DBConnectionManager db) implements IRepository<Factura> {

  public FacturaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Factura save(Factura entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Factura entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.merge(entity);
          em.flush();
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update Factura f set f.estado = :estado where f.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoFactura.ANULADA)
                .executeUpdate());
  }

  @Override
  public Optional<Factura> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Factura.class, id)));
  }

  @Override
  public List<Factura> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Factura> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return q.getResultList();
        });
  }

  @Override
  public long count() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Long> q = em.createQuery("select count(f) from Factura f", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Factura.class, id) != null);
  }
}
