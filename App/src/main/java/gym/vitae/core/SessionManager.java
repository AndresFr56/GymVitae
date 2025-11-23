package gym.vitae.core;

import gym.vitae.model.Empleado;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Singleton que gestiona la sesión del empleado autenticado.
 *
 * <p>Mantiene el estado del empleado que ha iniciado sesión en la aplicación.
 */
public final class SessionManager {

  private static final SessionManager INSTANCE = new SessionManager();
  private Empleado empleadoActual;
  private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

  private SessionManager() {}

  public void addPropertyChangeListener(PropertyChangeListener listener) {
    pcs.addPropertyChangeListener(listener);
  }

  public void removePropertyChangeListener(PropertyChangeListener listener) {
    pcs.removePropertyChangeListener(listener);
  }

  /**
   * Obtiene la instancia única del SessionManager.
   *
   * @return Instancia del SessionManager.
   */
  public static SessionManager getInstance() {
    return INSTANCE;
  }

  /**
   * Obtiene el empleado actualmente autenticado.
   *
   * @return Empleado autenticado o null si no hay sesión activa.
   */
  public Empleado getEmpleadoActual() {
    return empleadoActual;
  }

  /**
   * Establece el empleado actualmente autenticado.
   *
   * @param empleado Empleado que ha iniciado sesión.
   */
  public void setEmpleadoActual(Empleado empleado) {
    Empleado old = this.empleadoActual;
    this.empleadoActual = empleado;
    pcs.firePropertyChange("empleadoActual", old, empleado);
  }

  /**
   * Verifica si hay un empleado autenticado.
   *
   * @return true si hay una sesión activa, false en caso contrario.
   */
  public boolean isAuthenticated() {
    return empleadoActual != null;
  }

  /**
   * Cierra la sesión del empleado actual.
   */
  public void logout() {
    Empleado old = this.empleadoActual;
    this.empleadoActual = null;
    pcs.firePropertyChange("empleadoActual", old, null);
  }

  /**
   * Obtiene el ID del empleado autenticado.
   *
   * @return ID del empleado o null si no hay sesión activa.
   */
  public Integer getEmpleadoId() {
    return empleadoActual != null ? empleadoActual.getId() : null;
  }

  /**
   * Obtiene el nombre completo del empleado autenticado.
   *
   * @return Nombre completo del empleado o null si no hay sesión activa.
   */
  public String getNombreCompleto() {
    if (empleadoActual == null) {
      return null;
    }
    return empleadoActual.getNombres() + " " + empleadoActual.getApellidos();
  }

  /**
   * Obtiene el cargo del empleado autenticado.
   *
   * @return Nombre del cargo o null si no hay sesión activa.
   */
  public String getCargo() {
    if (empleadoActual == null || empleadoActual.getCargo() == null) {
      return null;
    }
    return empleadoActual.getCargo().getNombre();
  }
}
