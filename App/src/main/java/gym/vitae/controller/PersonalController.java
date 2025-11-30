package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.EmpleadoMapper;
import gym.vitae.model.Cargo;
import gym.vitae.model.Empleado;
import gym.vitae.model.dtos.empleado.EmpleadoCreateDTO;
import gym.vitae.model.dtos.empleado.EmpleadoDetalleDTO;
import gym.vitae.model.dtos.empleado.EmpleadoListadoDTO;
import gym.vitae.model.dtos.empleado.EmpleadoUpdateDTO;
import gym.vitae.model.enums.EstadoEmpleado;
import gym.vitae.repositories.CargoRepository;
import gym.vitae.repositories.EmpleadoRepository;
import java.util.List;

/** Controlador de Personal (Empleados). */
public class PersonalController extends BaseController {

  private final EmpleadoRepository empleadoRepository;
  private final CargoRepository cargoRepository;

  public PersonalController() {
    super();
    this.empleadoRepository = getRepository(EmpleadoRepository.class);
    this.cargoRepository = getRepository(CargoRepository.class);
  }

  PersonalController(EmpleadoRepository empleadoRepository, CargoRepository cargoRepository) {
    super(null);
    this.empleadoRepository = empleadoRepository;
    this.cargoRepository = cargoRepository;
  }

  public List<EmpleadoListadoDTO> getEmpleados() {
    return empleadoRepository.findAllListado();
  }

  /** Obtiene empleados con paginación. */
  public List<EmpleadoListadoDTO> getEmpleados(int offset, int limit) {
    return empleadoRepository.findAllListado(offset, limit);
  }

  /**
   * Obtiene empleados con filtros y paginación.
   *
   * @param searchText texto de búsqueda (nombres/apellidos)
   * @param cargoId id de cargo (nullable)
   * @param genero género como string (nullable)
   * @param offset inicio
   * @param limit cantidad
   */
  public List<EmpleadoListadoDTO> getEmpleadosWithFilters(
      String searchText, Integer cargoId, String genero, int offset, int limit) {
    return empleadoRepository.findAllListadoWithFilters(searchText, cargoId, genero, offset, limit);
  }

  /** Cuenta empleados totales. */
  public long countEmpleados() {
    return empleadoRepository.count();
  }

  /** Cuenta empleados con filtros. */
  public long countEmpleadosWithFilters(String searchText, Integer cargoId, String genero) {
    return empleadoRepository.countWithFilters(searchText, cargoId, genero);
  }

  public List<EmpleadoListadoDTO> getEmpleadosActivos() {
    return empleadoRepository.findActivosListado();
  }

  public EmpleadoDetalleDTO getEmpleadoById(int id) {
    validateId(id);
    return empleadoRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));
  }

  public List<Cargo> getCargos() {
    return cargoRepository.findAll();
  }

  public EmpleadoDetalleDTO createEmpleado(EmpleadoCreateDTO dto) {
    validateEmpleadoCreate(dto);

    Cargo cargo =
        cargoRepository
            .findById(dto.cargoId())
            .orElseThrow(
                () -> new IllegalArgumentException("Cargo no encontrado con ID: " + dto.cargoId()));

    String codigoEmpleado = generateCodigoEmpleado();
    Empleado empleado = EmpleadoMapper.toEntity(dto, cargo, codigoEmpleado);
    Empleado saved = empleadoRepository.save(empleado);

    return empleadoRepository
        .findByIdDetalle(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar empleado creado"));
  }

  public EmpleadoDetalleDTO updateEmpleado(int id, EmpleadoUpdateDTO dto) {
    validateId(id);
    validateEmpleadoUpdate(dto);

    Empleado empleado =
        empleadoRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));

    Cargo cargo =
        cargoRepository
            .findById(dto.cargoId())
            .orElseThrow(
                () -> new IllegalArgumentException("Cargo no encontrado con ID: " + dto.cargoId()));

    EmpleadoMapper.updateEntity(empleado, dto, cargo);
    empleadoRepository.update(empleado);

    return empleadoRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar empleado actualizado"));
  }

  public void deleteEmpleado(int id) {
    validateId(id);
    if (!empleadoRepository.existsById(id)) {
      throw new IllegalArgumentException("Empleado no encontrado con ID: " + id);
    }
    empleadoRepository.delete(id);
  }

  public EmpleadoDetalleDTO cambiarEstadoEmpleado(int id, EstadoEmpleado nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado no puede ser nulo");
    }

    Empleado empleado =
        empleadoRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));

    empleado.setEstado(nuevoEstado);
    empleadoRepository.update(empleado);

    return empleadoRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar empleado actualizado"));
  }

  /**
   * Da de baja a un empleado, cambiando su estado a INACTIVO y estableciendo la fecha de salida.
   *
   * @param id ID del empleado
   * @return EmpleadoDetalleDTO actualizado
   */
  public EmpleadoDetalleDTO darDeBajaEmpleado(int id) {
    validateId(id);

    Empleado empleado =
        empleadoRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));

    if (empleado.getEstado() == EstadoEmpleado.INACTIVO) {
      throw new IllegalArgumentException("El empleado ya está inactivo");
    }

    empleado.setEstado(EstadoEmpleado.INACTIVO);
    empleado.setFechaSalida(java.time.LocalDate.now());
    empleadoRepository.update(empleado);

    return empleadoRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar empleado actualizado"));
  }

  /**
   * Reactiva un empleado, cambiando su estado a ACTIVO y limpiando la fecha de salida.
   *
   * @param id ID del empleado
   * @return EmpleadoDetalleDTO actualizado
   */
  public EmpleadoDetalleDTO reactivarEmpleado(int id) {
    validateId(id);

    Empleado empleado =
        empleadoRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Empleado no encontrado con ID: " + id));

    if (empleado.getEstado() != EstadoEmpleado.INACTIVO) {
      throw new IllegalArgumentException("Solo se pueden reactivar empleados inactivos");
    }

    empleado.setEstado(EstadoEmpleado.ACTIVO);
    empleado.setFechaSalida(null);
    empleadoRepository.update(empleado);

    return empleadoRepository
        .findByIdDetalle(id)
        .orElseThrow(() -> new IllegalStateException("Error al recuperar empleado actualizado"));
  }

  private String generateCodigoEmpleado() {
    long count = empleadoRepository.count();
    int year = java.time.Year.now().getValue();
    return String.format("EMP-%d%03d", year, count + 1);
  }

  public void validateEmpleadoCreate(EmpleadoCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del empleado no pueden ser nulos");
    }

    validateNombres(dto.nombres());
    validateApellidos(dto.apellidos());
    validateCedula(dto.cedula());
    validateTelefono(dto.telefono());
    validateEmail(dto.email());
    validateDireccion(dto.direccion());
    validateFechaIngreso(dto.fechaIngreso());
    validateCargoId(dto.cargoId());

    if (dto.genero() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
    if (dto.tipoContrato() == null) {
      throw new IllegalArgumentException("El tipo de contrato es obligatorio");
    }
  }

  private void validateEmpleadoUpdate(EmpleadoUpdateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos del empleado no pueden ser nulos");
    }

    validateNombres(dto.nombres());
    validateApellidos(dto.apellidos());
    validateCedula(dto.cedula());
    validateTelefono(dto.telefono());
    validateEmail(dto.email());
    validateDireccion(dto.direccion());
    validateFechaIngreso(dto.fechaIngreso());
    validateCargoId(dto.cargoId());

    if (dto.genero() == null) {
      throw new IllegalArgumentException("El género es obligatorio");
    }
    if (dto.tipoContrato() == null) {
      throw new IllegalArgumentException("El tipo de contrato es obligatorio");
    }
    if (dto.estado() == null) {
      throw new IllegalArgumentException("El estado es obligatorio");
    }
  }
}
