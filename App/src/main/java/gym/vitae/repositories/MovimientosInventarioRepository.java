package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.MovimientosInventario;
import java.util.List;
import java.util.Optional;
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
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(MovimientosInventario entity) {
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
            em.find(MovimientosInventario.class, id) != null
                ? em.createQuery("delete from MovimientosInventario m where m.id = :id")
                    .setParameter("id", id)
                    .executeUpdate()
                : 0);
  }

  @Override
  public Optional<MovimientosInventario> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(MovimientosInventario.class, id)));
  }

  @Override
  public List<MovimientosInventario> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
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
        em -> {
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
        em -> {
          TypedQuery<Long> q =
              em.createQuery("select count(m) from MovimientosInventario m", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> em.find(MovimientosInventario.class, id) != null);
  }
}
