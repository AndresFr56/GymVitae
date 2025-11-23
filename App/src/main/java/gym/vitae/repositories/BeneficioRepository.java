package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Beneficio;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record BeneficioRepository(EntityManager em) implements IRepository<Beneficio> {

  public BeneficioRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Beneficio save(Beneficio entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Beneficio entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionalExecutor.inTransaction(
        em,
        () ->
            em.createQuery("update Beneficio c set c.activo = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Beneficio> findById(int id) {
    return Optional.ofNullable(em.find(Beneficio.class, id));
  }

  @Override
  public List<Beneficio> findAll() {
    TypedQuery<Beneficio> q = em.createQuery("from Beneficio b order by b.id", Beneficio.class);
    return q.getResultList();
  }

  @Override
  public List<Beneficio> findAll(int offset, int limit) {
    TypedQuery<Beneficio> q = em.createQuery("from Beneficio b order by b.id", Beneficio.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(b) from Beneficio b", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Beneficio.class, id) != null;
  }
}
