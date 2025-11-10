package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.MembresiaBeneficio;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record MembresiaBeneficioRepository(EntityManager em)
    implements IRepository<MembresiaBeneficio> {

  public MembresiaBeneficioRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public MembresiaBeneficio save(MembresiaBeneficio entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(MembresiaBeneficio entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.merge(entity);
          return true;
        });
  }

  @Override
  public void delete(int id) {
    TransactionalExecutor.inTransaction(em, () -> em.remove(em.find(MembresiaBeneficio.class, id)));
  }

  @Override
  public Optional<MembresiaBeneficio> findById(int id) {
    return Optional.ofNullable(em.find(MembresiaBeneficio.class, id));
  }

  @Override
  public List<MembresiaBeneficio> findAll() {
    TypedQuery<MembresiaBeneficio> q =
        em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
    return q.getResultList();
  }

  @Override
  public List<MembresiaBeneficio> findAll(int offset, int limit) {
    TypedQuery<MembresiaBeneficio> q =
        em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(mb) from MembresiaBeneficio mb", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(MembresiaBeneficio.class, id) != null;
  }
}
