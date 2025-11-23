package gym.vitae.controller;

import gym.vitae.model.Cliente;
import gym.vitae.model.Horario;
import gym.vitae.model.InscripcionesClase;
import gym.vitae.model.enums.EstadoInscripcion;
import gym.vitae.repositories.ClienteRepository;
import gym.vitae.repositories.HorarioRepository;
import gym.vitae.repositories.InscripcionesClaseRepository;
import java.time.LocalDate;
import java.util.List;

/** Controlador de Inscripciones a Clases. */
public class InscripcionesClaseController extends BaseController {

  private final InscripcionesClaseRepository inscripcionesRepository;
  private final HorarioRepository horarioRepository;
  private final ClienteRepository clienteRepository;

  public InscripcionesClaseController() {
    super();
    this.inscripcionesRepository = getRepository(InscripcionesClaseRepository.class);
    this.horarioRepository = getRepository(HorarioRepository.class);
    this.clienteRepository = getRepository(ClienteRepository.class);
  }

  /**
   * Constructor para pruebas.
   *
   * @param inscripcionesRepository Repositorio de inscripciones.
   * @param horarioRepository Repositorio de horarios.
   * @param clienteRepository Repositorio de clientes.
   */
  InscripcionesClaseController(
      InscripcionesClaseRepository inscripcionesRepository,
      HorarioRepository horarioRepository,
      ClienteRepository clienteRepository) {
    super(null);
    this.inscripcionesRepository = inscripcionesRepository;
    this.horarioRepository = horarioRepository;
    this.clienteRepository = clienteRepository;
  }

  /**
   * Obtiene todas las inscripciones.
   *
   * @return Lista de inscripciones.
   */
  public List<InscripcionesClase> getInscripciones() {
    return inscripcionesRepository.findAll();
  }

  /**
   * Obtiene inscripciones con paginación.
   *
   * @param offset Posición inicial.
   * @param limit Cantidad de registros.
   * @return Lista de inscripciones paginada.
   */
  public List<InscripcionesClase> getInscripciones(int offset, int limit) {
    validatePagination(offset, limit);
    return inscripcionesRepository.findAll(offset, limit);
  }

  /**
   * Obtiene una inscripción por ID.
   *
   * @param id ID de la inscripción.
   * @return Inscripción encontrada.
   * @throws IllegalArgumentException si no se encuentra la inscripción.
   */
  public InscripcionesClase getInscripcionById(int id) {
    validateId(id);
    return inscripcionesRepository
        .findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Inscripción no encontrada con ID: " + id));
  }

  /**
   * Crea una nueva inscripción a clase.
   *
   * @param inscripcion Inscripción a crear.
   * @return Inscripción creada.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public InscripcionesClase createInscripcion(InscripcionesClase inscripcion) {
    validateInscripcion(inscripcion);
    validateHorario(inscripcion);
    validateCliente(inscripcion);

    // Establecer estado inicial
    if (inscripcion.getEstado() == null) {
      inscripcion.setEstado(EstadoInscripcion.ACTIVA);
    }

    // Establecer fecha de inscripción si no está presente
    if (inscripcion.getFechaInscripcion() == null) {
      inscripcion.setFechaInscripcion(LocalDate.now());
    }

    return inscripcionesRepository.save(inscripcion);
  }

  /**
   * Actualiza una inscripción existente.
   *
   * @param inscripcion Inscripción con datos actualizados.
   * @return true si se actualizó correctamente.
   * @throws IllegalArgumentException si las validaciones fallan.
   */
  public boolean updateInscripcion(InscripcionesClase inscripcion) {
    validateUpdateId(inscripcion);
    validateInscripcionExists(inscripcion.getId());
    validateInscripcion(inscripcion);
    validateHorario(inscripcion);
    validateCliente(inscripcion);

    return inscripcionesRepository.update(inscripcion);
  }

  /**
   * Cancela una inscripción (soft delete).
   *
   * @param id ID de la inscripción a cancelar.
   * @throws IllegalArgumentException si la inscripción no existe.
   */
  public void cancelarInscripcion(int id) {
    validateId(id);
    validateInscripcionExists(id);
    inscripcionesRepository.delete(id);
  }

  /**
   * Obtiene el total de inscripciones.
   *
   * @return Cantidad total de inscripciones.
   */
  public long countInscripciones() {
    return inscripcionesRepository.count();
  }

  /**
   * Cambia el estado de una inscripción.
   *
   * @param id ID de la inscripción.
   * @param nuevoEstado Nuevo estado.
   * @return true si se actualizó correctamente.
   */
  public boolean cambiarEstadoInscripcion(int id, EstadoInscripcion nuevoEstado) {
    if (nuevoEstado == null) {
      throw new IllegalArgumentException("El estado no puede ser nulo");
    }
    InscripcionesClase inscripcion = getInscripcionById(id);
    inscripcion.setEstado(nuevoEstado);
    return inscripcionesRepository.update(inscripcion);
  }

  /**
   * Valida los datos de una inscripción.
   *
   * @param inscripcion Inscripción a validar.
   * @throws IllegalArgumentException si alguna validación falla.
   */
  private void validateInscripcion(InscripcionesClase inscripcion) {
    if (inscripcion == null) {
      throw new IllegalArgumentException("La inscripción no puede ser nula");
    }

    validateFechaInscripcion(inscripcion.getFechaInscripcion());
  }

  /**
   * Valida y establece el horario de la inscripción.
   *
   * @param inscripcion Inscripción a validar.
   * @throws IllegalArgumentException si el horario no es válido.
   */
  private void validateHorario(InscripcionesClase inscripcion) {
    if (inscripcion.getHorario() == null || inscripcion.getHorario().getId() == null) {
      throw new IllegalArgumentException("El horario es obligatorio");
    }

    Horario horario =
        horarioRepository
            .findById(inscripcion.getHorario().getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Horario no encontrado con ID: " + inscripcion.getHorario().getId()));

    if (!horario.getActivo()) {
      throw new IllegalArgumentException("El horario no está activo");
    }

    inscripcion.setHorario(horario);
  }

  /**
   * Valida y establece el cliente de la inscripción.
   *
   * @param inscripcion Inscripción a validar.
   * @throws IllegalArgumentException si el cliente no es válido.
   */
  private void validateCliente(InscripcionesClase inscripcion) {
    if (inscripcion.getCliente() == null || inscripcion.getCliente().getId() == null) {
      throw new IllegalArgumentException("El cliente es obligatorio");
    }

    Cliente cliente =
        clienteRepository
            .findById(inscripcion.getCliente().getId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Cliente no encontrado con ID: " + inscripcion.getCliente().getId()));

    inscripcion.setCliente(cliente);
  }

  /**
   * Valida la fecha de inscripción.
   *
   * @param fechaInscripcion Fecha a validar.
   * @throws IllegalArgumentException si la validación falla.
   */
  private void validateFechaInscripcion(LocalDate fechaInscripcion) {
    if (fechaInscripcion != null && fechaInscripcion.isAfter(LocalDate.now().plusDays(30))) {
      throw new IllegalArgumentException(
          "La fecha de inscripción no puede ser más de 30 días en el futuro");
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
   * Valida que una inscripción existe.
   *
   * @param id ID de la inscripción.
   * @throws IllegalArgumentException si la inscripción no existe.
   */
  private void validateInscripcionExists(int id) {
    if (!inscripcionesRepository.existsById(id)) {
      throw new IllegalArgumentException("Inscripción no encontrada con ID: " + id);
    }
  }

  /**
   * Valida el ID de una inscripción para actualización.
   *
   * @param inscripcion Inscripción a validar.
   * @throws IllegalArgumentException si el ID no es válido.
   */
  private void validateUpdateId(InscripcionesClase inscripcion) {
    if (inscripcion.getId() == null || inscripcion.getId() <= 0) {
      throw new IllegalArgumentException("ID de inscripción inválido");
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
