package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.MembresiaBeneficioMapper;
import gym.vitae.model.MembresiaBeneficio;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaBeneficioListadoDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record MembresiaBeneficioRepository(DBConnectionManager db)
    implements IRepository<MembresiaBeneficio> {

  public MembresiaBeneficioRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public MembresiaBeneficio save(MembresiaBeneficio entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(MembresiaBeneficio entity) {
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
            em.find(MembresiaBeneficio.class, id) != null
                ? em.createQuery("delete from MembresiaBeneficio mb where mb.id = :id")
                    .setParameter("id", id)
                    .executeUpdate()
                : 0);
  }

  @Override
  public Optional<MembresiaBeneficio> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(MembresiaBeneficio.class, id)));
  }

  @Override
  public List<MembresiaBeneficio> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
          return q.getResultList();
        });
  }

  @Override
  public List<MembresiaBeneficio> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
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
          TypedQuery<Long> q =
              em.createQuery("select count(mb) from MembresiaBeneficio mb", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> em.find(MembresiaBeneficio.class, id) != null);
  }

  // ==================== MÉTODOS CON DTOs ====================

  /**
   * Obtiene todas las asociaciones membresía-beneficio como DTOs de listado.
   *
   * @return Lista de MembresiaBeneficioListadoDTO
   */
  public List<MembresiaBeneficioListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
          return MembresiaBeneficioMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene asociaciones membresía-beneficio paginadas como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de MembresiaBeneficioListadoDTO
   */
  public List<MembresiaBeneficioListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery("from MembresiaBeneficio mb order by mb.id", MembresiaBeneficio.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return MembresiaBeneficioMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca una asociación membresía-beneficio por ID y retorna el DTO de detalle.
   *
   * @param id ID de la asociación
   * @return Optional de MembresiaBeneficioDetalleDTO
   */
  public Optional<MembresiaBeneficioDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          MembresiaBeneficio mb = em.find(MembresiaBeneficio.class, id);
          return Optional.ofNullable(MembresiaBeneficioMapper.toDetalleDTO(mb));
        });
  }

  /**
   * Obtiene los beneficios de un tipo de membresía específico como DTOs.
   *
   * @param tipoMembresiaId ID del tipo de membresía
   * @return Lista de MembresiaBeneficioListadoDTO
   */
  public List<MembresiaBeneficioListadoDTO> findByTipoMembresiaId(int tipoMembresiaId) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<MembresiaBeneficio> q =
              em.createQuery(
                  "from MembresiaBeneficio mb where mb.tipoMembresia.id = :tipoMembresiaId order by mb.id",
                  MembresiaBeneficio.class);
          q.setParameter("tipoMembresiaId", tipoMembresiaId);
          return MembresiaBeneficioMapper.toListadoDTOList(q.getResultList());
        });
  }
}
