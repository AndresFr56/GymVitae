package gym.vitae.controller;

import gym.vitae.model.Cliente;
import gym.vitae.model.enums.EstadoCliente;
import gym.vitae.repositories.ClienteRepository;
import java.time.LocalDate;
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

  /**
   * Obtiene todos los clientes.
   *
   * @return Lista de clientes.
   */
  public List<Cliente> getClientes() {
    return clienteRepository.findAll();
  }

  /**
   * Obtiene clientes con paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de clientes paginada.
   */
  public List<Cliente> getClientes(int offset, int limit) {
    validatePagination(offset, limit);
    return clienteRepository.findAll(offset, limit);
  }

  /**
   * Obtiene un cliente por ID.
   *
   * @param id ID del cliente.
   * @return Cliente encontrado.
   * @throws IllegalArgumentException si no se encuentra el cliente.
   */
  public Cliente getClienteById(int id) {
    validateId(id);
    return clienteRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente no encontrado con ID: " + id));
  }

  /**
   * Obtiene un cliente por código de cliente.
   *
   * @param codigoCliente Código del cliente.
   * @return Cliente encontrado.
   * @throws IllegalArgumentException si no se encuentra el cliente.
   */
  public Cliente getClienteByCodigoCliente(String codigoCliente) {
    if (codigoCliente == null || codigoCliente.trim().isEmpty()) {
      throw new IllegalArgumentException("El código de cliente es obligatorio");
    }
    return clienteRepository
        .findByCodigoCliente(codigoCliente.trim())
        .orElseThrow(
            () ->
                new IllegalArgumentException("Cliente no encontrado con código: " + codigoCliente));
  }

  /**
   * Crea un nuevo cliente.
   *
   * @param cliente Cliente a crear.
   * @return Cliente creado.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public Cliente createCliente(Cliente cliente) {
    validateCliente(cliente);
    validateCodigoClienteNoDuplicado(cliente.getCodigoCliente(), null);
    validateCedulaNoDuplicada(cliente.getCedula(), null);

    // Establecer estado inicial
    if (cliente.getEstado() == null) {
      cliente.setEstado(EstadoCliente.ACTIVO);
    }

    return clienteRepository.save(cliente);
  }

  /**
   * Actualiza un cliente existente.
   *
   * @param cliente Cliente con datos actualizados.
   * @return true si se actualizó correctamente.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public boolean updateCliente(Cliente cliente) {
    validateUpdateId(cliente);
    validateClienteExists(cliente.getId());
    validateCliente(cliente);
    validateCodigoClienteNoDuplicado(cliente.getCodigoCliente(), cliente.getId());
    validateCedulaNoDuplicada(cliente.getCedula(), cliente.getId());

    return clienteRepository.update(cliente);
  }

  /**
   * Elimina un cliente (soft delete - cambia estado a SUSPENDIDO).
   *
   * @param id ID del cliente a eliminar.
   * @throws IllegalArgumentException si el cliente no existe.
   */
  public void deleteCliente(int id) {
    validateId(id);
    validateClienteExists(id);
    clienteRepository.delete(id);
  }

  /**
   * Obtiene el total de clientes.
   *
   * @return Cantidad total de clientes.
   */
  public long countClientes() {
    return clienteRepository.count();
  }

  /**
   * Cambia el estado de un cliente.
   *
   * @param id ID del cliente.
   * @param nuevoEstado Nuevo estado.
   * @return true si se actualizó correctamente.
   */
  public boolean cambiarEstadoCliente(int id, EstadoCliente nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado no puede ser nulo");
    }
    Cliente cliente = getClienteById(id);
    cliente.setEstado(nuevoEstado);
    return clienteRepository.update(cliente);
  }

  /**
   * Valida los datos de un cliente.
   *
   * @param cliente Cliente a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateCliente(Cliente cliente) {
    if (cliente == null) {
      throw new IllegalArgumentException("El cliente no puede ser nulo");
    }

    validateClienteDatosBasicos(cliente);
    validateClienteDatosContacto(cliente);
    validateClienteDatosEmergencia(cliente);
  }

  /**
   * Valida los datos básicos del cliente.
   *
   * @param cliente Cliente a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateClienteDatosBasicos(Cliente cliente) {
    validateCodigoCliente(cliente.getCodigoCliente());
    validateNombres(cliente.getNombres());
    validateApellidos(cliente.getApellidos());
    validateCedula(cliente.getCedula());
    validateFechaNacimiento(cliente.getFechaNacimiento());
  }

  /**
   * Valida los datos de contacto del cliente.
   *
   * @param cliente Cliente a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateClienteDatosContacto(Cliente cliente) {
    validateTelefono(cliente.getTelefono());
    validateEmail(cliente.getEmail());
    validateDireccion(cliente.getDireccion());
  }

  /**
   * Valida los datos de contacto de emergencia.
   *
   * @param cliente Cliente a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateClienteDatosEmergencia(Cliente cliente) {
    validateContactoEmergencia(cliente.getContactoEmergencia());
    validateTelefonoEmergencia(cliente.getTelefonoEmergencia());
  }

  /**
   * Valida el código de cliente.
   *
   * @param codigoCliente Código a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateCodigoCliente(String codigoCliente) {
    if (codigoCliente == null || codigoCliente.trim().isEmpty()) {
      throw new IllegalArgumentException("El código de cliente es obligatorio");
    }
    if (codigoCliente.length() > 20) {
      throw new IllegalArgumentException("El código de cliente no puede exceder 20 caracteres");
    }
  }

  /**
   * Valida los nombres.
   *
   * @param nombres Nombres a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateNombres(String nombres) {
    if (nombres == null || nombres.trim().isEmpty()) {
      throw new IllegalArgumentException("Los nombres son obligatorios");
    }
    if (nombres.length() > 100) {
      throw new IllegalArgumentException("Los nombres no pueden exceder 100 caracteres");
    }
  }

  /**
   * Valida los apellidos.
   *
   * @param apellidos Apellidos a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateApellidos(String apellidos) {
    if (apellidos == null || apellidos.trim().isEmpty()) {
      throw new IllegalArgumentException("Los apellidos son obligatorios");
    }
    if (apellidos.length() > 100) {
      throw new IllegalArgumentException("Los apellidos no pueden exceder 100 caracteres");
    }
  }

  /**
   * Valida la cédula.
   *
   * @param cedula Cédula a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateCedula(String cedula) {
    if (cedula == null || cedula.trim().isEmpty()) {
      throw new IllegalArgumentException("La cédula es obligatoria");
    }
    if (!cedula.matches("\\d{10}")) {
      throw new IllegalArgumentException("La cédula debe tener exactamente 10 dígitos numéricos");
    }
  }

  /**
   * Valida el teléfono.
   *
   * @param telefono Teléfono a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateTelefono(String telefono) {
    if (telefono == null || telefono.trim().isEmpty()) {
      throw new IllegalArgumentException("El teléfono es obligatorio");
    }
    if (!telefono.matches("\\d{10}")) {
      throw new IllegalArgumentException("El teléfono debe tener exactamente 10 dígitos numéricos");
    }
  }

  /**
   * Valida el email.
   *
   * @param email Email a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateEmail(String email) {
    if (email != null && !email.trim().isEmpty() && !email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
      throw new IllegalArgumentException("El formato del email no es válido");
    }
    if (email != null && email.length() > 100) {
      throw new IllegalArgumentException("El email no puede exceder 100 caracteres");
    }
  }

  /**
   * Valida la dirección.
   *
   * @param direccion Dirección a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateDireccion(String direccion) {
    if (direccion != null && direccion.length() > 100) {
      throw new IllegalArgumentException("La dirección no puede exceder 100 caracteres");
    }
  }

  /**
   * Valida la fecha de nacimiento.
   *
   * @param fechaNacimiento Fecha a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateFechaNacimiento(LocalDate fechaNacimiento) {
    if (fechaNacimiento != null && fechaNacimiento.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de nacimiento no puede ser futura");
    }
    if (fechaNacimiento != null && fechaNacimiento.isBefore(LocalDate.now().minusYears(120))) {
      throw new IllegalArgumentException("La fecha de nacimiento no puede ser anterior a 120 años");
    }
  }

  /**
   * Valida el contacto de emergencia.
   *
   * @param contactoEmergencia Contacto a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateContactoEmergencia(String contactoEmergencia) {
    if (contactoEmergencia != null && contactoEmergencia.length() > 100) {
      throw new IllegalArgumentException(
          "El contacto de emergencia no puede exceder 100 caracteres");
    }
  }

  /**
   * Valida el teléfono de emergencia.
   *
   * @param telefonoEmergencia Teléfono a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateTelefonoEmergencia(String telefonoEmergencia) {
    if (telefonoEmergencia != null
        && !telefonoEmergencia.trim().isEmpty()
        && !telefonoEmergencia.matches("\\d{10}")) {
      throw new IllegalArgumentException(
          "El teléfono de emergencia debe tener exactamente 10 dígitos numéricos");
    }
  }

  /**
   * Valida que el código de cliente no esté duplicado.
   *
   * @param codigoCliente Código a validar.
   * @param idActual ID del cliente actual (null si es creación).
   * @throws IllegalArgumentException si el código ya existe.
   */
  private void validateCodigoClienteNoDuplicado(String codigoCliente, Integer idActual) {
    List<Cliente> clientesExistentes =
        clienteRepository.findAll().stream()
            .filter(
                c ->
                    c.getCodigoCliente().equalsIgnoreCase(codigoCliente.trim())
                        && (!c.getId().equals(idActual)))
            .toList();

    if (!clientesExistentes.isEmpty()) {
      throw new IllegalArgumentException("Ya existe un cliente con el código: " + codigoCliente);
    }
  }

  /**
   * Valida que la cédula no esté duplicada.
   *
   * @param cedula Cédula a validar.
   * @param idActual ID del cliente actual (null si es creación).
   * @throws IllegalArgumentException si la cédula ya existe.
   */
  private void validateCedulaNoDuplicada(String cedula, Integer idActual) {
    List<Cliente> clientesExistentes =
        clienteRepository.findAll().stream()
            .filter(
                c ->
                    c.getCedula().equals(cedula.trim())
                        && (!c.getId().equals(idActual)))
            .toList();

    if (!clientesExistentes.isEmpty()) {
      throw new IllegalArgumentException("Ya existe un cliente con la cédula: " + cedula);
    }
  }

  /**
   * Valida un ID.
   *
   * @param id ID a validar.
   * @throws IllegalArgumentException si el ID no es válido.
   */
  private void validateId(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
  }

  /**
   * Valida que un cliente existe.
   *
   * @param id ID del cliente.
   * @throws IllegalArgumentException si el cliente no existe.
   */
  private void validateClienteExists(int id) {
    if (!clienteRepository.existsById(id)) {
      throw new IllegalArgumentException("Cliente no encontrado con ID: " + id);
    }
  }

  /**
   * Valida el ID de un cliente para actualización.
   *
   * @param cliente Cliente a validar.
   * @throws IllegalArgumentException si el ID no es válido.
   */
  private void validateUpdateId(Cliente cliente) {
    if (cliente.getId() == null || cliente.getId() <= 0) {
      throw new IllegalArgumentException("ID de cliente inválido");
    }
  }

  /**
   * Valida los parámetros de paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @throws IllegalArgumentException si los parámetros no son válidos.
   */
  private void validatePagination(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("El offset no puede ser negativo");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("El limit debe ser mayor a 0");
    }
  }
}
