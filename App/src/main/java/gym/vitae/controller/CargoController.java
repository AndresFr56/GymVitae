package gym.vitae.controller;

import gym.vitae.model.Cargo;
import gym.vitae.repositories.CargoRepository;
import java.math.BigDecimal;
import java.util.List;

/** Controlador de Cargos. */
public class CargoController extends BaseController {

  private static final BigDecimal SALARIO_MINIMO = new BigDecimal("300.00");
  private static final String PATTERN_SOLO_LETRAS_ESPACIOS = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

  private final CargoRepository repository;

  public CargoController() {
    super();
    this.repository = getRepository(CargoRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param repository Repositorio de cargos.
   */
  CargoController(CargoRepository repository) {
    super(null);
    this.repository = repository;
  }

  /**
   * Obtiene todos los cargos.
   *
   * @return Lista de cargos.
   */
  public List<Cargo> getCargos() {
    return repository.findAll();
  }

  /**
   * Obtiene cargos con paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de cargos paginada.
   */
  public List<Cargo> getCargos(int offset, int limit) {
    validatePagination(offset, limit);
    return repository.findAll(offset, limit);
  }

  /**
   * Obtiene cargos filtrados por rango de salario base.
   *
   * @param salarioMinimo Salario base mínimo.
   * @param salarioMaximo Salario base máximo.
   * @return Lista de cargos filtrados.
   */
  public List<Cargo> getCargosBySalarioRange(BigDecimal salarioMinimo, BigDecimal salarioMaximo) {
    validateSalarioRange(salarioMinimo, salarioMaximo);

    return repository.findAll().stream()
        .filter(
            cargo ->
                cargo.getSalarioBase().compareTo(salarioMinimo) >= 0
                    && cargo.getSalarioBase().compareTo(salarioMaximo) <= 0)
        .toList();
  }

  /**
   * Obtiene un cargo por ID.
   *
   * @param id ID del cargo.
   * @return Cargo encontrado.
   * @throws IllegalArgumentException si no se encuentra el cargo.
   */
  public Cargo getCargoById(int id) {
    validateId(id);
    return repository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cargo no encontrado con ID: " + id));
  }

  /**
   * Crea un nuevo cargo.
   *
   * @param cargo Cargo a crear.
   * @return Cargo creado.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public Cargo createCargo(Cargo cargo) {
    validateCargo(cargo);
    validateNombreNoDuplicado(cargo.getNombre(), null);

    // Establecer valores por defecto
    if (cargo.getActivo() == null) {
      cargo.setActivo(true);
    }

    return repository.save(cargo);
  }

  /**
   * Actualiza un cargo existente.
   *
   * @param cargo Cargo con datos actualizados.
   * @return true si se actualizó correctamente.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public boolean updateCargo(Cargo cargo) {
    validateUpdateId(cargo);
    validateCargoExists(cargo.getId());
    validateCargo(cargo);
    validateNombreNoDuplicado(cargo.getNombre(), cargo.getId());

    return repository.update(cargo);
  }

  /**
   * Elimina un cargo (soft delete - marca como inactivo).
   *
   * @param id ID del cargo a eliminar.
   * @throws IllegalArgumentException si el cargo no existe o tiene empleados asociados.
   */
  public void deleteCargo(int id) {
    validateId(id);
    Cargo cargo = getCargoById(id);
    validateCargoSinEmpleados(cargo);
    repository.delete(id);
  }

  /**
   * Obtiene el total de cargos.
   *
   * @return Cantidad total de cargos.
   */
  public long countCargos() {
    return repository.count();
  }

  /**
   * Activa o desactiva un cargo.
   *
   * @param id ID del cargo.
   * @param activo Estado activo/inactivo.
   * @return true si se actualizó correctamente.
   */
  public boolean cambiarEstadoCargo(int id, boolean activo) {
    Cargo cargo = getCargoById(id);

    // Si se está desactivando, verificar que no tenga empleados activos
    if (!activo) {
      validateCargoSinEmpleados(cargo);
    }

    cargo.setActivo(activo);
    return repository.update(cargo);
  }

  /**
   * Valida los datos de un cargo.
   *
   * @param cargo Cargo a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateCargo(Cargo cargo) {
    if (cargo == null) {
      throw new IllegalArgumentException("El cargo no puede ser nulo");
    }

    validateNombre(cargo.getNombre());
    validateSalarioBase(cargo.getSalarioBase());
    validateDescripcion(cargo.getDescripcion());
  }

  /**
   * Valida el nombre del cargo.
   *
   * @param nombre Nombre a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateNombre(String nombre) {
    if (nombre == null || nombre.trim().isEmpty()) {
      throw new IllegalArgumentException("El nombre del cargo es obligatorio");
    }
    if (nombre.length() > 100) {
      throw new IllegalArgumentException("El nombre no puede exceder 100 caracteres");
    }
    if (!nombre.matches(PATTERN_SOLO_LETRAS_ESPACIOS)) {
      throw new IllegalArgumentException(
          "El nombre solo puede contener letras y espacios, sin números ni caracteres especiales");
    }
  }

  /**
   * Valida el salario base del cargo.
   *
   * @param salarioBase Salario a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateSalarioBase(BigDecimal salarioBase) {
    if (salarioBase == null) {
      throw new IllegalArgumentException("El salario base es obligatorio");
    }
    if (salarioBase.compareTo(SALARIO_MINIMO) < 0) {
      throw new IllegalArgumentException("El salario base debe ser mayor a 300");
    }
    if (salarioBase.compareTo(new BigDecimal("99999999.99")) > 0) {
      throw new IllegalArgumentException(
          "El salario base no puede exceder 99,999,999.99 (límite de precisión)");
    }
    if (salarioBase.scale() > 2) {
      throw new IllegalArgumentException("El salario base no puede tener más de 2 decimales");
    }
  }

  /**
   * Valida la descripción del cargo.
   *
   * @param descripcion Descripción a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateDescripcion(String descripcion) {
    if (descripcion == null || descripcion.trim().isEmpty()) {
      return; // La descripción es opcional
    }
    if (descripcion.length() > 100) {
      throw new IllegalArgumentException("La descripción no puede exceder 100 caracteres");
    }
    if (!descripcion.matches(PATTERN_SOLO_LETRAS_ESPACIOS)) {
      throw new IllegalArgumentException(
          "La descripción solo puede contener letras y espacios, sin números ni caracteres especiales");
    }
  }

  /**
   * Valida que el nombre del cargo no esté duplicado.
   *
   * @param nombre Nombre a validar.
   * @param idActual ID del cargo actual (null si es creación).
   * @throws IllegalArgumentException si el nombre ya existe.
   */
  private void validateNombreNoDuplicado(String nombre, Integer idActual) {
    List<Cargo> cargosExistentes =
        repository.findAll().stream()
            .filter(
                c ->
                    c.getNombre().equalsIgnoreCase(nombre.trim())
                        && (!c.getId().equals(idActual)))
            .toList();

    if (!cargosExistentes.isEmpty()) {
      throw new IllegalArgumentException("Ya existe un cargo con el nombre: " + nombre);
    }
  }

  /**
   * Valida que un cargo no tenga empleados asociados.
   *
   * @param cargo Cargo a validar.
   * @throws IllegalArgumentException si el cargo tiene empleados asociados.
   */
  private void validateCargoSinEmpleados(Cargo cargo) {
    if (cargo.getEmpleados() != null && !cargo.getEmpleados().isEmpty()) {
      throw new IllegalArgumentException(
          "No se puede eliminar/desactivar el cargo porque tiene empleados asociados");
    }
  }

  /**
   * Valida el rango de salarios para filtrado.
   *
   * @param salarioMinimo Salario mínimo.
   * @param salarioMaximo Salario máximo.
   * @throws IllegalArgumentException si el rango no es válido.
   */
  private void validateSalarioRange(BigDecimal salarioMinimo, BigDecimal salarioMaximo) {
    if (salarioMinimo == null) {
      throw new IllegalArgumentException("El salario base mínimo es obligatorio");
    }
    if (salarioMaximo == null) {
      throw new IllegalArgumentException("El salario base máximo es obligatorio");
    }
    if (salarioMinimo.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("El salario base mínimo debe ser un número positivo");
    }
    if (salarioMaximo.compareTo(BigDecimal.ZERO) < 0) {
      throw new IllegalArgumentException("El salario base máximo debe ser un número positivo");
    }
    if (salarioMinimo.compareTo(salarioMaximo) > 0) {
      throw new IllegalArgumentException(
          "El salario base mínimo debe ser menor o igual al salario base máximo");
    }
  }

  /**
   * Valida un ID.
   *
   * @param id ID a validar.
   * @throws IllegalArgumentException si el ID no es válido.
   */
  private void validateId(int id) {
    if (id <= 0) {
      throw new IllegalArgumentException("El ID debe ser mayor a 0");
    }
  }

  /**
   * Valida que un cargo existe.
   *
   * @param id ID del cargo.
   * @throws IllegalArgumentException si el cargo no existe.
   */
  private void validateCargoExists(int id) {
    if (!repository.existsById(id)) {
      throw new IllegalArgumentException("Cargo no encontrado con ID: " + id);
    }
  }

  /**
   * Valida el ID de un cargo para actualización.
   *
   * @param cargo Cargo a validar.
   * @throws IllegalArgumentException si el ID no es válido.
   */
  private void validateUpdateId(Cargo cargo) {
    if (cargo.getId() == null || cargo.getId() <= 0) {
      throw new IllegalArgumentException("ID de cargo inválido");
    }
  }

  /**
   * Valida los parámetros de paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @throws IllegalArgumentException si los parámetros no son válidos.
   */
  private void validatePagination(int offset, int limit) {
    if (offset < 0) {
      throw new IllegalArgumentException("El offset no puede ser negativo");
    }
    if (limit <= 0) {
      throw new IllegalArgumentException("El limit debe ser mayor a 0");
    }
  }
}
