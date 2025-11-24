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

    validateNombre(clase.getNombre(), clase.getId());
    validateDuracion(clase.getDuracionMinutos());
    validateCapacidad(clase.getCapacidadMaxima());
    validateNivel(clase.getNivel());
    validateDescripcion(clase.getDescripcion());
  }

  /** Valida el nombre de la clase. */
  private void validateNombre(String nombre, Integer idActual) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre de la clase es obligatorio");
    }
    if (nombre.length() > 100) {
      throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
    }

    // Validar solo letras y espacios
    if (!nombre.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")) {
      throw new IllegalArgumentException("El nombre solo puede contener letras y espacios");
    }

    // Validar que no esté duplicado
    validateNombreNoDuplicado(nombre.trim(), idActual);
  }

  /** Valida que el nombre no esté duplicado. */
  private void validateNombreNoDuplicado(String nombre, Integer idActual) {
    List<Clase> clases = repository.findAll();
    boolean existe =
        clases.stream()
            .anyMatch(
                c ->
                    c.getNombre().equalsIgnoreCase(nombre)
                        && (!c.getId().equals(idActual)));

    if (existe) {
      throw new IllegalArgumentException("Ya existe una clase con el nombre: " + nombre);
    }
  }

  /** Valida la duración en minutos. */
  private void validateDuracion(Integer duracion) {
    if (duracion == null) {
      throw new IllegalArgumentException("La duración es obligatoria");
    }
    if (duracion < 15 || duracion > 180) {
      throw new IllegalArgumentException("La duración debe estar entre 15 y 180 minutos");
    }

    // Validar que sea múltiplo de 5
    if (duracion % 5 != 0) {
      throw new IllegalArgumentException("La duración debe ser múltiplo de 5 minutos");
    }
  }

  /** Valida la capacidad máxima. */
  private void validateCapacidad(Integer capacidad) {
    if (capacidad == null) {
      throw new IllegalArgumentException("La capacidad máxima es obligatoria");
    }
    if (capacidad < 1 || capacidad > 50) {
      throw new IllegalArgumentException("La capacidad debe estar entre 1 y 50 personas");
    }
  }

  /** Valida el nivel de la clase. */
  private void validateNivel(NivelClase nivel) {
    if (nivel == null) {
      throw new IllegalArgumentException("El nivel de la clase es obligatorio");
    }
  }

  /** Valida la descripción (opcional). */
  private void validateDescripcion(String descripcion) {
    if (descripcion != null && descripcion.length() > 500) {
      throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
    }
  }
}
