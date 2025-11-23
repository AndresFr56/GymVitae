package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.MovimientosInventario;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record MovimientosInventarioRepository(DBConnectionManager db)
    implements IRepository<MovimientosInventario> {

  public MovimientosInventarioRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public MovimientosInventario save(MovimientosInventario entity) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(MovimientosInventario entity) {
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
          em.remove(em.find(MovimientosInventario.class, id));
        });
  }

  @Override
  public Optional<MovimientosInventario> findById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return Optional.ofNullable(em.find(MovimientosInventario.class, id));
        });
  }

  @Override
  public List<MovimientosInventario> findAll() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<MovimientosInventario> q =
              em.createQuery(
                  "from MovimientosInventario m order by m.id", MovimientosInventario.class);
          return q.getResultList();
        });
  }

  @Override
  public List<MovimientosInventario> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<MovimientosInventario> q =
              em.createQuery(
                  "from MovimientosInventario m order by m.id", MovimientosInventario.class);
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
          TypedQuery<Long> q =
              em.createQuery("select count(m) from MovimientosInventario m", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return em.find(MovimientosInventario.class, id) != null;
        });
  }
}
