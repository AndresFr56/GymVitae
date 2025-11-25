package gym.vitae.mapper;

import gym.vitae.model.DetallesFactura;
import gym.vitae.model.Equipo;
import gym.vitae.model.Factura;
import gym.vitae.model.Iva;
import gym.vitae.model.Producto;
import gym.vitae.model.TiposMembresia;
import gym.vitae.model.dtos.facturacion.DetalleFacturaCreateDTO;
import gym.vitae.model.dtos.facturacion.DetalleFacturaDTO;
import java.util.List;

public class DetalleFacturaMapper {

  private DetalleFacturaMapper() {}

  public static DetalleFacturaDTO toDTO(DetallesFactura detalle) {
    if (detalle == null) return null;

    DetalleFacturaDTO dto = new DetalleFacturaDTO();
    dto.setId(detalle.getId());
    dto.setCantidad(detalle.getCantidad());
    dto.setPrecioUnitario(detalle.getPrecioUnitario());
    dto.setSubtotal(detalle.getSubtotal());
    dto.setAplicaIva(detalle.getAplicaIva());

    // IVA info
    if (detalle.getIva() != null) {
      dto.setIvaPorcentaje(detalle.getIva().getPorcentaje());
    }

    // Tipo Membresía info
    if (detalle.getTipoMembresia() != null) {
      dto.setTipoMembresiaId(detalle.getTipoMembresia().getId());
      dto.setTipoMembresiaNombre(detalle.getTipoMembresia().getNombre());
    }

    // Producto info
    if (detalle.getProducto() != null) {
      dto.setProductoId(detalle.getProducto().getId());
      dto.setProductoNombre(detalle.getProducto().getNombre());
    }

    // Equipo info
    if (detalle.getEquipo() != null) {
      dto.setEquipoId(detalle.getEquipo().getId());
      dto.setEquipoNombre(detalle.getEquipo().getNombre());
    }

    return dto;
  }

  public static DetallesFactura toEntity(
      DetalleFacturaCreateDTO dto,
      Factura factura,
      Iva iva,
      TiposMembresia tipoMembresia,
      Producto producto,
      Equipo equipo) {
    if (dto == null) return null;

    DetallesFactura detalle = new DetallesFactura();
    detalle.setFactura(factura);
    detalle.setCantidad(dto.getCantidad());
    detalle.setPrecioUnitario(dto.getPrecioUnitario());
    detalle.setAplicaIva(dto.getAplicaIva());
    detalle.setIva(iva);
    detalle.setTipoMembresia(tipoMembresia);
    detalle.setProducto(producto);
    detalle.setEquipo(equipo);

    return detalle;
  }

  public static List<DetalleFacturaDTO> toDTOList(List<DetallesFactura> detalles) {
    if (detalles == null) return List.of();
    return detalles.stream().map(DetalleFacturaMapper::toDTO).toList();
  }
}
