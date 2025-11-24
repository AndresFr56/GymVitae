package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Horario;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record HorarioRepository(DBConnectionManager db) implements IRepository<Horario> {

  public HorarioRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Horario save(Horario entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Horario entity) {
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
            em.createQuery("update Horario h set h.activo = false where h.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Horario> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Horario.class, id)));
  }

  @Override
  public List<Horario> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Horario> q = em.createQuery("from Horario h order by h.id", Horario.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Horario> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Horario> q = em.createQuery("from Horario h order by h.id", Horario.class);
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
          TypedQuery<Long> q = em.createQuery("select count(h) from Horario h", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Horario.class, id) != null);
  }
}
