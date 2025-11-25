package gym.vitae.core;

import gym.vitae.repositories.*;
import java.util.logging.Logger;
import javax.persistence.EntityManagerFactory;

/**
 * ApplicationConfig es el entrypoint para la configuración de la aplicación. Permite inicializar y
 * cerrar los recursos necesarios como los repositorios y el EntityManager de JPA.
 *
 * @implNote Se debe de llamar a init() al iniciar la aplicación y a shutdown() al cerrarla.
 * @implNote No se debe de modifivar al menos que sea para agregar nuevos repositorios.
 */
public final class ApplicationConfig {

  private ApplicationConfig() {}

  public static void init() {
    ServiceContainer sc = ServiceContainer.getInstance();
    DBConnectionManager dbManager = new DBConnectionManager();

    try {
      dbManager.init();

      if (!dbManager.isConnected()) {
        DatabaseUnavailableException ex =
            new DatabaseUnavailableException(
                "No se pudo establecer conexión inicial con la base de datos");
        ErrorHandler.handleDatabaseError(ex);
        throw ex;
      }

      sc.register(DBConnectionManager.class, dbManager);

      // Registrar repositorios de forma segura
      registerRepositories(sc, dbManager);

      // Registrar EntityManagerFactory solo si está conectado
      if (dbManager.isConnected()) {
        EntityManagerFactory emf = dbManager.getEntityManagerFactory();
        sc.register(EntityManagerFactory.class, emf);
      }

    } catch (DatabaseUnavailableException e) {
      // Ya fue manejado por ErrorHandler
      throw e;
    } catch (Exception e) {
      ErrorHandler.handleUnexpectedError("Error inesperado al inicializar la aplicación", e);
      dbManager.close();
    }
  }

  private static void registerRepositories(ServiceContainer sc, DBConnectionManager dbManager) {
    try {
      sc.register(BeneficioRepository.class, new BeneficioRepository(dbManager));
      sc.register(CargoRepository.class, new CargoRepository(dbManager));
      sc.register(CategoriaRepository.class, new CategoriaRepository(dbManager));
      sc.register(ClaseRepository.class, new ClaseRepository(dbManager));
      sc.register(ClienteRepository.class, new ClienteRepository(dbManager));
      sc.register(DetallesFacturaRepository.class, new DetallesFacturaRepository(dbManager));
      sc.register(EmpleadoRepository.class, new EmpleadoRepository(dbManager));
      sc.register(EquipoRepository.class, new EquipoRepository(dbManager));
      sc.register(FacturaRepository.class, new FacturaRepository(dbManager));
      sc.register(HorarioRepository.class, new HorarioRepository(dbManager));
      sc.register(InscripcionesClaseRepository.class, new InscripcionesClaseRepository(dbManager));
      sc.register(IvaRepository.class, new IvaRepository(dbManager));
      sc.register(MembresiaRepository.class, new MembresiaRepository(dbManager));
      sc.register(MembresiaBeneficioRepository.class, new MembresiaBeneficioRepository(dbManager));
      sc.register(
          MovimientosInventarioRepository.class, new MovimientosInventarioRepository(dbManager));
      sc.register(NominaRepository.class, new NominaRepository(dbManager));
      sc.register(PagoRepository.class, new PagoRepository(dbManager));
      sc.register(ProductoRepository.class, new ProductoRepository(dbManager));
      sc.register(ProveedoreRepository.class, new ProveedoreRepository(dbManager));
      sc.register(TiposMembresiaRepository.class, new TiposMembresiaRepository(dbManager));
    } catch (Exception e) {
      ErrorHandler.handleUnexpectedError("Error al registrar repositorios", e);
      throw e;
    }
  }

  public static void shutdown() {
    ServiceContainer sc = ServiceContainer.getInstance();

    try {
      if (sc.isRegistered(EntityManagerFactory.class)) {
        EntityManagerFactory emf = sc.resolve(EntityManagerFactory.class);
        if (emf != null && emf.isOpen()) {
          emf.close();
        }
      }

      if (sc.isRegistered(DBConnectionManager.class)) {
        DBConnectionManager db = sc.resolve(DBConnectionManager.class);
        db.close();
      }
    } catch (Exception e) {
      // Log error pero no lanzar excepción durante shutdown
      Logger.getLogger("Error durante shutdown: " + e.getMessage());
    } finally {
      sc.clear();
    }
  }
}
