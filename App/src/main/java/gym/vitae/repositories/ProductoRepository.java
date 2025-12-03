package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.ProductoMapper;
import gym.vitae.model.Producto;
import gym.vitae.model.dtos.inventario.ProductoDetalleDTO;
import gym.vitae.model.dtos.inventario.ProductoListadoDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record ProductoRepository(DBConnectionManager db) implements IRepository<Producto> {

  public ProductoRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Producto save(Producto entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Producto entity) {
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
            em.createQuery("update Producto p set p.activo = false where p.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Producto> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Producto.class, id)));
  }

  @Override
  public List<Producto> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Producto> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
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
          TypedQuery<Long> q = em.createQuery("select count(p) from Producto p", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Producto.class, id) != null);
  }

  /**
   * Obtiene todos los productos como DTOs de listado.
   *
   * @return Lista de ProductoListadoDTO
   */
  public List<ProductoListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
          return ProductoMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene productos paginados como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de ProductoListadoDTO
   */
  public List<ProductoListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Producto> q = em.createQuery("from Producto p order by p.id", Producto.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return ProductoMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca un producto por ID y retorna el DTO de detalle.
   *
   * @param id ID del producto
   * @return Optional de ProductoDetalleDTO
   */
  public Optional<ProductoDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          Producto producto = em.find(Producto.class, id);
          return Optional.ofNullable(ProductoMapper.toDetalleDTO(producto));
        });
  }

  /**
   * Obtiene todos los productos activos como DTOs de listado.
   *
   * @return Lista de ProductoListadoDTO activos
   */
  public List<ProductoListadoDTO> findActivosListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Producto> q =
              em.createQuery(
                  "SELECT p FROM Producto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.proveedor "
                      + "WHERE p.activo = true ORDER BY p.id",
                  Producto.class);
          return ProductoMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca productos con filtros opcionales y paginación.
   *
   * @param searchText Texto de búsqueda en nombre/código (puede ser null)
   * @param categoriaId ID de categoría para filtrar (puede ser null)
   * @param proveedorId ID de proveedor para filtrar (puede ser null)
   * @param activo Estado activo para filtrar (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de ProductoListadoDTO que coinciden con los filtros
   */
  public List<ProductoListadoDTO> findAllListadoWithFilters(
      String searchText,
      Integer categoriaId,
      Integer proveedorId,
      Boolean activo,
      int offset,
      int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql =
              new StringBuilder(
                  "SELECT p FROM Producto p LEFT JOIN FETCH p.categoria LEFT JOIN FETCH p.proveedor WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(p.nombre) LIKE :searchText OR LOWER(p.codigo) LIKE :searchText)");
          }
          if (categoriaId != null) {
            jpql.append(" AND p.categoria.id = :categoriaId");
          }
          if (proveedorId != null) {
            jpql.append(" AND p.proveedor.id = :proveedorId");
          }
          if (activo != null) {
            jpql.append(" AND p.activo = :activo");
          }

          jpql.append(" ORDER BY p.id");

          TypedQuery<Producto> query = em.createQuery(jpql.toString(), Producto.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }
          if (categoriaId != null) {
            query.setParameter("categoriaId", categoriaId);
          }
          if (proveedorId != null) {
            query.setParameter("proveedorId", proveedorId);
          }
          if (activo != null) {
            query.setParameter("activo", activo);
          }

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          return ProductoMapper.toListadoDTOList(query.getResultList());
        });
  }

  /**
   * Cuenta productos con filtros opcionales.
   *
   * @param searchText Texto de búsqueda en nombre/código (puede ser null)
   * @param categoriaId ID de categoría para filtrar (puede ser null)
   * @param proveedorId ID de proveedor para filtrar (puede ser null)
   * @param activo Estado activo para filtrar (puede ser null)
   * @return Cantidad de productos que coinciden con los filtros
   */
  public long countWithFilters(
      String searchText, Integer categoriaId, Integer proveedorId, Boolean activo) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT COUNT(p) FROM Producto p WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(p.nombre) LIKE :searchText OR LOWER(p.codigo) LIKE :searchText)");
          }
          if (categoriaId != null) {
            jpql.append(" AND p.categoria.id = :categoriaId");
          }
          if (proveedorId != null) {
            jpql.append(" AND p.proveedor.id = :proveedorId");
          }
          if (activo != null) {
            jpql.append(" AND p.activo = :activo");
          }

          TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }
          if (categoriaId != null) {
            query.setParameter("categoriaId", categoriaId);
          }
          if (proveedorId != null) {
            query.setParameter("proveedorId", proveedorId);
          }
          if (activo != null) {
            query.setParameter("activo", activo);
          }

          return query.getSingleResult();
        });
  }
}
