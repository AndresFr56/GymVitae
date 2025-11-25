package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.TipoMembresiaMapper;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.TipoMembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.TipoMembresiaListadoDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record TiposMembresiaRepository(DBConnectionManager db)
    implements IRepository<TiposMembresia> {

  public TiposMembresiaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public TiposMembresia save(TiposMembresia entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(TiposMembresia entity) {
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
            em.createQuery("update TiposMembresia t set t.activo = false where t.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<TiposMembresia> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(TiposMembresia.class, id)));
  }

  @Override
  public List<TiposMembresia> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<TiposMembresia> q =
              em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
          return q.getResultList();
        });
  }

  @Override
  public List<TiposMembresia> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<TiposMembresia> q =
              em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
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
          TypedQuery<Long> q = em.createQuery("select count(t) from TiposMembresia t", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(TiposMembresia.class, id) != null);
  }

  /**
   * Obtiene todos los tipos de membresía como DTOs de listado.
   *
   * @return Lista de TipoMembresiaListadoDTO
   */
  public List<TipoMembresiaListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<TiposMembresia> q =
              em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
          return TipoMembresiaMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene tipos de membresía paginados como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de TipoMembresiaListadoDTO
   */
  public List<TipoMembresiaListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<TiposMembresia> q =
              em.createQuery("from TiposMembresia t order by t.id", TiposMembresia.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return TipoMembresiaMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca un tipo de membresía por ID y retorna el DTO de detalle.
   *
   * @param id ID del tipo de membresía
   * @return Optional de TipoMembresiaDetalleDTO
   */
  public Optional<TipoMembresiaDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TiposMembresia tipo = em.find(TiposMembresia.class, id);
          return Optional.ofNullable(TipoMembresiaMapper.toDetalleDTO(tipo));
        });
  }
}
