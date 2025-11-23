package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Empleado;
import gym.vitae.model.enums.EstadoEmpleado;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record EmpleadoRepository(DBConnectionManager db) implements IRepository<Empleado> {

  public EmpleadoRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Empleado save(Empleado entity) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Empleado entity) {
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
    // actualizando el estado a inactivo to'inactivo' ENUM ('activo', 'inactivo', 'vacaciones')
    TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.createQuery("update Empleado e set e.estado = :estado where e.id = :id")
              .setParameter("id", id)
              .setParameter("estado", EstadoEmpleado.INACTIVO)
              .executeUpdate();
        });
  }

  @Override
  public Optional<Empleado> findById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return Optional.ofNullable(em.find(Empleado.class, id));
        });
  }

  @Override
  public List<Empleado> findAll() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Empleado> q = em.createQuery("from Empleado e order by e.id", Empleado.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Empleado> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Empleado> q = em.createQuery("from Empleado e order by e.id", Empleado.class);
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
          TypedQuery<Long> q = em.createQuery("select count(e) from Empleado e", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return em.find(Empleado.class, id) != null;
        });
  }
}
