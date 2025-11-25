package gym.vitae.mapper;

import gym.vitae.model.Factura;
import gym.vitae.model.Pago;
import gym.vitae.model.dtos.facturacion.PagoCreateDTO;
import gym.vitae.model.dtos.facturacion.PagoDetalleDTO;
import gym.vitae.model.dtos.facturacion.PagoListadoDTO;
import gym.vitae.model.dtos.facturacion.PagoUpdateDTO;
import java.util.List;

public class PagoMapper {

  private PagoMapper() {}

  public static PagoListadoDTO toListadoDTO(Pago pago) {
    if (pago == null) return null;

    return new PagoListadoDTO(
        pago.getId(),
        pago.getMonto(),
        pago.getFechaPago(),
        pago.getReferencia(),
        pago.getEstado(),
        pago.getFactura() != null ? pago.getFactura().getId() : null,
        pago.getFactura() != null ? pago.getFactura().getNumeroFactura() : null);
  }

  public static PagoDetalleDTO toDetalleDTO(Pago pago) {
    if (pago == null) return null;

    PagoDetalleDTO dto = new PagoDetalleDTO();
    dto.setId(pago.getId());
    dto.setMonto(pago.getMonto());
    dto.setFechaPago(pago.getFechaPago());
    dto.setReferencia(pago.getReferencia());
    dto.setEstado(pago.getEstado());
    dto.setObservaciones(pago.getObservaciones());
    dto.setCreatedAt(pago.getCreatedAt());
    dto.setUpdatedAt(pago.getUpdatedAt());

    if (pago.getFactura() != null) {
      dto.setFacturaId(pago.getFactura().getId());
      dto.setFacturaNumero(pago.getFactura().getNumeroFactura());
    }

    return dto;
  }

  public static Pago toEntity(PagoCreateDTO dto, Factura factura) {
    if (dto == null) return null;

    Pago pago = new Pago();
    pago.setFactura(factura);
    pago.setMonto(dto.getMonto());
    pago.setFechaPago(dto.getFechaPago());
    pago.setReferencia(dto.getReferencia());
    pago.setObservaciones(dto.getObservaciones());

    return pago;
  }

  public static void updateEntity(Pago pago, PagoUpdateDTO dto) {
    if (pago == null || dto == null) return;

    if (dto.getEstado() != null) {
      pago.setEstado(dto.getEstado());
    }
    if (dto.getReferencia() != null) {
      pago.setReferencia(dto.getReferencia());
    }
    if (dto.getObservaciones() != null) {
      pago.setObservaciones(dto.getObservaciones());
    }
  }

  public static List<PagoListadoDTO> toListadoDTOList(List<Pago> pagos) {
    if (pagos == null) return List.of();
    return pagos.stream().map(PagoMapper::toListadoDTO).toList();
  }
}
