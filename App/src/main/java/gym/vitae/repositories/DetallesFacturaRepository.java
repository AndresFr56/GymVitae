package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.DetalleFacturaMapper;
import gym.vitae.model.DetallesFactura;
import gym.vitae.model.dtos.facturacion.DetalleFacturaDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record DetallesFacturaRepository(DBConnectionManager db)
    implements IRepository<DetallesFactura> {

  public DetallesFacturaRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public DetallesFactura save(DetallesFactura entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(DetallesFactura entity) {
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
            em.createQuery("delete from DetallesFactura d where d.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<DetallesFactura> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(DetallesFactura.class, id)));
  }

  @Override
  public List<DetallesFactura> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<DetallesFactura> q =
              em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
          return q.getResultList();
        });
  }

  @Override
  public List<DetallesFactura> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<DetallesFactura> q =
              em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
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
          TypedQuery<Long> q = em.createQuery("select count(d) from DetallesFactura d", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(DetallesFactura.class, id) != null);
  }

  // ==================== MÉTODOS CON DTOs ====================

  /**
   * Obtiene todos los detalles de factura como DTOs.
   *
   * @return Lista de DetalleFacturaDTO
   */
  public List<DetalleFacturaDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<DetallesFactura> q =
              em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
          return DetalleFacturaMapper.toDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene detalles de factura paginados como DTOs.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de DetalleFacturaDTO
   */
  public List<DetalleFacturaDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<DetallesFactura> q =
              em.createQuery("from DetallesFactura d order by d.id", DetallesFactura.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return DetalleFacturaMapper.toDTOList(q.getResultList());
        });
  }

  /**
   * Busca un detalle de factura por ID y retorna el DTO.
   *
   * @param id ID del detalle
   * @return Optional de DetalleFacturaDTO
   */
  public Optional<DetalleFacturaDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          DetallesFactura detalle = em.find(DetallesFactura.class, id);
          return Optional.ofNullable(DetalleFacturaMapper.toDTO(detalle));
        });
  }

  /**
   * Obtiene los detalles de una factura específica como DTOs.
   *
   * @param facturaId ID de la factura
   * @return Lista de DetalleFacturaDTO
   */
  public List<DetalleFacturaDTO> findByFacturaId(int facturaId) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<DetallesFactura> q =
              em.createQuery(
                  "from DetallesFactura d where d.factura.id = :facturaId order by d.id",
                  DetallesFactura.class);
          q.setParameter("facturaId", facturaId);
          return DetalleFacturaMapper.toDTOList(q.getResultList());
        });
  }
}
