package gym.vitae.repositories;

import gym.vitae.core.TransactionalExecutor;
import gym.vitae.model.Cliente;
import gym.vitae.model.enums.EstadoCliente;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public record ClienteRepository(EntityManager em) implements IRepository<Cliente> {
  public ClienteRepository {
    if (em == null) {
      throw new IllegalArgumentException("EntityManager cannot be null");
    }
  }

  @Override
  public Cliente save(Cliente entity) {
    return TransactionalExecutor.inTransaction(
        em,
        () -> {
          em.persist(entity);
          return entity;
        });
  }

  @Override
  public boolean update(Cliente entity) {
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
            em.createQuery("update Cliente c set c.estado = :estado where c.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoCliente.SUSPENDIDO)
                .executeUpdate());
  }

  @Override
  public Optional<Cliente> findById(int id) {
    return Optional.ofNullable(em.find(Cliente.class, id));
  }

  @Override
  public List<Cliente> findAll() {
    TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
    return q.getResultList();
  }

  @Override
  public List<Cliente> findAll(int offset, int limit) {
    TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
    q.setFirstResult(offset);
    q.setMaxResults(limit);
    return q.getResultList();
  }

  @Override
  public long count() {
    TypedQuery<Long> q = em.createQuery("select count(c) from Cliente c", Long.class);
    return q.getSingleResult();
  }

  @Override
  public boolean existsById(int id) {
    return em.find(Cliente.class, id) != null;
  }

  public Optional<Cliente> findByCodigoCliente(String codigo) {
    TypedQuery<Cliente> q =
        em.createQuery("from Cliente c where c.codigoCliente = :codigo", Cliente.class);
    q.setParameter("codigo", codigo);
    List<Cliente> res = q.getResultList();
    return res.isEmpty() ? Optional.empty() : Optional.of(res.getFirst());
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ClienteRepository) obj;
    return Objects.equals(this.em, that.em);
  }

  @Override
  public String toString() {
    return "ClienteRepository[" + "em=" + em + ']';
  }
}
