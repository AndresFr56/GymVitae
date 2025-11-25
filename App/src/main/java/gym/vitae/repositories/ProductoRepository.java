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
}
