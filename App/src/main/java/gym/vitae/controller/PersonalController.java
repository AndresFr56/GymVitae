package gym.vitae.controller;

import gym.vitae.model.Cargo;
import gym.vitae.model.Empleado;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.repositories.CargoRepository;
import gym.vitae.repositories.EmpleadoRepository;
import java.time.LocalDate;
import java.util.List;

public class PersonalController extends BaseController {

  private final EmpleadoRepository empleadoRepository;
  private final CargoRepository cargoRepository;

  public PersonalController() {
    super();
    this.empleadoRepository = getRepository(EmpleadoRepository.class);
    this.cargoRepository = getRepository(CargoRepository.class);
  }

  PersonalController(EmpleadoRepository empleadoRepository, CargoRepository cargoRepository) {
    super(null);
    this.empleadoRepository = empleadoRepository;
    this.cargoRepository = cargoRepository;
  }

  /**
   * Obtiene todos los empleados
   *
   * @return Lista de empleados
   */
  public List<Empleado> getEmpleados() {
    return empleadoRepository.findAll();
  }

  /**
   * Obtiene empleados con paginación
   *
   * @param offset Posición inicial
   * @param limit Cantidad de registros
   * @return Lista de empleados paginada
   */
  public List<Empleado> getEmpleados(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("El offset no puede ser negativo");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("El limit debe ser mayor a 0");
    }
    return empleadoRepository.findAll(offset, limit);
  }

  /**
   * Obtiene un empleado por ID
   *
   * @param id ID del empleado
   * @return Empleado encontrado
   * @throws IllegalArgumentException si no se encuentra el empleado
   */
  public Empleado getEmpleadoById(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
    return empleadoRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));
  }

  /**
   * Crea un nuevo empleado
   *
   * @param empleado Empleado a crear
   * @return Empleado creado
   * @throws IllegalArgumentException si las validaciones fallan
   */
  public Empleado createEmpleado(Empleado empleado) {
    validateEmpleado(empleado);

    // Validar que el cargo exista y esté activo
    if (empleado.getCargo() == null || empleado.getCargo().getId() == null) {
      throw new IllegalArgumentException("El cargo es obligatorio");
    }
    Cargo cargo =
        cargoRepository
            .findById(empleado.getCargo().getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Cargo no encontrado con ID: " + empleado.getCargo().getId()));

    empleado.setCargo(cargo);

    // Establecer estado inicial si no se proporciona
    if (empleado.getEstado() == null) {
      empleado.setEstado(EstadoEmpleado.ACTIVO);
    }

    return empleadoRepository.save(empleado);
  }

  /**
   * Actualiza un empleado existente
   *
   * @param empleado Empleado con datos actualizados
   * @return true si se actualizó correctamente
   * @throws IllegalArgumentException si las validaciones fallan
   */
  public boolean updateEmpleado(Empleado empleado) {
    if (empleado.getId() == null || empleado.getId() <= 0) {
      throw new IllegalArgumentException("ID de empleado inválido");
    }

    // Verificar que existe
    if (!empleadoRepository.existsById(empleado.getId())) {
      throw new IllegalArgumentException("Empleado no encontrado con ID: " + empleado.getId());
    }

    validateEmpleado(empleado);

    // Validar que el cargo exista y esté activo
    if (empleado.getCargo() != null && empleado.getCargo().getId() != null) {
      Cargo cargo =
          cargoRepository
              .findById(empleado.getCargo().getId())
              .orElseThrow(
                  () ->
                      new IllegalArgumentException(
                          "Cargo no encontrado con ID: " + empleado.getCargo().getId()));

      empleado.setCargo(cargo);
    }

    return empleadoRepository.update(empleado);
  }

  /**
   * Elimina un empleado (soft delete - cambia estado a INACTIVO)
   *
   * @param id ID del empleado a eliminar
   * @throws IllegalArgumentException si el empleado no existe
   */
  public void deleteEmpleado(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
    if (!empleadoRepository.existsById(id)) {
      throw new IllegalArgumentException("Empleado no encontrado con ID: " + id);
    }
    empleadoRepository.delete(id);
  }

  /**
   * Obtiene el total de empleados
   *
   * @return Cantidad total de empleados
   */
  public long countEmpleados() {
    return empleadoRepository.count();
  }

  /**
   * Cambia el estado de un empleado
   *
   * @param id ID del empleado
   * @param nuevoEstado Nuevo estado
   * @return true si se actualizó correctamente
   */
  public boolean cambiarEstadoEmpleado(int id, EstadoEmpleado nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado no puede ser nulo");
    }
    Empleado empleado = getEmpleadoById(id);
    empleado.setEstado(nuevoEstado);
    return empleadoRepository.update(empleado);
  }

  /**
   * Valida los datos de un empleado
   *
   * @param empleado Empleado a validar
   * @throws IllegalArgumentException si alguna validación falla
   */
  private void validateEmpleado(Empleado empleado) {
    if (empleado == null) {
      throw new IllegalArgumentException("El empleado no puede ser nulo");
    }

    validateEmpleadoDatosBasicos(empleado);
    validateEmpleadoDatosContacto(empleado);
    validateEmpleadoFechas(empleado);
    validateEmpleadoContrato(empleado);
  }

  /**
   * Valida los datos básicos del empleado
   *
   * @param empleado Empleado a validar
   * @throws IllegalArgumentException si alguna validación falla
   */
  private void validateEmpleadoDatosBasicos(Empleado empleado) {
    validateCodigoEmpleado(empleado.getCodigoEmpleado());
    validateNombres(empleado.getNombres());
    validateApellidos(empleado.getApellidos());
    validateCedula(empleado.getCedula());

    if (empleado.getGenero() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
  }

  /**
   * Valida los datos de contacto del empleado
   *
   * @param empleado Empleado a validar
   * @throws IllegalArgumentException si alguna validación falla
   */
  private void validateEmpleadoDatosContacto(Empleado empleado) {
    validateTelefono(empleado.getTelefono());
    validateEmail(empleado.getEmail());
    validateDireccion(empleado.getDireccion());
  }

  /**
   * Valida las fechas del empleado
   *
   * @param empleado Empleado a validar
   * @throws IllegalArgumentException si alguna validación falla
   */
  private void validateEmpleadoFechas(Empleado empleado) {
    if (empleado.getFechaIngreso() == null) {
      throw new IllegalArgumentException("La fecha de ingreso es obligatoria");
    }
    if (empleado.getFechaIngreso().isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("La fecha de ingreso no puede ser futura");
    }

    if (empleado.getFechaSalida() != null
        && empleado.getFechaSalida().isBefore(empleado.getFechaIngreso())) {
      throw new IllegalArgumentException(
          "La fecha de salida no puede ser anterior a la fecha de ingreso");
    }
  }

  /**
   * Valida los datos del contrato del empleado
   *
   * @param empleado Empleado a validar
   * @throws IllegalArgumentException si alguna validación falla
   */
  private void validateEmpleadoContrato(Empleado empleado) {
    if (empleado.getTipoContrato() == null) {
      throw new IllegalArgumentException("El tipo de contrato es obligatorio");
    }
  }

  /**
   * Valida el código de empleado
   *
   * @param codigoEmpleado Código a validar
   * @throws IllegalArgumentException si la validación falla
   */
  private void validateCodigoEmpleado(String codigoEmpleado) {
    if (codigoEmpleado == null || codigoEmpleado.trim().isEmpty()) {
      throw new IllegalArgumentException("El código de empleado es obligatorio");
    }
    if (codigoEmpleado.length() > 20) {
      throw new IllegalArgumentException("El código de empleado no puede exceder 20 caracteres");
    }
  }

  /**
   * Valida los nombres
   *
   * @param nombres Nombres a validar
   * @throws IllegalArgumentException si la validación falla
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
   * Valida los apellidos
   *
   * @param apellidos Apellidos a validar
   * @throws IllegalArgumentException si la validación falla
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
   * Valida la cédula
   *
   * @param cedula Cédula a validar
   * @throws IllegalArgumentException si la validación falla
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
   * Valida el teléfono
   *
   * @param telefono Teléfono a validar
   * @throws IllegalArgumentException si la validación falla
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
   * Valida el email
   *
   * @param email Email a validar
   * @throws IllegalArgumentException si la validación falla
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
   * Valida la dirección
   *
   * @param direccion Dirección a validar
   * @throws IllegalArgumentException si la validación falla
   */
  private void validateDireccion(String direccion) {
    if (direccion != null && direccion.length() > 100) {
      throw new IllegalArgumentException("La dirección no puede exceder 100 caracteres");
    }
  }
}
