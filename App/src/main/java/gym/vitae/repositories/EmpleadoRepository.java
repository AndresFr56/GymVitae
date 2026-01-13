package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.EmpleadoMapper;
import gym.vitae.model.Empleado;
import gym.vitae.model.dtos.empleado.EmpleadoAuthDTO;
import gym.vitae.model.dtos.empleado.EmpleadoDetalleDTO;
import gym.vitae.model.dtos.empleado.EmpleadoListadoDTO;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.model.enums.Genero;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record EmpleadoRepository(DBConnectionManager db) implements IRepository<Empleado> {

  public EmpleadoRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Empleado save(Empleado entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Empleado entity) {
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
    // actualizando el estado a inactivo to'inactivo' ENUM ('activo', 'inactivo', 'vacaciones')
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update Empleado e set e.estado = :estado where e.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoEmpleado.INACTIVO)
                .executeUpdate());
  }

  @Override
  public Optional<Empleado> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Empleado.class, id)));
  }

  @Override
  public List<Empleado> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Empleado> q = em.createQuery("from Empleado e order by e.id", Empleado.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Empleado> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Empleado> q =
              em.createQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo ORDER BY e.id", Empleado.class);
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
          TypedQuery<Long> q = em.createQuery("select count(e) from Empleado e", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Empleado.class, id) != null);
  }

  /**
   * Busca empleados con filtros opcionales y paginación.
   *
   * @param searchText Texto de búsqueda en nombres/apellidos (puede ser null)
   * @param cargoId ID del cargo para filtrar (puede ser null)
   * @param genero Género para filtrar (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de empleados que coinciden con los filtros
   */
  public List<Empleado> findAllWithFilters(
      String searchText, Integer cargoId, String genero, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          String jpql =
              buildFilterQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo WHERE 1=1",
                  searchText,
                  cargoId,
                  genero);
          jpql += " ORDER BY e.id";

          TypedQuery<Empleado> query = em.createQuery(jpql, Empleado.class);
          applyFilterParameters(query, searchText, cargoId, genero);

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          return query.getResultList();
        });
  }

  /**
   * Cuenta empleados con filtros opcionales.
   *
   * @param searchText Texto de búsqueda en nombres/apellidos (puede ser null)
   * @param cargoId ID del cargo para filtrar (puede ser null)
   * @param genero Género para filtrar (puede ser null)
   * @return Cantidad de empleados que coinciden con los filtros
   */
  public long countWithFilters(String searchText, Integer cargoId, String genero) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          String jpql =
              buildFilterQuery(
                  "SELECT COUNT(e) FROM Empleado e WHERE 1=1", searchText, cargoId, genero);

          TypedQuery<Long> query = em.createQuery(jpql, Long.class);
          applyFilterParameters(query, searchText, cargoId, genero);

          return query.getSingleResult();
        });
  }

  private String buildFilterQuery(
      String baseQuery, String searchText, Integer cargoId, String genero) {
    StringBuilder jpql = new StringBuilder(baseQuery);

    if (searchText != null && !searchText.trim().isEmpty()) {
      jpql.append(
          " AND (LOWER(e.nombres) LIKE :searchText OR LOWER(e.apellidos) LIKE :searchText)");
    }

    if (cargoId != null) {
      jpql.append(" AND e.cargo.id = :cargoId");
    }

    if (genero != null && !genero.isEmpty()) {
      jpql.append(" AND e.genero = :genero");
    }

    return jpql.toString();
  }

  private void applyFilterParameters(
      TypedQuery<?> query, String searchText, Integer cargoId, String genero) {
    if (searchText != null && !searchText.trim().isEmpty()) {
      query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
    }

    if (cargoId != null) {
      query.setParameter("cargoId", cargoId);
    }

    if (genero != null && !genero.isEmpty()) {
      query.setParameter("genero", Genero.valueOf(genero));
    }
  }

  /**
   * Obtiene todos los empleados como DTOs de listado. Optimizado para tablas, solo carga los campos
   * necesarios.
   *
   * @return Lista de EmpleadoListadoDTO
   */
  public List<EmpleadoListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Empleado> q =
              em.createQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo WHERE e.estado <> :estadoInactivo ORDER BY e.id",
                  Empleado.class);
          q.setParameter("estadoInactivo", EstadoEmpleado.INACTIVO);
          List<Empleado> empleados = q.getResultList();
          return EmpleadoMapper.toListadoDTOList(empleados);
        });
  }

  /**
   * Obtiene empleados con paginación como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de EmpleadoListadoDTO
   */
  public List<EmpleadoListadoDTO> findAllListado(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Empleado> q =
              em.createQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo WHERE e.estado <> :estadoInactivo ORDER BY e.id",
                  Empleado.class);
          q.setParameter("estadoInactivo", EstadoEmpleado.INACTIVO);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          List<Empleado> empleados = q.getResultList();
          return EmpleadoMapper.toListadoDTOList(empleados);
        });
  }

  /**
   * Busca empleados con filtros opcionales y paginación, retornando DTOs.
   *
   * @param searchText Texto de búsqueda en nombres/apellidos (puede ser null)
   * @param cargoId ID del cargo para filtrar (puede ser null)
   * @param genero Género para filtrar (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de EmpleadoListadoDTO que coinciden con los filtros
   */
  public List<EmpleadoListadoDTO> findAllListadoWithFilters(
      String searchText, Integer cargoId, String genero, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          String jpql =
              buildFilterQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo WHERE e.estado <> :estadoInactivo",
                  searchText,
                  cargoId,
                  genero);
          jpql += " ORDER BY e.id";

          TypedQuery<Empleado> query = em.createQuery(jpql, Empleado.class);
          query.setParameter("estadoInactivo", EstadoEmpleado.INACTIVO);
          applyFilterParameters(query, searchText, cargoId, genero);

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          List<Empleado> empleados = query.getResultList();
          return EmpleadoMapper.toListadoDTOList(empleados);
        });
  }

  /**
   * Busca un empleado por email o nombre completo para autenticación. Carga el cargo con JOIN
   * FETCH.
   *
   * @param emailOrUsername Email o nombre completo del empleado
   * @return Optional de EmpleadoAuthDTO
   */
  public Optional<EmpleadoAuthDTO> findByEmailOrUsernameForAuth(String emailOrUsername) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          String searchTerm = emailOrUsername.trim();
          TypedQuery<Empleado> q =
              em.createQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo "
                      + "WHERE e.email = :search OR CONCAT(e.nombres, ' ', e.apellidos) = :search",
                  Empleado.class);
          q.setParameter("search", searchTerm);

          return q.getResultStream().findFirst().map(EmpleadoMapper::toAuthDTO);
        });
  }

  /**
   * Busca un empleado por ID y retorna el DTO de detalle con cargo cargado.
   *
   * @param id ID del empleado
   * @return Optional de EmpleadoDetalleDTO
   */
  public Optional<EmpleadoDetalleDTO> findByIdDetalle(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Empleado> q =
              em.createQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo WHERE e.id = :id",
                  Empleado.class);
          q.setParameter("id", id);

          return q.getResultStream().findFirst().map(EmpleadoMapper::toDetalleDTO);
        });
  }

  /**
   * Busca empleados activos como DTOs de listado.
   *
   * @return Lista de EmpleadoListadoDTO solo con empleados activos
   */
  public List<EmpleadoListadoDTO> findActivosListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Empleado> q =
              em.createQuery(
                  "SELECT e FROM Empleado e LEFT JOIN FETCH e.cargo "
                      + "WHERE e.estado = :estado ORDER BY e.id",
                  Empleado.class);
          q.setParameter("estado", EstadoEmpleado.ACTIVO);
          List<Empleado> empleados = q.getResultList();
          return EmpleadoMapper.toListadoDTOList(empleados);
        });
  }

  /**
   * Verifica si existe un empleado con la cédula especificada.
   *
   * @param cedula Cédula a verificar
   * @param excludeId ID a excluir de la búsqueda (para updates), puede ser null
   * @return true si existe otro empleado con esa cédula
   */
  public boolean existsByCedula(String cedula, Integer excludeId) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          String jpql = "SELECT COUNT(e) FROM Empleado e WHERE e.cedula = :cedula";
          if (excludeId != null) {
            jpql += " AND e.id <> :excludeId";
          }
          TypedQuery<Long> q = em.createQuery(jpql, Long.class);
          q.setParameter("cedula", cedula.trim());
          if (excludeId != null) {
            q.setParameter("excludeId", excludeId);
          }
          return q.getSingleResult() > 0;
        });
  }

  /**
   * Verifica si existe un empleado con los mismos nombres y apellidos.
   *
   * @param nombres Nombres a verificar
   * @param apellidos Apellidos a verificar
   * @param excludeId ID a excluir de la búsqueda (para updates), puede ser null
   * @return true si existe otro empleado con esos nombres y apellidos
   */
  public boolean existsByNombresApellidos(String nombres, String apellidos, Integer excludeId) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          String jpql =
              "SELECT COUNT(e) FROM Empleado e WHERE LOWER(e.nombres) = LOWER(:nombres) AND LOWER(e.apellidos) = LOWER(:apellidos)";
          if (excludeId != null) {
            jpql += " AND e.id <> :excludeId";
          }
          TypedQuery<Long> q = em.createQuery(jpql, Long.class);
          q.setParameter("nombres", nombres.trim());
          q.setParameter("apellidos", apellidos.trim());
          if (excludeId != null) {
            q.setParameter("excludeId", excludeId);
          }
          return q.getSingleResult() > 0;
        });
  }
}
