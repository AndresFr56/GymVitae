package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.ProveedorMapper;
import gym.vitae.model.Proveedore;
import gym.vitae.model.dtos.inventario.ProveedorDetalleDTO;
import gym.vitae.model.dtos.inventario.ProveedorListadoDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

/**
 * Repositorio encargado de gestionar el CRUD de proveedores.
 *
 * @param db manejador de conexión con la bd
 */
public record ProveedoreRepository(DBConnectionManager db) implements IRepository<Proveedore> {

  /**
   * Crea una instancia del controlador validando la conexión con la bd.
   *
   * @param db manejador de conexión con la bd
   */
  public ProveedoreRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  /**
   * Guarda un nuevo proveedor en la bd.
   *
   * @param entity proveedor a guardar
   * @return entidad persistida
   */
  @Override
  public Proveedore save(Proveedore entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  /**
   * Actualiza un proveedor existente.
   *
   * @param entity proveedor con los datos actualizados
   * @return true si la operación fue exitosa
   */
  @Override
  public boolean update(Proveedore entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.merge(entity);
          em.flush();
          return true;
        });
  }

  /**
   * Eliminación lógica del proveedor, cambia estado a inactivo.
   *
   * @param id del proveedor
   */
  @Override
  public void delete(int id) {
    TransactionHandler.inTransaction(
        db,
        em ->
            em.createQuery("update Proveedore p set p.activo = false where p.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  /**
   * Busca proveedor por su id.
   *
   * @param id del proveedor
   * @return proveedor si lo encontró, sino vacío
   */
  @Override
  public Optional<Proveedore> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Proveedore.class, id)));
  }

  /**
   * Obtiene todos los proveedores registrados.
   *
   * @return lista de proveedores
   */
  @Override
  public List<Proveedore> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery("from Proveedore p order by p.id", Proveedore.class);
          return q.getResultList();
        });
  }

  /**
   * Obtiene proveedores con paginación.
   *
   * @param offset posición inicial
   * @param limit cantidad de registros
   * @return lista de proveedores
   */
  @Override
  public List<Proveedore> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery("from Proveedore p order by p.id", Proveedore.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return q.getResultList();
        });
  }

  /**
   * Cuenta el total de proveedores registrados.
   *
   * @return cantidad total de registros
   */
  @Override
  public long count() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Long> q = em.createQuery("select count(p) from Proveedore p", Long.class);
          return q.getSingleResult();
        });
  }

  /**
   * Verifica que un proveedor existe por su id.
   *
   * @param id del proveedor
   * @return true si existe
   */
  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Proveedore.class, id) != null);
  }

  /**
   * Obtiene todos los proveedores como DTOs de listado.
   *
   * @return lista de ProveedorListadoDTO
   */
  public List<ProveedorListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery("from Proveedore p order by p.id", Proveedore.class);
          return ProveedorMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene proveedores paginados como DTOs de listado.
   *
   * @param offset posición inicial
   * @param limit cantidad de registros
   * @return lista de ProveedorListadoDTO
   */
  public List<ProveedorListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery("from Proveedore p order by p.id", Proveedore.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return ProveedorMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca un proveedor por ID y retorna el DTO de detalle.
   *
   * @param id ID del proveedor
   * @return Optional de ProveedorDetalleDTO
   */
  public Optional<ProveedorDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          Proveedore proveedor = em.find(Proveedore.class, id);
          return Optional.ofNullable(ProveedorMapper.toDetalleDTO(proveedor));
        });
  }

  /**
   * Filtro proveedores con estado activo true.
   *
   * @return listado de proveedores con estado activo
   */
  public List<ProveedorListadoDTO> findProveedoresActivos() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Proveedore> q =
              em.createQuery(
                  "SELECT p from Proveedore p where p.activo = :activo ORDER BY p.id",
                  Proveedore.class);
          q.setParameter("activo", true);
          List<Proveedore> proveedores = q.getResultList();
          return ProveedorMapper.toListadoDTOList(proveedores);
        });
  }

  /**
   * Filtrar por nombre o correo del proveedor.
   *
   * @param searchText nombre, correo o codigo
   * @param estado true (activo) o false (inactivo)
   * @param offset posición inicial
   * @param limit cantidad de registros
   * @return lista filtrada de proveedores
   */
  public List<ProveedorListadoDTO> findAllWithFiltersListado(
      String searchText, Boolean estado, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT p FROM Proveedore p WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(p.nombre) LIKE :searchText OR LOWER(p.email) LIKE :searchText)");
          }

          if (estado != null) {
            jpql.append(" AND p.activo = :estado");
          }

          jpql.append(" ORDER BY p.id");

          TypedQuery<Proveedore> query = em.createQuery(jpql.toString(), Proveedore.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (estado != null) {
            query.setParameter("estado", estado);
          }

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          List<Proveedore> proveedores = query.getResultList();
          return ProveedorMapper.toListadoDTOList(proveedores);
        });
  }

  /**
   * Cuenta proveedores con filtros opcionales.
   *
   * @param searchText Texto de búsqueda en nombre/email (puede ser null)
   * @param estado Estado activo para filtrar (puede ser null)
   * @return Cantidad de proveedores que coinciden con los filtros
   */
  public long countWithFilters(String searchText, Boolean estado) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT COUNT(p) FROM Proveedore p WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(p.nombre) LIKE :searchText OR LOWER(p.email) LIKE :searchText)");
          }

          if (estado != null) {
            jpql.append(" AND p.activo = :estado");
          }

          TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (estado != null) {
            query.setParameter("estado", estado);
          }

          return query.getSingleResult();
        });
  }
}
