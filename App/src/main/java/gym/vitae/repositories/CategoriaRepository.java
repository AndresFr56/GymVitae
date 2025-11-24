package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Categoria;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record CategoriaRepository(DBConnectionManager db) implements IRepository<Categoria> {

  public CategoriaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Categoria save(Categoria entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Categoria entity) {
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
            em.createQuery("update Categoria c set c.activo = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Categoria> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Categoria.class, id)));
  }

  @Override
  public List<Categoria> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Categoria> q =
              em.createQuery("from Categoria c order by c.id", Categoria.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Categoria> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Categoria> q =
              em.createQuery("from Categoria c order by c.id", Categoria.class);
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
          TypedQuery<Long> q = em.createQuery("select count(c) from Categoria c", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Categoria.class, id) != null);
  }
}
