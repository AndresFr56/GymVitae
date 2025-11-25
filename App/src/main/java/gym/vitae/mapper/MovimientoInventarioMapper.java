package gym.vitae.mapper;

import gym.vitae.model.MovimientosInventario;
import gym.vitae.model.dtos.inventario.MovimientoInventarioDetalleDTO;
import gym.vitae.model.dtos.inventario.MovimientoInventarioListadoDTO;
import java.util.List;

public class MovimientoInventarioMapper {

  private MovimientoInventarioMapper() {}

  public static MovimientoInventarioListadoDTO toListadoDTO(MovimientosInventario movimiento) {
    if (movimiento == null) return null;

    return new MovimientoInventarioListadoDTO(
        movimiento.getId(),
        movimiento.getTipoMovimiento(),
        movimiento.getCantidad(),
        movimiento.getPrecioUnitario(),
        movimiento.getFechaMovimiento(),
        movimiento.getMotivo(),
        movimiento.getProducto() != null ? movimiento.getProducto().getNombre() : null,
        movimiento.getUsuario() != null ? movimiento.getUsuario().getNombres() : null);
  }

  public static MovimientoInventarioDetalleDTO toDetalleDTO(MovimientosInventario movimiento) {
    if (movimiento == null) return null;

    MovimientoInventarioDetalleDTO dto = new MovimientoInventarioDetalleDTO();
    dto.setId(movimiento.getId());
    dto.setTipoMovimiento(movimiento.getTipoMovimiento());
    dto.setCantidad(movimiento.getCantidad());
    dto.setPrecioUnitario(movimiento.getPrecioUnitario());
    dto.setFechaMovimiento(movimiento.getFechaMovimiento());
    dto.setMotivo(movimiento.getMotivo());
    dto.setCreatedAt(movimiento.getCreatedAt());

    // Producto info
    if (movimiento.getProducto() != null) {
      dto.setProductoId(movimiento.getProducto().getId());
      dto.setProductoNombre(movimiento.getProducto().getNombre());
      dto.setProductoCodigo(movimiento.getProducto().getCodigo());
    }

    // Usuario info
    if (movimiento.getUsuario() != null) {
      dto.setUsuarioId(movimiento.getUsuario().getId());
      dto.setUsuarioNombre(movimiento.getUsuario().getNombres());
    }

    return dto;
  }

  public static List<MovimientoInventarioListadoDTO> toListadoDTOList(
      List<MovimientosInventario> movimientos) {
    if (movimientos == null) return List.of();
    return movimientos.stream().map(MovimientoInventarioMapper::toListadoDTO).toList();
  }
}
