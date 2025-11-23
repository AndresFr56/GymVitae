package gym.vitae.controller;

import gym.vitae.model.Clase;
import gym.vitae.model.enums.NivelClase;
import gym.vitae.repositories.ClaseRepository;
import java.util.List;

/** Controllador de la clase Clases. */
public class ClasesController extends BaseController {

  private final ClaseRepository repository;

  public ClasesController() {
    super();
    this.repository = getRepository(ClaseRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param repository Repositorio de clases.
   */
  ClasesController(ClaseRepository repository) {
    super(null);
    this.repository = repository;
  }

  /**
   * Obtiene todas las clases.
   *
   * @return Lista de clases.
   */
  public List<Clase> getClases() {
    return repository.findAll();
  }

  /**
   * Obtiene clases con paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de clases paginada.
   */
  public List<Clase> getClases(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("El offset no puede ser negativo");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("El limit debe ser mayor a 0");
    }
    return repository.findAll(offset, limit);
  }

  /**
   * Obtiene todas las clases activas.
   *
   * @return Lista de clases activas.
   */
  public List<Clase> getClasesActivas() {
    return repository.findActive();
  }

  /**
   * Obtiene una clase por ID.
   *
   * @param id ID de la clase.
   * @return Clase encontrada.
   * @throws IllegalArgumentException si no se encuentra la clase.
   */
  public Clase getClaseById(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
    return repository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con ID: " + id));
  }

  /**
   * Crea una nueva clase.
   *
   * @param clase Clase a crear.
   * @return Clase creada.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public Clase createClase(Clase clase) {
    validateClase(clase);

    // Establecer valores por defecto
    if (clase.getNivel() == null) {
      clase.setNivel(NivelClase.TODOS);
    }
    if (clase.getActiva() == null) {
      clase.setActiva(true);
    }

    return repository.save(clase);
  }

  /**
   * Actualiza una clase existente.
   *
   * @param clase Clase con datos actualizados.
   * @return true si se actualizó correctamente.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public boolean updateClase(Clase clase) {
    if (clase.getId() == null || clase.getId() <= 0) {
      throw new IllegalArgumentException("ID de clase inválido");
    }

    // Verificar que existe
    if (!repository.existsById(clase.getId())) {
      throw new IllegalArgumentException("Clase no encontrada con ID: " + clase.getId());
    }

    validateClase(clase);
    return repository.update(clase);
  }

  /**
   * Elimina una clase (soft delete - marca como inactiva).
   *
   * @param id ID de la clase a eliminar.
   * @throws IllegalArgumentException si la clase no existe.
   */
  public void deleteClase(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Clase no encontrada con ID: " + id);
    }
    repository.delete(id);
  }

  /**
   * Obtiene el total de clases.
   *
   * @return Cantidad total de clases.
   */
  public long countClases() {
    return repository.count();
  }

  /**
   * Activa o desactiva una clase.
   *
   * @param id ID de la clase.
   * @param activa Estado activo/inactivo.
   * @return true si se actualizó correctamente.
   */
  public boolean cambiarEstadoClase(int id, boolean activa) {
    Clase clase = getClaseById(id);
    clase.setActiva(activa);
    return repository.update(clase);
  }

  /**
   * Valida los datos de una clase.
   *
   * @param clase Clase a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateClase(Clase clase) {
    if (clase == null) {
      throw new IllegalArgumentException("La clase no puede ser nula");
    }

    // Validar nombre
    if (clase.getNombre() == null || clase.getNombre().trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la clase es obligatorio");
    }
    if (clase.getNombre().length() > 100) {
      throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
    }

    // Validar duración
    if (clase.getDuracionMinutos() == null) {
      throw new IllegalArgumentException("La duración es obligatoria");
    }
    if (clase.getDuracionMinutos() <= 0) {
      throw new IllegalArgumentException("La duración debe ser mayor a 0 minutos");
    }
    if (clase.getDuracionMinutos() > 480) {
      // Máximo 8 horas
      throw new IllegalArgumentException("La duración no puede exceder 480 minutos (8 horas)");
    }

    // Validar capacidad máxima
    if (clase.getCapacidadMaxima() == null) {
      throw new IllegalArgumentException("La capacidad máxima es obligatoria");
    }
    if (clase.getCapacidadMaxima() <= 0) {
      throw new IllegalArgumentException("La capacidad máxima debe ser mayor a 0");
    }
    if (clase.getCapacidadMaxima() > 100) {
      throw new IllegalArgumentException(
          "La capacidad máxima no puede exceder 100 personas por razones de seguridad");
    }

    // Validar nivel
    if (clase.getNivel() == null) {
      throw new IllegalArgumentException("El nivel de la clase es obligatorio");
    }

    // Validar descripción (opcional)
    if (clase.getDescripcion() != null && clase.getDescripcion().length() > 5000) {
      throw new IllegalArgumentException("La descripción no puede exceder 5000 caracteres");
    }
  }
}
