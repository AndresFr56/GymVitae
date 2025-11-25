package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.BeneficioMapper;
import gym.vitae.model.Beneficio;
import gym.vitae.model.dtos.membresias.BeneficioDetalleDTO;
import gym.vitae.model.dtos.membresias.BeneficioListadoDTO;
import java.util.List;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record BeneficioRepository(DBConnectionManager db) implements IRepository<Beneficio> {

  public BeneficioRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Beneficio save(Beneficio entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Beneficio entity) {
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
            em.createQuery("update Beneficio c set c.activo = false where c.id = :id")
                .setParameter("id", id)
                .executeUpdate());
  }

  @Override
  public Optional<Beneficio> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Beneficio.class, id)));
  }

  @Override
  public List<Beneficio> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Beneficio> q =
              em.createQuery("from Beneficio b order by b.id", Beneficio.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Beneficio> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Beneficio> q =
              em.createQuery("from Beneficio b order by b.id", Beneficio.class);
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
          TypedQuery<Long> q = em.createQuery("select count(b) from Beneficio b", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Beneficio.class, id) != null);
  }

  // ==================== MÉTODOS CON DTOs ====================

  /**
   * Obtiene todos los beneficios como DTOs de listado.
   *
   * @return Lista de BeneficioListadoDTO
   */
  public List<BeneficioListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Beneficio> q =
              em.createQuery("from Beneficio b order by b.id", Beneficio.class);
          return BeneficioMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Obtiene beneficios paginados como DTOs de listado.
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de BeneficioListadoDTO
   */
  public List<BeneficioListadoDTO> findAllListadoPaginated(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Beneficio> q =
              em.createQuery("from Beneficio b order by b.id", Beneficio.class);
          q.setFirstResult(offset);
          q.setMaxResults(limit);
          return BeneficioMapper.toListadoDTOList(q.getResultList());
        });
  }

  /**
   * Busca un beneficio por ID y retorna el DTO de detalle.
   *
   * @param id ID del beneficio
   * @return Optional de BeneficioDetalleDTO
   */
  public Optional<BeneficioDetalleDTO> findDetalleById(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          Beneficio beneficio = em.find(Beneficio.class, id);
          return Optional.ofNullable(BeneficioMapper.toDetalleDTO(beneficio));
        });
  }
}
