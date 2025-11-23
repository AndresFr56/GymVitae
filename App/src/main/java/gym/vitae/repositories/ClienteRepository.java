package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.model.Cliente;
import gym.vitae.model.enums.EstadoCliente;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record ClienteRepository(DBConnectionManager db) implements IRepository<Cliente> {
  public ClienteRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Cliente save(Cliente entity) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Cliente entity) {
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
          em.createQuery("update Cliente c set c.estado = :estado where c.id = :id")
              .setParameter("id", id)
              .setParameter("estado", EstadoCliente.SUSPENDIDO)
              .executeUpdate();
        });
  }

  @Override
  public Optional<Cliente> findById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return Optional.ofNullable(em.find(Cliente.class, id));
        });
  }

  @Override
  public List<Cliente> findAll() {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Cliente> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
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
          TypedQuery<Long> q = em.createQuery("select count(c) from Cliente c", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          return em.find(Cliente.class, id) != null;
        });
  }

  public Optional<Cliente> findByCodigoCliente(String codigo) {
    return TransactionHandler.inTransaction(
        db,
        () -> {
          EntityManager em = db.getEntityManager();
          TypedQuery<Cliente> q =
              em.createQuery("from Cliente c where c.codigoCliente = :codigo", Cliente.class);
          q.setParameter("codigo", codigo);
          List<Cliente> res = q.getResultList();
          return res.isEmpty() ? Optional.empty() : Optional.of(res.getFirst());
        });
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ClienteRepository) obj;
    return Objects.equals(this.db, that.db);
  }

  @Override
  public String toString() {
    return "ClienteRepository[" + "db=" + db + ']';
  }
}
