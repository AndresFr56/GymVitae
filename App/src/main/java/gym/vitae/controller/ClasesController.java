package gym.vitae.controller;

import gym.vitae.mapper.ClaseMapper;
import gym.vitae.model.Clase;
import gym.vitae.model.dtos.clase.ClaseCreateDTO;
import gym.vitae.model.dtos.clase.ClaseDetalleDTO;
import gym.vitae.model.dtos.clase.ClaseListadoDTO;
import gym.vitae.model.dtos.clase.ClaseUpdateDTO;
import gym.vitae.model.enums.NivelClase;
import gym.vitae.repositories.ClaseRepository;
import java.util.List;

/** Controllador de la clase Clases. */
public class ClasesController extends BaseController {

  private final ClaseRepository repository;

  /** Controlador para la inyección de dependencias. */
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
   * Obtiene todas las clases como DTOs de listado.
   *
   * @return Lista de ClaseListadoDTO.
   */
  public List<ClaseListadoDTO> getClases() {
    return repository.findAllListado();
  }

  /**
   * Obtiene clases con paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de clases paginada.
   */
  public List<ClaseListadoDTO> getClases(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("El offset no puede ser negativo");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("El limit debe ser mayor a 0");
    }
    return repository.findAllListado(offset, limit);
  }

  /**
   * Obtiene todas las clases activas.
   *
   * @return Lista de clases activas.
   */
  public List<ClaseListadoDTO> getClasesActivas() {
    return repository.findActivosListado();
  }

  /**
   * Obtiene una clase por ID.
   *
   * @param id ID de la clase.
   * @return ClaseDetalleDTO encontrada.
   * @throws IllegalArgumentException si no se encuentra la clase.
   */
  public ClaseDetalleDTO getClaseById(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
    return repository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con ID: " + id));
  }

  /**
   * Obtiene clases con filtros y paginación.
   *
   * @param searchText Texto de búsqueda en nombre (puede ser null).
   * @param nivel Nivel para filtrar (puede ser null).
   * @param conCupos Filtrar solo clases con cupos disponibles (puede ser null).
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de clases que coinciden con los filtros.
   */
  public List<ClaseListadoDTO> getClasesWithFilters(
      String searchText, String nivel, Boolean conCupos, int offset, int limit) {
    return repository.findAllListadoWithFilters(searchText, nivel, conCupos, offset, limit);
  }

  /**
   * Cuenta clases con filtros.
   *
   * @param searchText Texto de búsqueda en nombre (puede ser null).
   * @param nivel Nivel para filtrar (puede ser null).
   * @param conCupos Filtrar solo clases con cupos disponibles (puede ser null).
   * @return Cantidad de clases que coinciden con los filtros.
   */
  public long countClasesWithFilters(String searchText, String nivel, Boolean conCupos) {
    return repository.countWithFilters(searchText, nivel, conCupos);
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
   * Crea una nueva clase.
   *
   * @param dto DTO con datos de la clase a crear.
   * @return ClaseDetalleDTO creada.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public ClaseDetalleDTO createClase(ClaseCreateDTO dto) {
    validateClaseCreate(dto);
    validateNombreNoDuplicado(dto.nombre(), null);

    Clase clase = ClaseMapper.toEntity(dto);
    Clase saved = repository.save(clase);

    return repository
        .findByIdDetalle(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar clase creada"));
  }

  /**
   * Actualiza una clase existente.
   *
   * @param id ID de la clase a actualizar.
   * @param dto DTO con datos actualizados.
   * @return ClaseDetalleDTO actualizada.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public ClaseDetalleDTO updateClase(int id, ClaseUpdateDTO dto) {
    if (id <= 0) {
      throw new IllegalArgumentException("ID de clase inválido");
    }

    Clase clase =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con ID: " + id));

    validateClaseUpdate(dto);
    validateNombreNoDuplicado(dto.nombre(), id);

    ClaseMapper.updateEntity(clase, dto);
    repository.update(clase);

    return repository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar clase actualizada"));
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
   * Activa o desactiva una clase.
   *
   * @param id ID de la clase.
   * @param activa Estado activo/inactivo.
   * @return ClaseDetalleDTO actualizada.
   */
  public ClaseDetalleDTO cambiarEstadoClase(int id, boolean activa) {
    Clase clase =
        repository
            .findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Clase no encontrada con ID: " + id));

    clase.setActiva(activa);
    repository.update(clase);

    return repository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar clase actualizada"));
  }

  private void validateClaseCreate(ClaseCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos de la clase no pueden ser nulos");
    }

    validateNombre(dto.nombre());
    validateDuracion(dto.duracionMinutos());
    validateCapacidad(dto.capacidadMaxima());
    validateDescripcion(dto.descripcion());
  }

  private void validateClaseUpdate(ClaseUpdateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos de la clase no pueden ser nulos");
    }

    validateNombre(dto.nombre());
    validateDuracion(dto.duracionMinutos());
    validateCapacidad(dto.capacidadMaxima());
    validateNivel(dto.nivel());
    validateDescripcion(dto.descripcion());

    if (dto.activa() == null) {
      throw new IllegalArgumentException("El estado activo es obligatorio");
    }
  }

  /** Valida el nombre de la clase. */
  private void validateNombre(String nombre) {
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
  }

  /** Valida que el nombre no esté duplicado. */
  private void validateNombreNoDuplicado(String nombre, Integer idActual) {
    // Buscar por nombre existente excluyendo el ID actual
    List<ClaseListadoDTO> clases = repository.findAllListado();
    boolean existe =
        clases.stream()
            .anyMatch(c -> c.nombre().equalsIgnoreCase(nombre.trim()) && !c.id().equals(idActual));

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
    if (descripcion != null && descripcion.length() > 200) {
      throw new IllegalArgumentException("La descripción no puede exceder 500 caracteres");
    }
  }
}
