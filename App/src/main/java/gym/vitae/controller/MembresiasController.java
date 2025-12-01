package gym.vitae.controller;

import static gym.vitae.controller.ValidationUtils.*;

import gym.vitae.mapper.MembresiaMapper;
import gym.vitae.model.Cliente;
import gym.vitae.model.Factura;
import gym.vitae.model.Membresia;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;
import gym.vitae.model.enums.EstadoMembresia;
import gym.vitae.repositories.ClienteRepository;
import gym.vitae.repositories.FacturaRepository;
import gym.vitae.repositories.MembresiaRepository;
import gym.vitae.repositories.TiposMembresiaRepository;
import java.util.List;

public class MembresiasController extends BaseController {

  private final MembresiaRepository membresiaRepository;
  private final ClienteRepository clienteRepository;
  private final TiposMembresiaRepository tiposMembresiaRepository;
  private final FacturaRepository facturaRepository;

  public MembresiasController() {
    super();
    this.membresiaRepository = getRepository(MembresiaRepository.class);
    this.clienteRepository = getRepository(ClienteRepository.class);
    this.tiposMembresiaRepository = getRepository(TiposMembresiaRepository.class);
    this.facturaRepository = getRepository(FacturaRepository.class);
  }

  MembresiasController(
      MembresiaRepository membresiaRepository,
      ClienteRepository clienteRepository,
      TiposMembresiaRepository tiposMembresiaRepository,
      FacturaRepository facturaRepository) {
    super(null);
    this.membresiaRepository = membresiaRepository;
    this.clienteRepository = clienteRepository;
    this.tiposMembresiaRepository = tiposMembresiaRepository;
    this.facturaRepository = facturaRepository;
  }

  // =====================================================
  // LISTADOS
  // =====================================================

  public List<MembresiaListadoDTO> getMembresias() {
    return membresiaRepository.findAllListado();
  }

  public MembresiaDetalleDTO getMembresiaById(int id) {
    validateId(id);
    return membresiaRepository
        .findDetalleById(id)
        .orElseThrow(() -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));
  }

  // =====================================================
  // CREAR MEMBRESÍA
  // =====================================================

  public MembresiaDetalleDTO createMembresia(MembresiaCreateDTO dto) {
    if (dto == null) {
      throw new IllegalArgumentException("Los datos de la membresía no pueden ser nulos");
    }

    validateId(dto.getClienteId());
    validateId(dto.getTipoMembresiaId());
    validateId(dto.getFacturaId());

    if (dto.getFechaInicio() == null || dto.getFechaFin() == null) {
      throw new IllegalArgumentException("Las fechas de inicio y fin son obligatorias");
    }

    if (dto.getPrecioPagado() == null || dto.getPrecioPagado().doubleValue() <= 0) {
      throw new IllegalArgumentException("El precio pagado debe ser mayor a 0");
    }

    // ===================== Cargar entidades ======================
    Cliente cliente =
        clienteRepository
            .findById(dto.getClienteId())
            .orElseThrow(
                () -> new IllegalArgumentException("Cliente no encontrado con ID: " + dto.getClienteId()));

    TiposMembresia tipo =
        tiposMembresiaRepository
            .findById(dto.getTipoMembresiaId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Tipo de membresía no encontrado con ID: " + dto.getTipoMembresiaId()));

    Factura factura =
        facturaRepository
            .findById(dto.getFacturaId())
            .orElseThrow(
                () ->
                    new IllegalArgumentException(
                        "Factura no encontrada con ID: " + dto.getFacturaId()));

    // ===================== Crear entidad ======================
    Membresia membresia = MembresiaMapper.toEntity(dto, cliente, tipo, factura);
    Membresia saved = membresiaRepository.save(membresia);

    return membresiaRepository
        .findDetalleById(saved.getId())
        .orElseThrow(() -> new IllegalStateException("Error al recuperar la membresía creada"));
  }

  // =====================================================
  // ACTUALIZAR MEMBRESÍA
  // =====================================================

  public MembresiaDetalleDTO updateMembresia(int id, MembresiaUpdateDTO dto) {
    validateId(id);

    if (dto == null) {
      throw new IllegalArgumentException("Los datos actualizados no pueden ser nulos");
    }

    Membresia membresia =
        membresiaRepository
            .findById(id)
            .orElseThrow(
                () -> new IllegalArgumentException("Membresía no encontrada con ID: " + id));

    // Aplicar cambios
    MembresiaMapper.updateEntity(membresia, dto);
    membresiaRepository.update(membresia);

    return membresiaRepository
        .findDetalleById(id)
        .orElseThrow(
            () -> new IllegalStateException("Error al recuperar la membresía actualizada"));
  }

  // =====================================================
  // CANCELAR MEMBRESÍA (Soft Delete)
  // =====================================================

  public void cancelarMembresia(int id) {
    validateId(id);

    if (!membresiaRepository.existsById(id)) {
      throw new IllegalArgumentException("Membresía no encontrada con ID: " + id);
    }

    membresiaRepository.delete(id); // cambia estado a CANCELADA
  }

  public List<MembresiaListadoDTO> getPaged(int page, int size) {
    var all = getMembresias();
    int from = Math.min(page * size, all.size());
    int to = Math.min(from + size, all.size());
    return all.subList(from, to);
  }

}
