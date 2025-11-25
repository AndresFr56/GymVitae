package gym.vitae.controller;

import gym.vitae.core.SessionManager;
import gym.vitae.model.Empleado;
import gym.vitae.model.dtos.empleado.EmpleadoAuthDTO;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.repositories.EmpleadoRepository;

/** Controlador de Autenticación. */
public class AuthController extends BaseController {

  private static final String CARGO_ADMINISTRACION = "Administración";
  private static final String CARGO_RECEPCIONISTA = "Recepcionista";

  private final EmpleadoRepository empleadoRepository;
  private final SessionManager sessionManager;

  public AuthController() {
    super();
    this.empleadoRepository = getRepository(EmpleadoRepository.class);
    this.sessionManager = SessionManager.getInstance();
  }

  /**
   * Constructor para pruebas.
   *
   * @param empleadoRepository Repositorio de empleados.
   * @param sessionManager Gestor de sesión.
   */
  AuthController(EmpleadoRepository empleadoRepository, SessionManager sessionManager) {
    super(null);
    this.empleadoRepository = empleadoRepository;
    this.sessionManager = sessionManager;
  }

  /**
   * Autentica a un empleado usando su correo o nombre de usuario y cédula como contraseña.
   *
   * @param emailOrUsername Correo electrónico o nombre completo del empleado.
   * @param cedula Cédula del empleado (usada como contraseña).
   * @return true si la autenticación fue exitosa.
   * @throws IllegalArgumentException si las credenciales son inválidas o el empleado no tiene
   *     permisos.
   */
  public boolean login(String emailOrUsername, String cedula) {
    validateLoginCredentials(emailOrUsername, cedula);

    EmpleadoAuthDTO empleadoDTO =
        empleadoRepository
            .findByEmailOrUsernameForAuth(emailOrUsername)
            .orElseThrow(() -> new IllegalArgumentException("Credenciales inválidas"));

    validateCedula(empleadoDTO, cedula);
    validateEmpleadoActivo(empleadoDTO);
    validateCargo(empleadoDTO);

    // Cargar entidad completa para sesión (SessionManager aún usa Entity)
    Empleado empleado =
        empleadoRepository
            .findById(empleadoDTO.id())
            .orElseThrow(() -> new IllegalStateException("Error al cargar empleado"));

    sessionManager.setEmpleadoActual(empleado);
    return true;
  }

  /** Cierra la sesión del empleado actual. */
  public void logout() {
    sessionManager.logout();
  }

  /**
   * Obtiene el empleado actualmente autenticado.
   *
   * @return Empleado autenticado o null si no hay sesión activa.
   */
  public Empleado getEmpleadoActual() {
    return sessionManager.getEmpleadoActual();
  }

  /**
   * Verifica si hay un empleado autenticado.
   *
   * @return true si hay una sesión activa.
   */
  public boolean isAuthenticated() {
    return sessionManager.isAuthenticated();
  }

  /**
   * Valida las credenciales de inicio de sesión.
   *
   * @param emailOrUsername Correo o nombre de usuario.
   * @param cedula Cédula.
   * @throws IllegalArgumentException si las credenciales están vacías.
   */
  private void validateLoginCredentials(String emailOrUsername, String cedula) {
    if (emailOrUsername == null || emailOrUsername.trim().isEmpty()) {
      throw new IllegalArgumentException("El correo o nombre de usuario es obligatorio");
    }
    if (cedula == null || cedula.trim().isEmpty()) {
      throw new IllegalArgumentException("La cédula es obligatoria");
    }
  }

  /**
   * Valida que la cédula coincida con la del empleado.
   *
   * @param empleadoDTO DTO del empleado a validar.
   * @param cedula Cédula proporcionada.
   * @throws IllegalArgumentException si la cédula no coincide.
   */
  private void validateCedula(EmpleadoAuthDTO empleadoDTO, String cedula) {
    if (!empleadoDTO.cedula().equals(cedula.trim())) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }
  }

  /**
   * Valida que el empleado esté activo.
   *
   * @param empleadoDTO DTO del empleado a validar.
   * @throws IllegalArgumentException si el empleado no está activo.
   */
  private void validateEmpleadoActivo(EmpleadoAuthDTO empleadoDTO) {
    if (empleadoDTO.estado() != EstadoEmpleado.ACTIVO) {
      throw new IllegalArgumentException(
          "El empleado no está activo. Estado actual: " + empleadoDTO.estado());
    }
  }

  /**
   * Valida que el empleado tenga un cargo autorizado (Recepcionista o Administración).
   *
   * @param empleadoDTO DTO del empleado a validar.
   * @throws IllegalArgumentException si el cargo no está autorizado.
   */
  private void validateCargo(EmpleadoAuthDTO empleadoDTO) {
    if (empleadoDTO.cargo() == null) {
      throw new IllegalArgumentException("El empleado no tiene un cargo asignado");
    }

    String nombreCargo = empleadoDTO.cargo().nombre();
    if (!CARGO_ADMINISTRACION.equalsIgnoreCase(nombreCargo)
        && !CARGO_RECEPCIONISTA.equalsIgnoreCase(nombreCargo)) {
      throw new IllegalArgumentException(
          "No tiene permisos para acceder al sistema. Solo personal de Administración o Recepción puede iniciar sesión");
    }
  }
}
