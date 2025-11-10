package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Categoria;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record CategoriaRepository(EntityManager em) implements IRepository<Categoria> {

  @Override
  public Categoria save(Categoria entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Categoria entity) {
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
            em.createQuery("update Categoria c set c.activo = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Categoria> findById(int id) {
    return Optional.ofNullable(em.find(Categoria.class, id));
  }

  @Override
  public List<Categoria> findAll() {
    TypedQuery<Categoria> q = em.createQuery("from Categoria c order by c.id", Categoria.class);
    return q.getResultList();
  }

  @Override
  public List<Categoria> findAll(int offset, int limit) {
    TypedQuery<Categoria> q = em.createQuery("from Categoria c order by c.id", Categoria.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(c) from Categoria c", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Categoria.class, id) != null;
  }
}
