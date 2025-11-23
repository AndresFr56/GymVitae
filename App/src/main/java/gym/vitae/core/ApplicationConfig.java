package gym.vitae.core;

import gym.vitae.repositories.*;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

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

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("gym_vitaePU");
    EntityManager em = emf.createEntityManager();

    ServiceContainer sc = ServiceContainer.getInstance();

    // recursos necesarios de jpa entity manager
    sc.register(EntityManagerFactory.class, emf);
    sc.register(EntityManager.class, em);

    // repositorios definidos
    sc.register(BeneficioRepository.class, new BeneficioRepository(em));
    sc.register(CargoRepository.class, new CargoRepository(em));
    sc.register(CategoriaRepository.class, new CategoriaRepository(em));
    sc.register(ClaseRepository.class, new ClaseRepository(em));
    sc.register(ClienteRepository.class, new ClienteRepository(em));
    sc.register(DetallesFacturaRepository.class, new DetallesFacturaRepository(em));
    sc.register(EmpleadoRepository.class, new EmpleadoRepository(em));
    sc.register(EquipoRepository.class, new EquipoRepository(em));
    sc.register(FacturaRepository.class, new FacturaRepository(em));
    sc.register(HorarioRepository.class, new HorarioRepository(em));
    sc.register(InscripcionesClaseRepository.class, new InscripcionesClaseRepository(em));
    sc.register(IvaRepository.class, new IvaRepository(em));
    sc.register(MembresiaRepository.class, new MembresiaRepository(em));
    sc.register(MembresiaBeneficioRepository.class, new MembresiaBeneficioRepository(em));
    sc.register(MovimientosInventarioRepository.class, new MovimientosInventarioRepository(em));
    sc.register(NominaRepository.class, new NominaRepository(em));
    sc.register(PagoRepository.class, new PagoRepository(em));
    sc.register(ProductoRepository.class, new ProductoRepository(em));
    sc.register(ProveedoreRepository.class, new ProveedoreRepository(em));
    sc.register(TiposMembresiaRepository.class, new TiposMembresiaRepository(em));
  }

  /**
   * Cierra los recursos abiertos por la aplicación, como el EntityManager y el
   * EntityManagerFactory.
   */
  public static void shutdown() {
    ServiceContainer sc = ServiceContainer.getInstance();

    if (sc.isRegistered(EntityManager.class)) {
      EntityManager em = sc.resolve(EntityManager.class);
      em.close();
    }

    if (sc.isRegistered(EntityManagerFactory.class)) {
      EntityManagerFactory emf = sc.resolve(EntityManagerFactory.class);
      emf.close();
    }

    sc.clear();
  }
}
