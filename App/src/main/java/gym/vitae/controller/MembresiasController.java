package gym.vitae.controller;

import gym.vitae.mapper.MembresiaMapper;
import gym.vitae.model.*;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import gym.vitae.model.enums.EstadoFactura;
import gym.vitae.model.enums.TipoVenta;
import gym.vitae.repositories.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Random;

import static gym.vitae.controller.ValidationUtils.validateFechaSalida;
import static gym.vitae.controller.ValidationUtils.validateId;

/**
 * Controlador para gestionar membresías.
 *
 * @author GymVitae
 */
public class MembresiasController extends BaseController {

  private final MembresiaRepository membresiaRepository;
  private final ClienteRepository clienteRepository;
  private final TiposMembresiaRepository tiposMembresiaRepository;
  private final FacturaRepository facturaRepository;
  private final DetallesFacturaRepository detallesFacturaRepository;
  private final AuthController authController;

  /** Constructor por defecto. */
  public MembresiasController() {
    super();
    this.membresiaRepository = getRepository(MembresiaRepository.class);
    this.clienteRepository = getRepository(ClienteRepository.class);
    this.tiposMembresiaRepository = getRepository(TiposMembresiaRepository.class);
    this.facturaRepository = getRepository(FacturaRepository.class);
    this.detallesFacturaRepository = getRepository(DetallesFacturaRepository.class);
    this.authController = new AuthController();
  }

  /**
   * Constructor para pruebas.
   *
   * @param membresiaRepository Repositorio de Membresia.
   * @param clienteRepository Repositorio de Cliente.
   * @param tiposMembresiaRepository Repositorio de TiposMembresia.
   * @param facturaRepository Repositorio de Factura.
   * @param detallesFacturaRepository Repositorio de DetallesFactura.
   */
  MembresiasController(
      MembresiaRepository membresiaRepository,
      ClienteRepository clienteRepository,
      TiposMembresiaRepository tiposMembresiaRepository,
      FacturaRepository facturaRepository,
      DetallesFacturaRepository detallesFacturaRepository,
      AuthController authController) {
    super(null);
    this.membresiaRepository = membresiaRepository;
    this.clienteRepository = clienteRepository;
    this.tiposMembresiaRepository = tiposMembresiaRepository;
    this.facturaRepository = facturaRepository;
    this.detallesFacturaRepository = detallesFacturaRepository;
    this.authController = authController;
  }

  /**
   * Listado de Membresias.
   *
   * @return Lista de Membresias.
   */
  public List<MembresiaListadoDTO> getMembresias() {
    return membresiaRepository.findAllListado();
  }

  /**
   * Obtiene una membresía por su ID.
   *
   * @param id ID de la membresía.
   * @return Detalle de la membresía.
   * @throws IllegalArgumentException si la membresía no se encuentra.
   */
  public MembresiaDetalleDTO getMembresiaById(int id) {
    validateId(id);
    return membresiaRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));
  }

  /**
   * Crea una nueva membresía.
   *
   * @param dto Datos de la membresía a crear.
   * @return Detalle de la membresía creada.
   * @throws IllegalArgumentException si los datos son inválidos.
   * @throws IllegalStateException si ocurre un error al recuperar la membresía creada.
   */
  public MembresiaDetalleDTO createMembresia(MembresiaCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos de la membresía no pueden ser nulos");
    }

    validateId(dto.getClienteId());
    validateId(dto.getTipoMembresiaId());

    if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
      throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
    }

    validateFechaSalida(dto.getFechaInicio(), dto.getFechaFin());

    if (dto.getPrecioPagado() == null || dto.getPrecioPagado().doubleValue() <= 0) {
      throw new IllegalArgumentException("El precio pagado debe ser mayor a 0");
    }

    Cliente cliente =
        clienteRepository
            .findById(dto.getClienteId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Cliente no encontrado con ID: " + dto.getClienteId()));
    final TiposMembresia tipo =
        tiposMembresiaRepository
            .findById(dto.getTipoMembresiaId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Tipo de membresía no encontrado con ID: " + dto.getTipoMembresiaId()));

    Empleado empleadoResponsable = authController.getEmpleadoActual();

    if (empleadoResponsable == null) {
      throw new IllegalStateException("No hay un empleado logueado para registrar la venta");
    }

    Factura factura = new Factura();
    String numeroFactura =
        "FAC-" + LocalDate.now().toString().replace("-", "") + new Random().nextInt(10000);

    factura.setNumeroFactura(numeroFactura);
    factura.setCliente(cliente);
    factura.setEmpleadoResponsable(empleadoResponsable);
    factura.setFechaEmision(LocalDate.now());
    factura.setTipoVenta(TipoVenta.MEMBRESIA);
    factura.setTotal(dto.getPrecioPagado());
    factura.setEstado(EstadoFactura.PAGADA);

    Factura savedFactura = facturaRepository.save(factura);

    final Integer nuevaFacturaId = savedFactura.getId();

    DetallesFactura detalle = new DetallesFactura();
    detalle.setFactura(savedFactura);
    detalle.setTipoMembresia(tipo);
    detalle.setCantidad(1);
    detalle.setPrecioUnitario(dto.getPrecioPagado());
    detalle.setSubtotal(dto.getPrecioPagado());

    detallesFacturaRepository.save(detalle);

    dto.setFacturaId(nuevaFacturaId);

    Membresia membresia = MembresiaMapper.toEntity(dto, cliente, tipo, savedFactura);
    Membresia saved = membresiaRepository.save(membresia);

    return membresiaRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar la membresía creada"));
  }

  /**
   * Actualiza una membresía existente.
   *
   * @param id ID de la membresía a actualizar.
   * @param dto Datos actualizados de la membresía.
   * @return Detalle de la membresía actualizada.
   * @throws IllegalArgumentException si los datos son inválidos o la membresía no se encuentra.
   * @throws IllegalStateException si ocurre un error al recuperar la membresía actualizada.
   */
  public MembresiaDetalleDTO updateMembresia(int id, MembresiaUpdateDTO dto) {
    validateId(id);

    if (dto == null) {
      throw new IllegalArgumentException("Los datos actualizados no pueden ser nulos");
    }

    Membresia membresia = membresiaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));

    if (dto.getTipoMembresiaId() != null) {
      TiposMembresia nuevoTipo = tiposMembresiaRepository.findById(dto.getTipoMembresiaId())
              .orElseThrow(() -> new IllegalArgumentException("Tipo de membresía no encontrado"));
      membresia.setTipoMembresia(nuevoTipo);
    }

    MembresiaMapper.updateEntity(membresia, dto);
    membresiaRepository.update(membresia);

    return membresiaRepository.findDetalleById(id)
            .orElseThrow(() -> new IllegalStateException("Error al recuperar la membresía actualizada"));
  }

  /**
   * Cancela una membresía existente.
   *
   * @param id ID de la membresía a cancelar.
   * @throws IllegalArgumentException si la membresía no se encuentra.
   */
  public void cancelarMembresia(int id) {
    validateId(id);

    Membresia membresia = membresiaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));

    // Cambiamos el estado a CANCELADO (Eliminación lógica tipo Netflix)
    membresia.setEstado(gym.vitae.model.enums.EstadoMembresia.CANCELADA);

    // Usamos update en lugar de delete para persistir el cambio de estado
    membresiaRepository.update(membresia);
  }

  /**
   * Obtiene una página de membresías.
   *
   * @param page Número de página (0-indexado).
   * @param size Tamaño de la página.
   * @return Lista de membresías en la página solicitada.
   * @throws IllegalArgumentException si los parámetros de paginación son inválidos.
   */
  public List<MembresiaListadoDTO> getPaged(int page, int size) {
    var all = getMembresias();
    int from = Math.min(page * size, all.size());
    int to = Math.min(from + size, all.size());
    return all.subList(from, to);
  }

  /**
   * Cambia el estado de una membresía de forma específica.
   * Útil para la lógica de suspensión.
   *
   * @param id          ID de la membresía.
   * @param nuevoEstado El estado al que se desea cambiar.
   */
  public void cambiarEstado(int id, gym.vitae.model.enums.EstadoMembresia nuevoEstado) {
    validateId(id);

    Membresia membresia = membresiaRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));

    membresia.setEstado(nuevoEstado);
    membresiaRepository.update(membresia);
  }


}
