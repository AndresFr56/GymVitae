package gym.vitae.mapper;

import gym.vitae.model.Cliente;
import gym.vitae.model.Factura;
import gym.vitae.model.Membresia;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.membresias.MembresiaCreateDTO;
import gym.vitae.model.dtos.membresias.MembresiaDetalleDTO;
import gym.vitae.model.dtos.membresias.MembresiaListadoDTO;
import gym.vitae.model.dtos.membresias.MembresiaUpdateDTO;

import java.util.List;

public class MembresiaMapper {

  private MembresiaMapper() {}

  public static MembresiaListadoDTO toListadoDTO(Membresia membresia) {
    if (membresia == null) return null;

    return new MembresiaListadoDTO(
        membresia.getId(),
        membresia.getFechaInicio(),
        membresia.getFechaFin(),
        membresia.getPrecioPagado(),
        membresia.getEstado(),
        membresia.getCliente() != null ? membresia.getCliente().getNombres() : null,
        membresia.getCliente() != null ? membresia.getCliente().getCodigoCliente() : null,
        membresia.getTipoMembresia() != null ? membresia.getTipoMembresia().getNombre() : null);
  }

  public static MembresiaDetalleDTO toDetalleDTO(Membresia membresia) {
    if (membresia == null) return null;

    MembresiaDetalleDTO dto = new MembresiaDetalleDTO();
    dto.setId(membresia.getId());
    dto.setFechaInicio(membresia.getFechaInicio());
    dto.setFechaFin(membresia.getFechaFin());
    dto.setPrecioPagado(membresia.getPrecioPagado());
    dto.setEstado(membresia.getEstado());
    dto.setObservaciones(membresia.getObservaciones());
    dto.setCreatedAt(membresia.getCreatedAt());
    dto.setUpdatedAt(membresia.getUpdatedAt());

    // Cliente info
    if (membresia.getCliente() != null) {
      dto.setClienteId(membresia.getCliente().getId());
      dto.setClienteNombre(membresia.getCliente().getNombres());
      dto.setClienteDocumento(membresia.getCliente().getCedula());
    }

    // Tipo Membresía info
    if (membresia.getTipoMembresia() != null) {
      dto.setTipoMembresiaId(membresia.getTipoMembresia().getId());
      dto.setTipoMembresiaNombre(membresia.getTipoMembresia().getNombre());
    }

    // Factura info
    if (membresia.getFactura() != null) {
      dto.setFacturaId(membresia.getFactura().getId());
      dto.setFacturaNumero(membresia.getFactura().getNumeroFactura());
    }

    return dto;
  }

  public static Membresia toEntity(
      MembresiaCreateDTO dto, Cliente cliente, TiposMembresia tipoMembresia, Factura factura) {
    if (dto == null) return null;

    Membresia membresia = new Membresia();
    membresia.setCliente(cliente);
    membresia.setTipoMembresia(tipoMembresia);
    membresia.setFactura(factura);
    membresia.setFechaInicio(dto.getFechaInicio());
    membresia.setFechaFin(dto.getFechaFin());
    membresia.setPrecioPagado(dto.getPrecioPagado());
    membresia.setObservaciones(dto.getObservaciones());

    return membresia;
  }

  public static void updateEntity(Membresia membresia, MembresiaUpdateDTO dto) {
    if (membresia == null || dto == null) return;

    if (dto.getEstado() != null) {
      membresia.setEstado(dto.getEstado());
    }
    if (dto.getFechaFin() != null) {
      membresia.setFechaFin(dto.getFechaFin());
    }
    if (dto.getObservaciones() != null) {
      membresia.setObservaciones(dto.getObservaciones());
    }
    if (dto.getPrecioPagado() != null) {
      membresia.setPrecioPagado(dto.getPrecioPagado());
    }
  }

  public static List<MembresiaListadoDTO> toListadoDTOList(List<Membresia> membresias) {
    if (membresias == null) return List.of();
    return membresias.stream().map(MembresiaMapper::toListadoDTO).toList();
  }
}
