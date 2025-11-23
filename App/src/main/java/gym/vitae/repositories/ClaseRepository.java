package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Clase;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record ClaseRepository(DBConnectionManager db) implements IRepository<Clase> {

  public ClaseRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Clase save(Clase entity) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Clase entity) {
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
          em.createQuery("update Clase c set c.activa = false where c.id = :id")
              .setParameter("id", id)
              .executeUpdate();
        });
  }

  @Override
  public Optional<Clase> findById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return Optional.ofNullable(em.find(Clase.class, id));
        });
  }

  @Override
  public List<Clase> findAll() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Clase> q = em.createQuery("from Clase c order by c.id", Clase.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Clase> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Clase> q = em.createQuery("from Clase c order by c.id", Clase.class);
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
          TypedQuery<Long> q = em.createQuery("select count(c) from Clase c", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return em.find(Clase.class, id) != null;
        });
  }

  // Model-specific logic examples
  public List<Clase> findActive() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Clase> q =
              em.createQuery("from Clase c where c.activa = true order by c.nombre", Clase.class);
          return q.getResultList();
        });
  }
}
