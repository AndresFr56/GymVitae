package gym.vitae.repositories;

import gym.vitae.core.DBConnectionManager;
import gym.vitae.core.TransactionHandler;
import gym.vitae.mapper.ClienteMapper;
import gym.vitae.model.Cliente;
import gym.vitae.model.dtos.cliente.ClienteDetalleDTO;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.model.enums.EstadoCliente;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.TypedQuery;

public record ClienteRepository(DBConnectionManager db) implements IRepository<Cliente> {
  public ClienteRepository {
    if (db == null) {
      throw new IllegalArgumentException("DBConnectionManager cannot be null");
    }
  }

  @Override
  public Cliente save(Cliente entity) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          em.persist(entity);
          em.flush();
          return entity;
        });
  }

  @Override
  public boolean update(Cliente entity) {
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
            em.createQuery("update Cliente c set c.estado = :estado where c.id = :id")
                .setParameter("id", id)
                .setParameter("estado", EstadoCliente.SUSPENDIDO)
                .executeUpdate());
  }

  @Override
  public Optional<Cliente> findById(int id) {
    return TransactionHandler.inTransaction(
        db, em -> Optional.ofNullable(em.find(Cliente.class, id)));
  }

  @Override
  public List<Cliente> findAll() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
          return q.getResultList();
        });
  }

  @Override
  public List<Cliente> findAll(int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cliente> q = em.createQuery("from Cliente c order by c.id", Cliente.class);
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
          TypedQuery<Long> q = em.createQuery("select count(c) from Cliente c", Long.class);
          return q.getSingleResult();
        });
  }

  @Override
  public boolean existsById(int id) {
    return TransactionHandler.inTransaction(db, em -> em.find(Cliente.class, id) != null);
  }

  public Optional<Cliente> findByCodigoCliente(String codigo) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cliente> q =
              em.createQuery("from Cliente c where c.codigoCliente = :codigo", Cliente.class);
          q.setParameter("codigo", codigo);
          List<Cliente> res = q.getResultList();
          return res.isEmpty() ? Optional.empty() : Optional.of(res.getFirst());
        });
  }

  /**
   * Busca clientes con filtros opcionales y paginación.
   *
   * @param searchText Texto de búsqueda en nombres/apellidos/cédula (puede ser null)
   * @param estado Estado para filtrar (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de clientes que coinciden con los filtros
   */
  public List<Cliente> findAllWithFilters(String searchText, String estado, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT c FROM Cliente c WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(c.nombres) LIKE :searchText OR LOWER(c.apellidos) LIKE :searchText OR c.cedula LIKE :searchText)");
          }

          if (estado != null && !estado.isEmpty()) {
            jpql.append(" AND c.estado = :estado");
          }

          jpql.append(" ORDER BY c.nombres, c.apellidos");

          TypedQuery<Cliente> query = em.createQuery(jpql.toString(), Cliente.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (estado != null && !estado.isEmpty()) {
            query.setParameter("estado", gym.vitae.model.enums.EstadoCliente.valueOf(estado));
          }

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          return query.getResultList();
        });
  }

  /**
   * Cuenta clientes con filtros opcionales.
   *
   * @param searchText Texto de búsqueda en nombres/apellidos/cédula (puede ser null)
   * @param estado Estado para filtrar (puede ser null)
   * @return Cantidad de clientes que coinciden con los filtros
   */
  public long countWithFilters(String searchText, String estado) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT COUNT(c) FROM Cliente c WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(c.nombres) LIKE :searchText OR LOWER(c.apellidos) LIKE :searchText OR c.cedula LIKE :searchText)");
          }

          if (estado != null && !estado.isEmpty()) {
            jpql.append(" AND c.estado = :estado");
          }

          TypedQuery<Long> query = em.createQuery(jpql.toString(), Long.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (estado != null && !estado.isEmpty()) {
            query.setParameter("estado", gym.vitae.model.enums.EstadoCliente.valueOf(estado));
          }

          return query.getSingleResult();
        });
  }

  /**
   * Obtiene todos los clientes como DTOs de listado.
   *
   * @return Lista de ClienteListadoDTO
   */
  public List<ClienteListadoDTO> findAllListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cliente> q =
              em.createQuery("SELECT c FROM Cliente c ORDER BY c.id", Cliente.class);
          List<Cliente> clientes = q.getResultList();
          return ClienteMapper.toListadoDTOList(clientes);
        });
  }

  /**
   * Busca un cliente por ID y retorna el DTO de detalle.
   *
   * @param id ID del cliente
   * @return Optional de ClienteDetalleDTO
   */
  public Optional<ClienteDetalleDTO> findByIdDetalle(int id) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cliente> q =
              em.createQuery("SELECT c FROM Cliente c WHERE c.id = :id", Cliente.class);
          q.setParameter("id", id);

          return q.getResultStream().findFirst().map(ClienteMapper::toDetalleDTO);
        });
  }

  /**
   * Busca clientes activos como DTOs de listado.
   *
   * @return Lista de ClienteListadoDTO solo con clientes activos
   */
  public List<ClienteListadoDTO> findActivosListado() {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          TypedQuery<Cliente> q =
              em.createQuery(
                  "SELECT c FROM Cliente c WHERE c.estado = :estado ORDER BY c.id", Cliente.class);
          q.setParameter("estado", EstadoCliente.ACTIVO);
          List<Cliente> clientes = q.getResultList();
          return ClienteMapper.toListadoDTOList(clientes);
        });
  }

  /**
   * Busca clientes con filtros y retorna DTOs de listado.
   *
   * @param searchText Texto de búsqueda (puede ser null)
   * @param estado Estado para filtrar (puede ser null)
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de ClienteListadoDTO
   */
  public List<ClienteListadoDTO> findAllWithFiltersListado(
      String searchText, String estado, int offset, int limit) {
    return TransactionHandler.inTransaction(
        db,
        em -> {
          StringBuilder jpql = new StringBuilder("SELECT c FROM Cliente c WHERE 1=1");

          if (searchText != null && !searchText.trim().isEmpty()) {
            jpql.append(
                " AND (LOWER(c.nombres) LIKE :searchText OR LOWER(c.apellidos) LIKE :searchText OR c.cedula LIKE :searchText)");
          }

          if (estado != null && !estado.isEmpty()) {
            jpql.append(" AND c.estado = :estado");
          }

          jpql.append(" ORDER BY c.id");

          TypedQuery<Cliente> query = em.createQuery(jpql.toString(), Cliente.class);

          if (searchText != null && !searchText.trim().isEmpty()) {
            query.setParameter("searchText", "%" + searchText.toLowerCase() + "%");
          }

          if (estado != null && !estado.isEmpty()) {
            query.setParameter("estado", EstadoCliente.valueOf(estado));
          }

          query.setFirstResult(offset);
          query.setMaxResults(limit);

          List<Cliente> clientes = query.getResultList();
          return ClienteMapper.toListadoDTOList(clientes);
        });
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (ClienteRepository) obj;
    return Objects.equals(this.db, that.db);
  }

  @Override
  public String toString() {
    return "ClienteRepository[" + "db=" + db + ']';
  }
}
