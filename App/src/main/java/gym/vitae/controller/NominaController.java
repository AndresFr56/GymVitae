package gym.vitae.controller;

import gym.vitae.model.Empleado;
import gym.vitae.model.Nomina;
import gym.vitae.model.enums.EstadoNomina;
import gym.vitae.repositories.EmpleadoRepository;
import gym.vitae.repositories.NominaRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Controlador de Nóminas. */
public class NominaController extends BaseController {

  private final NominaRepository nominaRepository;
  private final EmpleadoRepository empleadoRepository;

  public NominaController() {
    super();
    this.nominaRepository = getRepository(NominaRepository.class);
    this.empleadoRepository = getRepository(EmpleadoRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param nominaRepository Repositorio de nóminas.
   * @param empleadoRepository Repositorio de empleados.
   */
  NominaController(NominaRepository nominaRepository, EmpleadoRepository empleadoRepository) {
    super(null);
    this.nominaRepository = nominaRepository;
    this.empleadoRepository = empleadoRepository;
  }

  /**
   * Obtiene todas las nóminas.
   *
   * @return Lista de nóminas.
   */
  public List<Nomina> getNominas() {
    return nominaRepository.findAll();
  }

  /**
   * Obtiene nóminas con paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de nóminas paginada.
   */
  public List<Nomina> getNominas(int offset, int limit) {
    validatePagination(offset, limit);
    return nominaRepository.findAll(offset, limit);
  }

  /**
   * Obtiene una nómina por ID.
   *
   * @param id ID de la nómina.
   * @return Nómina encontrada.
   * @throws IllegalArgumentException si no se encuentra la nómina.
   */
  public Nomina getNominaById(int id) {
    validateId(id);
    return nominaRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Nómina no encontrada con ID: " + id));
  }

  /**
   * Busca una nómina por empleado y periodo.
   *
   * @param empleadoId ID del empleado.
   * @param mes Mes (1-12).
   * @param anio Año.
   * @return Nómina encontrada si existe.
   */
  public Nomina getNominaByEmpleadoAndPeriodo(int empleadoId, int mes, int anio) {
    validateId(empleadoId);
    validateMes(mes);
    validateAnio(anio);

    return nominaRepository.findByEmpleadoAndPeriodo(empleadoId, (byte) mes, anio).orElse(null);
  }

  /**
   * Crea una nueva nómina.
   *
   * @param nomina Nómina a crear.
   * @return Nómina creada.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public Nomina createNomina(Nomina nomina) {
    validateNomina(nomina);
    validateAndSetEmpleado(nomina);
    validateNominaNoExiste(nomina);

    // Establecer estado inicial
    if (nomina.getEstado() == null) {
      nomina.setEstado(EstadoNomina.PENDIENTE);
    }

    // Validar y establecer valores por defecto para montos
    setDefaultMontos(nomina);

    return nominaRepository.save(nomina);
  }

  /**
   * Actualiza una nómina existente.
   *
   * @param nomina Nómina con datos actualizados.
   * @return true si se actualizó correctamente.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public boolean updateNomina(Nomina nomina) {
    validateUpdateId(nomina);
    validateNominaExists(nomina.getId());
    validateNomina(nomina);
    validateAndSetEmpleado(nomina);

    return nominaRepository.update(nomina);
  }

  /**
   * Anula una nómina (soft delete).
   *
   * @param id ID de la nómina a anular.
   * @throws IllegalArgumentException si la nómina no existe.
   */
  public void anularNomina(int id) {
    validateId(id);
    validateNominaExists(id);
    nominaRepository.delete(id);
  }

  /**
   * Obtiene el total de nóminas.
   *
   * @return Cantidad total de nóminas.
   */
  public long countNominas() {
    return nominaRepository.count();
  }

  /**
   * Cambia el estado de una nómina.
   *
   * @param id ID de la nómina.
   * @param nuevoEstado Nuevo estado.
   * @return true si se actualizó correctamente.
   */
  public boolean cambiarEstadoNomina(int id, EstadoNomina nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado no puede ser nulo");
    }
    Nomina nomina = getNominaById(id);
    validateCambioEstado(nomina, nuevoEstado);
    nomina.setEstado(nuevoEstado);
    return nominaRepository.update(nomina);
  }

  /**
   * Aprueba una nómina.
   *
   * @param id ID de la nómina.
   * @param aprobadaPor Empleado que aprueba.
   * @return true si se aprobó correctamente.
   */
  public boolean aprobarNomina(int id, Empleado aprobadaPor) {
    Nomina nomina = getNominaById(id);
    if (nomina.getEstado() != EstadoNomina.PENDIENTE) {
      throw new IllegalArgumentException("Solo se pueden aprobar nóminas en estado PENDIENTE");
    }
    nomina.setEstado(EstadoNomina.APROBADA);
    nomina.setAprobadaPor(aprobadaPor);
    return nominaRepository.update(nomina);
  }

  /**
   * Marca una nómina como pagada.
   *
   * @param id ID de la nómina.
   * @param pagadaPor Empleado que registra el pago.
   * @param fechaPago Fecha del pago.
   * @return true si se actualizó correctamente.
   */
  public boolean pagarNomina(int id, Empleado pagadaPor, LocalDate fechaPago) {
    Nomina nomina = getNominaById(id);
    if (nomina.getEstado() != EstadoNomina.APROBADA) {
      throw new IllegalArgumentException("Solo se pueden pagar nóminas en estado APROBADA");
    }
    nomina.setEstado(EstadoNomina.PAGADA);
    nomina.setPagadaPor(pagadaPor);
    nomina.setFechaPago(fechaPago != null ? fechaPago : LocalDate.now());
    return nominaRepository.update(nomina);
  }

  /**
   * Valida los datos de una nómina.
   *
   * @param nomina Nómina a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateNomina(Nomina nomina) {
    if (nomina == null) {
      throw new IllegalArgumentException("La nómina no puede ser nula");
    }

    validateMes(nomina.getMes());
    validateAnio(nomina.getAnio());
    validateMontos(nomina);
    validateObservaciones(nomina.getObservaciones());
  }

  /**
   * Valida y establece el empleado de la nómina.
   *
   * @param nomina Nómina a validar.
   * @throws IllegalArgumentException si el empleado no es válido.
   */
  private void validateAndSetEmpleado(Nomina nomina) {
    if (nomina.getEmpleado() == null || nomina.getEmpleado().getId() == null) {
      throw new IllegalArgumentException("El empleado es obligatorio");
    }

    Empleado empleado =
        empleadoRepository
            .findById(nomina.getEmpleado().getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Empleado no encontrado con ID: " + nomina.getEmpleado().getId()));

    nomina.setEmpleado(empleado);

    // Si no hay salario base, usar el del cargo
    if (nomina.getSalarioBase() == null && empleado.getCargo() != null) {
      nomina.setSalarioBase(empleado.getCargo().getSalarioBase());
    }
  }

  /**
   * Valida que no exista una nómina para el mismo empleado y periodo.
   *
   * @param nomina Nómina a validar.
   * @throws IllegalArgumentException si ya existe una nómina.
   */
  private void validateNominaNoExiste(Nomina nomina) {
    if (nomina.getId() == null
        && nominaRepository
            .findByEmpleadoAndPeriodo(
                nomina.getEmpleado().getId(), nomina.getMes().byteValue(), nomina.getAnio())
            .isPresent()) {
      throw new IllegalArgumentException(
          "Ya existe una nómina para el empleado "
              + nomina.getEmpleado().getId()
              + " en el periodo "
              + nomina.getMes()
              + "/"
              + nomina.getAnio());
    }
  }

  /**
   * Valida el mes.
   *
   * @param mes Mes a validar (1-12).
   * @throws IllegalArgumentException si el mes no es válido.
   */
  private void validateMes(Integer mes) {
    if (mes == null) {
      throw new IllegalArgumentException("El mes es obligatorio");
    }
    if (mes < 1 || mes > 12) {
      throw new IllegalArgumentException("El mes debe estar entre 1 y 12");
    }
  }

  /**
   * Valida el año.
   *
   * @param anio Año a validar.
   * @throws IllegalArgumentException si el año no es válido.
   */
  private void validateAnio(Integer anio) {
    if (anio == null) {
      throw new IllegalArgumentException("El año es obligatorio");
    }
    int currentYear = LocalDate.now().getYear();
    if (anio < currentYear - 5 || anio > currentYear + 1) {
      throw new IllegalArgumentException(
          "El año debe estar entre " + (currentYear - 5) + " y " + (currentYear + 1));
    }
  }

  /**
   * Valida los montos de la nómina.
   *
   * @param nomina Nómina a validar.
   * @throws IllegalArgumentException si los montos no son válidos.
   */
  private void validateMontos(Nomina nomina) {
    validateSalarioBase(nomina.getSalarioBase());
    validateMonto("Bonificaciones", nomina.getBonificaciones());
    validateMonto("Deducciones", nomina.getDeducciones());
  }

  /**
   * Valida el salario base.
   *
   * @param salarioBase Salario base a validar.
   * @throws IllegalArgumentException si el salario base no es válido.
   */
  private void validateSalarioBase(BigDecimal salarioBase) {
    if (salarioBase == null) {
      throw new IllegalArgumentException("El salario base es obligatorio");
    }
    if (salarioBase.compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("El salario base debe ser mayor a 0");
    }
    if (salarioBase.compareTo(new BigDecimal("999999.99")) > 0) {
      throw new IllegalArgumentException("El salario base no puede exceder 999,999.99");
    }
  }

  /**
   * Valida un monto (bonificaciones o deducciones).
   *
   * @param nombre Nombre del monto.
   * @param monto Monto a validar.
   * @throws IllegalArgumentException si el monto no es válido.
   */
  private void validateMonto(String nombre, BigDecimal monto) {
    if (monto != null) {
      if (monto.compareTo(BigDecimal.ZERO) < 0) {
        throw new IllegalArgumentException(nombre + " no puede ser negativo");
      }
      if (monto.compareTo(new BigDecimal("999999.99")) > 0) {
        throw new IllegalArgumentException(nombre + " no puede exceder 999,999.99");
      }
    }
  }

  /**
   * Valida las observaciones.
   *
   * @param observaciones Observaciones a validar.
   * @throws IllegalArgumentException si las observaciones no son válidas.
   */
  private void validateObservaciones(String observaciones) {
    if (observaciones != null && observaciones.length() > 5000) {
      throw new IllegalArgumentException("Las observaciones no pueden exceder 5000 caracteres");
    }
  }

  /**
   * Valida el cambio de estado de una nómina.
   *
   * @param nomina Nómina actual.
   * @param nuevoEstado Nuevo estado.
   * @throws IllegalArgumentException si el cambio de estado no es válido.
   */
  private void validateCambioEstado(Nomina nomina, EstadoNomina nuevoEstado) {
    EstadoNomina estadoActual = nomina.getEstado();

    // No se puede cambiar el estado de una nómina anulada
    if (estadoActual == EstadoNomina.ANULADA) {
      throw new IllegalArgumentException("No se puede cambiar el estado de una nómina anulada");
    }

    // No se puede volver a estado anterior (excepto anular)
    if (nuevoEstado != EstadoNomina.ANULADA && estadoActual == EstadoNomina.PAGADA) {
      throw new IllegalArgumentException("No se puede cambiar el estado de una nómina ya pagada");
    }
  }

  /**
   * Establece valores por defecto para montos.
   *
   * @param nomina Nómina a configurar.
   */
  private void setDefaultMontos(Nomina nomina) {
    if (nomina.getBonificaciones() == null) {
      nomina.setBonificaciones(BigDecimal.ZERO);
    }
    if (nomina.getDeducciones() == null) {
      nomina.setDeducciones(BigDecimal.ZERO);
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
   * Valida que una nómina existe.
   *
   * @param id ID de la nómina.
   * @throws IllegalArgumentException si la nómina no existe.
   */
  private void validateNominaExists(int id) {
    if (!nominaRepository.existsById(id)) {
      throw new IllegalArgumentException("Nómina no encontrada con ID: " + id);
    }
  }

  /**
   * Valida el ID de una nómina para actualización.
   *
   * @param nomina Nómina a validar.
   * @throws IllegalArgumentException si el ID no es válido.
   */
  private void validateUpdateId(Nomina nomina) {
    if (nomina.getId() == null || nomina.getId() <= 0) {
      throw new IllegalArgumentException("ID de nómina inválido");
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
