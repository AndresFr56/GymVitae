package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.EquipoMapper;
import gym.vitae.model.Equipo;
import gym.vitae.model.dtos.inventario.EquipoDetalleDTO;
import gym.vitae.model.dtos.inventario.EquipoListadoDTO;
import gym.vitae.model.enums.EstadoEquipo;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record EquipoRepository(DBConnectionManager db) implements IRepository<Equipo> {

  public EquipoRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Equipo save(Equipo entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Equipo entity) {
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
            em.createQuery("update Equipo c set c.estado = :estado where c.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoEquipo.FUERA_SERVICIO)
                .executeUpdate());
  }

  @Override
  public Optional<Equipo> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Equipo.class, id)));
  }

  @Override
  public List<Equipo> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Equipo> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
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
          TypedQuery<Long> q = em.createQuery("select count(e) from Equipo e", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Equipo.class, id) != null);
  }

  /**
   * Obtiene todos los equipos como DTOs de listado.
   *
   * @return Lista de EquipoListadoDTO
   */
  public List<EquipoListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
          return EquipoMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene equipos paginados como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de EquipoListadoDTO
   */
  public List<EquipoListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Equipo> q = em.createQuery("from Equipo e order by e.id", Equipo.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return EquipoMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca un equipo por ID y retorna el DTO de detalle.
   *
   * @param id ID del equipo
   * @return Optional de EquipoDetalleDTO
   */
  public Optional<EquipoDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          Equipo equipo = em.find(Equipo.class, id);
          return Optional.ofNullable(EquipoMapper.toDetalleDTO(equipo));
        });
  }
}
