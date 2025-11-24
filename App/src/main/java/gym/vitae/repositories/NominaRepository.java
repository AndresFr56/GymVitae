package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Nomina;
import gym.vitae.model.enums.EstadoNomina;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record NominaRepository(DBConnectionManager db) implements IRepository<Nomina> {

  public NominaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Nomina save(Nomina entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Nomina entity) {
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
    // Soft-delete: mark payroll as anulada
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update Nomina n set n.estado = :estado where n.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoNomina.ANULADA)
                .executeUpdate());
  }

  @Override
  public Optional<Nomina> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Nomina.class, id)));
  }

  @Override
  public List<Nomina> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Nomina> q = em.createQuery("from Nomina n order by n.id", Nomina.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Nomina> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Nomina> q = em.createQuery("from Nomina n order by n.id", Nomina.class);
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
          TypedQuery<Long> q = em.createQuery("select count(n) from Nomina n", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Nomina.class, id) != null);
  }

  // Convenience finder: by empleado, mes y anio
  public Optional<Nomina> findByEmpleadoAndPeriodo(int empleadoId, byte mes, int anio) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Nomina> q =
              em.createQuery(
                  "from Nomina n where n.empleado.id = :empId and n.mes = :mes and n.anio = :anio",
                  Nomina.class);
          q.setParameter("empId", empleadoId);
          q.setParameter("mes", mes);
          q.setParameter("anio", anio);
          List<Nomina> res = q.getResultList();
          return res.isEmpty() ? Optional.empty() : Optional.of(res.get(0));
        });
  }
}
