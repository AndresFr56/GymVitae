package gym.vitae.core;

import gym.vitae.repositories.*;
import javax.persistence.EntityManager;
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
    dbManager.init();

    sc.register(DBConnectionManager.class, dbManager);

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

    if (dbManager.isConnected()) {
      EntityManagerFactory emf = dbManager.getEntityManagerFactory();
      EntityManager em = dbManager.getEntityManager();
      sc.register(EntityManagerFactory.class, emf);
      sc.register(EntityManager.class, em);
    }
  }

  /**
   * Cierra los recursos abiertos por la aplicación, como el EntityManager y el
   * EntityManagerFactory.
   */
  public static void shutdown() {
    ServiceContainer sc = ServiceContainer.getInstance();

    if (sc.isRegistered(EntityManager.class)) {
      EntityManager em = sc.resolve(EntityManager.class);
      if (em.isOpen()) em.close();
    }

    if (sc.isRegistered(EntityManagerFactory.class)) {
      EntityManagerFactory emf = sc.resolve(EntityManagerFactory.class);
      if (emf.isOpen()) emf.close();
    }

    if (sc.isRegistered(DBConnectionManager.class)) {
      DBConnectionManager db = sc.resolve(DBConnectionManager.class);
      db.close();
    }

    sc.clear();
  }
}
