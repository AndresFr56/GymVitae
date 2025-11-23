package gym.vitae.controller;

import gym.vitae.core.SessionManager;
import gym.vitae.model.Empleado;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.repositories.EmpleadoRepository;
import java.util.List;

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

    Empleado empleado = findEmpleadoByEmailOrUsername(emailOrUsername);

    if (empleado == null) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }

    validateCedula(empleado, cedula);
    validateEmpleadoActivo(empleado);
    validateCargo(empleado);

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
   * Busca un empleado por correo electrónico o nombre completo.
   *
   * @param emailOrUsername Correo o nombre completo.
   * @return Empleado encontrado o null.
   */
  private Empleado findEmpleadoByEmailOrUsername(String emailOrUsername) {
    String searchTerm = emailOrUsername.trim();
    List<Empleado> empleados = empleadoRepository.findAll();

    return empleados.stream()
        .filter(
            emp ->
                (emp.getEmail() != null && emp.getEmail().equalsIgnoreCase(searchTerm))
                    || matchesFullName(emp, searchTerm))
        .findFirst()
        .orElse(null);
  }

  /**
   * Verifica si el nombre completo del empleado coincide con el término de búsqueda.
   *
   * @param empleado Empleado a verificar.
   * @param searchTerm Término de búsqueda.
   * @return true si coincide.
   */
  private boolean matchesFullName(Empleado empleado, String searchTerm) {
    String fullName = empleado.getNombres() + " " + empleado.getApellidos();
    return fullName.equalsIgnoreCase(searchTerm);
  }

  /**
   * Valida que la cédula coincida con la del empleado.
   *
   * @param empleado Empleado a validar.
   * @param cedula Cédula proporcionada.
   * @throws IllegalArgumentException si la cédula no coincide.
   */
  private void validateCedula(Empleado empleado, String cedula) {
    if (!empleado.getCedula().equals(cedula.trim())) {
      throw new IllegalArgumentException("Credenciales inválidas");
    }
  }

  /**
   * Valida que el empleado esté activo.
   *
   * @param empleado Empleado a validar.
   * @throws IllegalArgumentException si el empleado no está activo.
   */
  private void validateEmpleadoActivo(Empleado empleado) {
    if (empleado.getEstado() != EstadoEmpleado.ACTIVO) {
      throw new IllegalArgumentException(
          "El empleado no está activo. Estado actual: " + empleado.getEstado());
    }
  }

  /**
   * Valida que el empleado tenga un cargo autorizado (Recepcionista o Administración).
   *
   * @param empleado Empleado a validar.
   * @throws IllegalArgumentException si el cargo no está autorizado.
   */
  private void validateCargo(Empleado empleado) {
    if (empleado.getCargo() == null) {
      throw new IllegalArgumentException("El empleado no tiene un cargo asignado");
    }

    String nombreCargo = empleado.getCargo().getNombre();
    if (!CARGO_ADMINISTRACION.equalsIgnoreCase(nombreCargo)
        && !CARGO_RECEPCIONISTA.equalsIgnoreCase(nombreCargo)) {
      throw new IllegalArgumentException(
          "No tiene permisos para acceder al sistema. Solo personal de Administración o Recepción puede iniciar sesión");
    }
  }
}
