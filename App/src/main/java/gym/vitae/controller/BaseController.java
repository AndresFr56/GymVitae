package gym.vitae.controller;

import gym.vitae.core.ServiceContainer;
import gym.vitae.repositories.IRepository;

/**
 * Controlador base que proporciona acceso a repositories
 * mediante el Service Container.
 */
public abstract class BaseController {

    protected final ServiceContainer container;

    protected BaseController() {
        this.container = ServiceContainer.getInstance();
    }

    /**
     * Constructor protegido para inyección de dependencias en pruebas.
     * @param container Contenedor de servicios/repositories a utilizar
     */
    protected BaseController(ServiceContainer container) {
        this.container = container;
    }

    /**
     * Obtiene un repository del contenedor de servicio
     * @param repoClass Clase del repository a obtener
     * @return Instancia del repository solicitado
     * @apiNote Nota Este método solo debe ser llamado desde constructores de producción,
     * no en modo de prueba.
     * @throws IllegalStateException si se llama sin ServiceContainer (en tests)
     */
    protected <R extends IRepository<?>> R getRepository(Class<R> repoClass) {
        if (container == null) {
            throw new IllegalStateException(
                    "Cannot resolve repository '" + repoClass.getSimpleName() + "': " +
                            "ServiceContainer not available. " +
                            "This method should only be called from production constructors, " +
                            "not in test mode."
            );
        }
        return container.resolve(repoClass);
    }
}
