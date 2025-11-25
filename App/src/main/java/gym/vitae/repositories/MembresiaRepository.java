package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.MembresiaMapper;
import gym.vitae.model.Membresia;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.enums.EstadoMembresia;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record MembresiaRepository(DBConnectionManager db) implements IRepository<Membresia> {

  public MembresiaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Membresia save(Membresia entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Membresia entity) {
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
            em.createQuery("update Membresia m set m.estado = :estado where m.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoMembresia.CANCELADA)
                .executeUpdate());
  }

  @Override
  public Optional<Membresia> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Membresia.class, id)));
  }

  @Override
  public List<Membresia> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Membresia> q =
              em.createQuery("from Membresia m order by m.id", Membresia.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Membresia> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Membresia> q =
              em.createQuery("from Membresia m order by m.id", Membresia.class);
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
          TypedQuery<Long> q = em.createQuery("select count(m) from Membresia m", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Membresia.class, id) != null);
  }

  /**
   * Obtiene todas las membresías como DTOs de listado.
   *
   * @return Lista de MembresiaListadoDTO
   */
  public List<MembresiaListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Membresia> q =
              em.createQuery("from Membresia m order by m.id", Membresia.class);
          return MembresiaMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene membresías paginadas como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de MembresiaListadoDTO
   */
  public List<MembresiaListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Membresia> q =
              em.createQuery("from Membresia m order by m.id", Membresia.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return MembresiaMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca una membresía por ID y retorna el DTO de detalle.
   *
   * @param id ID de la membresía
   * @return Optional de MembresiaDetalleDTO
   */
  public Optional<MembresiaDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          Membresia membresia = em.find(Membresia.class, id);
          return Optional.ofNullable(MembresiaMapper.toDetalleDTO(membresia));
        });
  }
}
