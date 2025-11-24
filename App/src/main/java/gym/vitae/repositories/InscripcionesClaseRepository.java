package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.InscripcionesClase;
import gym.vitae.model.enums.EstadoInscripcion;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record InscripcionesClaseRepository(DBConnectionManager db)
    implements IRepository<InscripcionesClase> {

  public InscripcionesClaseRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public InscripcionesClase save(InscripcionesClase entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(InscripcionesClase entity) {
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
    // cancelar subscripcion
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update InscripcionesClase ic set ic.estado = :estado where ic.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoInscripcion.CANCELADA)
                .executeUpdate());
  }

  @Override
  public Optional<InscripcionesClase> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(InscripcionesClase.class, id)));
  }

  @Override
  public List<InscripcionesClase> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<InscripcionesClase> q =
              em.createQuery("from InscripcionesClase i order by i.id", InscripcionesClase.class);
          return q.getResultList();
        });
  }

  @Override
  public List<InscripcionesClase> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<InscripcionesClase> q =
              em.createQuery("from InscripcionesClase i order by i.id", InscripcionesClase.class);
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
              em.createQuery("select count(i) from InscripcionesClase i", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> em.find(InscripcionesClase.class, id) != null);
  }
}
