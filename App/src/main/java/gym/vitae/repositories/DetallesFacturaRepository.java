package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.DetallesFactura;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record DetallesFacturaRepository(DBConnectionManager db)
    implements IRepository<DetallesFactura> {

  public DetallesFacturaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public DetallesFactura save(DetallesFactura entity) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(DetallesFactura entity) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.remove(findById(id));
        });
  }

  @Override
  public Optional<DetallesFactura> findById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return Optional.ofNullable(em.find(DetallesFactura.class, id));
        });
  }

  @Override
  public List<DetallesFactura> findAll() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<DetallesFactura> q =
              em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
          return q.getResultList();
        });
  }

  @Override
  public List<DetallesFactura> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<DetallesFactura> q =
              em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return q.getResultList();
        });
  }

  @Override
  public long count() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Long> q = em.createQuery("select count(d) from DetallesFactura d", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return em.find(DetallesFactura.class, id) != null;
        });
  }
}
