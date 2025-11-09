package gym.vitae.core;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Contenedor simple de inyección de dependencias. Implementa el patrón Service Locator con registro
 * de singletons.
 *
 * @implNote No se debe de utilizar en los Tests.
 */
public class ServiceContainer {

  private static final ServiceContainer INSTANCE = new ServiceContainer();
  private final Map<Class<?>, Object> services = new ConcurrentHashMap<>();

  private ServiceContainer() {}

  public static ServiceContainer getInstance() {
    return INSTANCE;
  }

  /** Registra un servicio/repository en el contenedor */
  public <T> void register(Class<T> serviceClass, T implementation) {
    services.put(serviceClass, implementation);
  }

  /** Obtiene una instancia del servicio/repository */
  @SuppressWarnings("unchecked")
  public <T> T resolve(Class<T> serviceClass) {
    T service = (T) services.get(serviceClass);
    if (service == null) {
      throw new IllegalStateException("Service not registered: " + serviceClass.getName());
    }
    return service;
  }

  /** Verifica si un servicio está registrado */
  public boolean isRegistered(Class<?> serviceClass) {
    return services.containsKey(serviceClass);
  }

  /** Limpia todos los servicios registrados */
  public void clear() {
    services.clear();
  }
}
