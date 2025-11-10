package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.InscripcionesClase;
import gym.vitae.model.enums.EstadoInscripcion;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record InscripcionesClaseRepository(EntityManager em)
    implements IRepository<InscripcionesClase> {

  public InscripcionesClaseRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public InscripcionesClase save(InscripcionesClase entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(InscripcionesClase entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    // cancelar subscripcion
    TransactionalExecutor.inTransaction(
        em,
        () ->
            em.createQuery("update InscripcionesClase ic set ic.estado = :estado where ic.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoInscripcion.CANCELADA)
                .executeUpdate());
  }

  @Override
  public Optional<InscripcionesClase> findById(int id) {
    return Optional.ofNullable(em.find(InscripcionesClase.class, id));
  }

  @Override
  public List<InscripcionesClase> findAll() {
    TypedQuery<InscripcionesClase> q =
        em.createQuery("from InscripcionesClase i order by i.id", InscripcionesClase.class);
    return q.getResultList();
  }

  @Override
  public List<InscripcionesClase> findAll(int offset, int limit) {
    TypedQuery<InscripcionesClase> q =
        em.createQuery("from InscripcionesClase i order by i.id", InscripcionesClase.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(i) from InscripcionesClase i", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(InscripcionesClase.class, id) != null;
  }
}
