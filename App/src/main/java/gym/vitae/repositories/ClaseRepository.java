package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.ClaseMapper;
import gym.vitae.model.Clase;
import gym.vitae.model.dtos.clase.ClaseDetalleDTO;
import gym.vitae.model.dtos.clase.ClaseListadoDTO;
import gym.vitae.model.enums.NivelClase;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record ClaseRepository(DBConnectionManager db) implements IRepository<Clase> {

  public ClaseRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Clase save(Clase entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Clase entity) {
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
            em.createQuery("update Clase c set c.activa = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Clase> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Clase.class, id)));
  }

  @Override
  public List<Clase> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q = em.createQuery("from Clase c order by c.id", Clase.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Clase> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q = em.createQuery("from Clase c order by c.id", Clase.class);
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
          TypedQuery<Long> q = em.createQuery("select count(c) from Clase c", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Clase.class, id) != null);
  }

  // Model-specific logic examples
  public List<Clase> findActive() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q =
              em.createQuery("from Clase c where c.activa = true order by c.nombre", Clase.class);
          return q.getResultList();
        });
  }

  /**
   * Busca clases con filtros opcionales y paginación.
   *
   * @param searchText Texto de búsqueda en nombre (puede ser null)
   * @param nivel Nivel para filtrar (puede ser null)
   * @param conCupos Filtrar solo clases activas con capacidad disponible (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de clases que coinciden con los filtros
   */
  public List<Clase> findAllWithFilters(
      String searchText, String nivel, Boolean conCupos, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT c FROM Clase c WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(" AND LOWER(c.nombre) LIKE :searchText");
          }

          if (nivel != null && !nivel.isEmpty()) {
            jpql.append(" AND c.nivel = :nivel");
          }

          if (conCupos != null && conCupos) {
            jpql.append(" AND c.activa = true AND c.capacidadMaxima > 0");
          }

          jpql.append(" ORDER BY c.nombre");

          TypedQuery<Clase> query = em.createQuery(jpql.toString(), Clase.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (nivel != null && !nivel.isEmpty()) {
            query.setParameter("nivel", gym.vitae.model.enums.NivelClase.valueOf(nivel));
          }

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          return query.getResultList();
        });
  }

  /**
   * Cuenta clases con filtros opcionales.
   *
   * @param searchText Texto de búsqueda en nombre (puede ser null)
   * @param nivel Nivel para filtrar (puede ser null)
   * @param conCupos Filtrar solo clases activas con capacidad disponible (puede ser null)
   * @return Cantidad de clases que coinciden con los filtros
   */
  public long countWithFilters(String searchText, String nivel, Boolean conCupos) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT COUNT(c) FROM Clase c WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(" AND LOWER(c.nombre) LIKE :searchText");
          }

          if (nivel != null && !nivel.isEmpty()) {
            jpql.append(" AND c.nivel = :nivel");
          }

          if (conCupos != null && conCupos) {
            jpql.append(" AND c.activa = true AND c.capacidadMaxima > 0");
          }

          TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (nivel != null && !nivel.isEmpty()) {
            query.setParameter("nivel", gym.vitae.model.enums.NivelClase.valueOf(nivel));
          }

          return query.getSingleResult();
        });
  }

  /**
   * Obtiene todas las clases como DTOs de listado.
   *
   * @return Lista de ClaseListadoDTO
   */
  public List<ClaseListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q =
              em.createQuery("SELECT c FROM Clase c ORDER BY c.nombre", Clase.class);
          List<Clase> clases = q.getResultList();
          return ClaseMapper.toListadoDTOList(clases);
        });
  }

  /**
   * Obtiene clases con paginación como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de ClaseListadoDTO
   */
  public List<ClaseListadoDTO> findAllListado(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q =
              em.createQuery("SELECT c FROM Clase c ORDER BY c.nombre", Clase.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          List<Clase> clases = q.getResultList();
          return ClaseMapper.toListadoDTOList(clases);
        });
  }

  /**
   * Busca clases con filtros y retorna DTOs de listado.
   *
   * @param searchText Texto de búsqueda en nombre (puede ser null)
   * @param nivel Nivel para filtrar (puede ser null)
   * @param conCupos Filtrar solo clases activas con capacidad disponible (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de ClaseListadoDTO
   */
  public List<ClaseListadoDTO> findAllListadoWithFilters(
      String searchText, String nivel, Boolean conCupos, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT c FROM Clase c WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(" AND LOWER(c.nombre) LIKE :searchText");
          }

          if (nivel != null && !nivel.isEmpty()) {
            jpql.append(" AND c.nivel = :nivel");
          }

          if (conCupos != null && conCupos) {
            jpql.append(" AND c.activa = true AND c.capacidadMaxima > 0");
          }

          jpql.append(" ORDER BY c.nombre");

          TypedQuery<Clase> query = em.createQuery(jpql.toString(), Clase.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (nivel != null && !nivel.isEmpty()) {
            query.setParameter("nivel", NivelClase.valueOf(nivel));
          }

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          List<Clase> clases = query.getResultList();
          return ClaseMapper.toListadoDTOList(clases);
        });
  }

  /**
   * Busca una clase por ID y retorna el DTO de detalle.
   *
   * @param id ID de la clase
   * @return Optional de ClaseDetalleDTO
   */
  public Optional<ClaseDetalleDTO> findByIdDetalle(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q =
              em.createQuery("SELECT c FROM Clase c WHERE c.id = :id", Clase.class);
          q.setParameter("id", id);
          return q.getResultStream().findFirst().map(ClaseMapper::toDetalleDTO);
        });
  }

  /**
   * Busca clases activas como DTOs de listado.
   *
   * @return Lista de ClaseListadoDTO solo con clases activas
   */
  public List<ClaseListadoDTO> findActivosListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Clase> q =
              em.createQuery(
                  "SELECT c FROM Clase c WHERE c.activa = true ORDER BY c.nombre", Clase.class);
          List<Clase> clases = q.getResultList();
          return ClaseMapper.toListadoDTOList(clases);
        });
  }
}
