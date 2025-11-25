package gym.vitae.mapper;

import gym.vitae.model.Cliente;
import gym.vitae.model.Empleado;
import gym.vitae.model.Factura;
import gym.vitae.model.dtos.facturacion.DetalleFacturaDTO;
import gym.vitae.model.dtos.facturacion.FacturaCreateDTO;
import gym.vitae.model.dtos.facturacion.FacturaDetalleDTO;
import gym.vitae.model.dtos.facturacion.FacturaListadoDTO;
import gym.vitae.model.dtos.facturacion.FacturaUpdateDTO;
import java.time.LocalDate;
import java.util.List;

public class FacturaMapper {

  private FacturaMapper() {}

  public static FacturaListadoDTO toListadoDTO(Factura factura) {
    if (factura == null) return null;

    return new FacturaListadoDTO(
        factura.getId(),
        factura.getNumeroFactura(),
        factura.getFechaEmision(),
        factura.getTipoVenta(),
        factura.getTotal(),
        factura.getEstado(),
        factura.getCliente() != null ? factura.getCliente().getNombres() : null,
        factura.getEmpleadoResponsable() != null
            ? factura.getEmpleadoResponsable().getNombres()
            : null);
  }

  public static FacturaDetalleDTO toDetalleDTO(Factura factura) {
    if (factura == null) return null;

    FacturaDetalleDTO dto = new FacturaDetalleDTO();
    dto.setId(factura.getId());
    dto.setNumeroFactura(factura.getNumeroFactura());
    dto.setFechaEmision(factura.getFechaEmision());
    dto.setTipoVenta(factura.getTipoVenta());
    dto.setTotal(factura.getTotal());
    dto.setEstado(factura.getEstado());
    dto.setObservaciones(factura.getObservaciones());
    dto.setCreatedAt(factura.getCreatedAt());
    dto.setUpdatedAt(factura.getUpdatedAt());

    // Cliente info
    if (factura.getCliente() != null) {
      dto.setClienteId(factura.getCliente().getId());
      dto.setClienteNombre(factura.getCliente().getNombres());
      dto.setClienteDocumento(factura.getCliente().getCedula());
    }

    // Empleado info
    if (factura.getEmpleadoResponsable() != null) {
      dto.setEmpleadoId(factura.getEmpleadoResponsable().getId());
      dto.setEmpleadoNombre(factura.getEmpleadoResponsable().getNombres());
    }

    // Detalles
    if (factura.getDetalles() != null) {
      List<DetalleFacturaDTO> detalles =
          factura.getDetalles().stream().map(DetalleFacturaMapper::toDTO).toList();
      dto.setDetalles(detalles);
    }

    return dto;
  }

  public static Factura toEntity(
      FacturaCreateDTO dto, Cliente cliente, Empleado empleado, String numeroFactura) {
    if (dto == null) return null;

    Factura factura = new Factura();
    factura.setNumeroFactura(numeroFactura);
    factura.setCliente(cliente);
    factura.setEmpleadoResponsable(empleado);
    factura.setFechaEmision(LocalDate.now());
    factura.setTipoVenta(dto.getTipoVenta());
    factura.setObservaciones(dto.getObservaciones());

    return factura;
  }

  public static void updateEntity(Factura factura, FacturaUpdateDTO dto) {
    if (factura == null || dto == null) return;

    if (dto.getEstado() != null) {
      factura.setEstado(dto.getEstado());
    }
    if (dto.getObservaciones() != null) {
      factura.setObservaciones(dto.getObservaciones());
    }
  }

  public static List<FacturaListadoDTO> toListadoDTOList(List<Factura> facturas) {
    if (facturas == null) return List.of();
    return facturas.stream().map(FacturaMapper::toListadoDTO).toList();
  }
}
