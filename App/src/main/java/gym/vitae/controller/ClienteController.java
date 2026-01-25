package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.ClienteMapper;
import gym.vitae.model.Cliente;
import gym.vitae.model.dtos.cliente.ClienteCreateDTO;
import gym.vitae.model.dtos.cliente.ClienteDetalleDTO;
import gym.vitae.model.dtos.cliente.ClienteListadoDTO;
import gym.vitae.model.dtos.cliente.ClienteUpdateDTO;
import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.repositories.ClienteRepository;
import java.util.List;

/** Controlador de Clientes. */
public class ClienteController extends BaseController {

  private final ClienteRepository clienteRepository;

  public ClienteController() {
    super();
    this.clienteRepository = getRepository(ClienteRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param clienteRepository Repositorio de clientes.
   */
  ClienteController(ClienteRepository clienteRepository) {
    super(null);
    this.clienteRepository = clienteRepository;
  }

  public List<ClienteListadoDTO> getClientes() {
    return clienteRepository.findAllListado();
  }

  public List<ClienteListadoDTO> getClientesActivos() {
    return clienteRepository.findActivosListado();
  }

  public ClienteDetalleDTO getClienteById(int id) {
    validateId(id);
    return clienteRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
  }

  public List<ClienteListadoDTO> getClientesWithFilters(
      String searchText, String estado, int offset, int limit) {
    validatePagination(offset, limit);
    return clienteRepository.findAllWithFiltersListado(searchText, estado, offset, limit);
  }

  public long countClientesWithFilters(String searchText, String estado) {
    return clienteRepository.countWithFilters(searchText, estado);
  }

  /**
   * Crea un nuevo cliente.
   *
   * @param dto DTO con datos del cliente a crear.
   * @return ClienteDetalleDTO del cliente creado.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public ClienteDetalleDTO createCliente(ClienteCreateDTO dto) {
    validateClienteCreate(dto);
    validateCedulaNoDuplicada(dto.cedula(), null);
    validateNombresApellidosNoDuplicados(dto.nombres(), dto.apellidos(), null);
    validateEmailNoDuplicado(dto.email(), null);

    // Generar código
    String codigoCliente = generateCodigoCliente();

    // Mapear y guardar
    Cliente cliente = ClienteMapper.toEntity(dto, codigoCliente);
    Cliente saved = clienteRepository.save(cliente);

    // Retornar DTO detalle
    return clienteRepository
        .findByIdDetalle(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar cliente creado"));
  }

  /**
   * Actualiza un cliente existente.
   *
   * @param id ID del cliente a actualizar.
   * @param dto DTO con datos actualizados.
   * @return ClienteDetalleDTO del cliente actualizado.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public ClienteDetalleDTO updateCliente(int id, ClienteUpdateDTO dto) {
    validateId(id);
    validateClienteUpdate(dto);

    // Cargar cliente existente
    Cliente cliente =
        clienteRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

    validateCedulaNoDuplicada(dto.cedula(), id);
    validateNombresApellidosNoDuplicados(dto.nombres(), dto.apellidos(), id);
    validateEmailNoDuplicado(dto.email(), id);

    // Actualizar con mapper
    ClienteMapper.updateEntity(cliente, dto);
    clienteRepository.update(cliente);

    // Retornar DTO actualizado
    return clienteRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar cliente actualizado"));
  }

  public void deleteCliente(int id) {
    validateId(id);
    if (!clienteRepository.existsById(id)) {
      throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
    }
    clienteRepository.delete(id);
  }

  public ClienteDetalleDTO cambiarEstadoCliente(int id, EstadoCliente nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado no puede ser nulo");
    }
    validateId(id);

    Cliente cliente =
        clienteRepository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));

    cliente.setEstado(nuevoEstado);
    clienteRepository.update(cliente);

    return clienteRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar cliente actualizado"));
  }

  private String generateCodigoCliente() {
    int year = java.time.Year.now().getValue();
    int nextSecuencial = clienteRepository.getNextCodigoSecuencial(year);
    return String.format("CLI-%d%05d", year, nextSecuencial);
  }

  /** Obtiene un código de cliente generado automáticamente. */
  public String getCodigoClienteGenerado() {
    return generateCodigoCliente();
  }

  private void validateClienteCreate(ClienteCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del cliente no pueden ser nulos");
    }

    validateNombres(dto.nombres());
    validateApellidos(dto.apellidos());
    validateCedula(dto.cedula());
    validateTelefono(dto.telefono());
    validateEmailOpcional(dto.email());
    validateDireccion(dto.direccion());
    validateFechaNacimiento(dto.fechaNacimiento());
    validateOptionalString(dto.contactoEmergencia(), "El contacto de emergencia", 100);
    validateTelefonoOpcional(dto.telefonoEmergencia(), "El teléfono de emergencia");

    if (dto.genero() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
  }

  private void validateClienteUpdate(ClienteUpdateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del cliente no pueden ser nulos");
    }

    validateNombres(dto.nombres());
    validateApellidos(dto.apellidos());
    validateCedula(dto.cedula());
    validateTelefono(dto.telefono());
    validateEmailOpcional(dto.email());
    validateDireccion(dto.direccion());
    validateFechaNacimiento(dto.fechaNacimiento());
    validateOptionalString(dto.contactoEmergencia(), "El contacto de emergencia", 100);
    validateTelefonoOpcional(dto.telefonoEmergencia(), "El teléfono de emergencia");

    if (dto.genero() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
    if (dto.estado() == null) {
      throw new IllegalArgumentException("El estado es obligatorio");
    }
  }

  private void validateCedulaNoDuplicada(String cedula, Integer idActual) {
    if (clienteRepository.existsByCedula(cedula, idActual)) {
      throw new IllegalArgumentException(
          "Cédula existente en otro Cliente, verifique o ingrese otro valor");
    }
  }

  /**
   * Valida que los nombres y apellidos no estén duplicados.
   *
   * @param nombres Nombres a validar
   * @param apellidos Apellidos a validar
   * @param idActual ID del cliente actual (null si es creación)
   */
  private void validateNombresApellidosNoDuplicados(
      String nombres, String apellidos, Integer idActual) {
    if (clienteRepository.existsByNombresApellidos(nombres, apellidos, idActual)) {
      throw new IllegalArgumentException(
          "Nombres y apellidos existentes en otro Cliente, verifique o ingrese otros valores");
    }
  }

  /**
   * Valida que el email no esté duplicado.
   *
   * @param email Email a validar
   * @param idActual ID del cliente actual (null si es creación)
   */
  private void validateEmailNoDuplicado(String email, Integer idActual) {
    if (clienteRepository.existsByEmail(email, idActual)) {
      throw new IllegalArgumentException(
          "Email existente en otro Cliente, verifique o ingrese otro valor");
    }
  }
}
