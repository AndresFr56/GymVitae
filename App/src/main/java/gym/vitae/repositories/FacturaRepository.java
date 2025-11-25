package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.FacturaMapper;
import gym.vitae.model.Factura;
import gym.vitae.model.dtos.facturacion.FacturaDetalleDTO;
import gym.vitae.model.dtos.facturacion.FacturaListadoDTO;
import gym.vitae.model.enums.EstadoFactura;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record FacturaRepository(DBConnectionManager db) implements IRepository<Factura> {

  public FacturaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Factura save(Factura entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Factura entity) {
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
            em.createQuery("update Factura f set f.estado = :estado where f.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoFactura.ANULADA)
                .executeUpdate());
  }

  @Override
  public Optional<Factura> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Factura.class, id)));
  }

  @Override
  public List<Factura> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Factura> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
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
          TypedQuery<Long> q = em.createQuery("select count(f) from Factura f", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Factura.class, id) != null);
  }

  /**
   * Obtiene todas las facturas como DTOs de listado.
   *
   * @return Lista de FacturaListadoDTO
   */
  public List<FacturaListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
          return FacturaMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene facturas paginadas como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de FacturaListadoDTO
   */
  public List<FacturaListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Factura> q = em.createQuery("from Factura f order by f.id", Factura.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return FacturaMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca una factura por ID y retorna el DTO de detalle.
   *
   * @param id ID de la factura
   * @return Optional de FacturaDetalleDTO
   */
  public Optional<FacturaDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          Factura factura = em.find(Factura.class, id);
          return Optional.ofNullable(FacturaMapper.toDetalleDTO(factura));
        });
  }
}
