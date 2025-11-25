package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.TipoMovimiento;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MovimientoInventarioListadoDTO {

  private Integer id;
  private TipoMovimiento tipoMovimiento;
  private Integer cantidad;
  private BigDecimal precioUnitario;
  private LocalDate fechaMovimiento;
  private String motivo;
  private String productoNombre;
  private String usuarioNombre;

  public MovimientoInventarioListadoDTO() {}

  public MovimientoInventarioListadoDTO(
      Integer id,
      TipoMovimiento tipoMovimiento,
      Integer cantidad,
      BigDecimal precioUnitario,
      LocalDate fechaMovimiento,
      String motivo,
      String productoNombre,
      String usuarioNombre) {
    this.id = id;
    this.tipoMovimiento = tipoMovimiento;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    this.fechaMovimiento = fechaMovimiento;
    this.motivo = motivo;
    this.productoNombre = productoNombre;
    this.usuarioNombre = usuarioNombre;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public TipoMovimiento getTipoMovimiento() {
    return tipoMovimiento;
  }

  public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
    this.tipoMovimiento = tipoMovimiento;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public LocalDate getFechaMovimiento() {
    return fechaMovimiento;
  }

  public void setFechaMovimiento(LocalDate fechaMovimiento) {
    this.fechaMovimiento = fechaMovimiento;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public String getProductoNombre() {
    return productoNombre;
  }

  public void setProductoNombre(String productoNombre) {
    this.productoNombre = productoNombre;
  }

  public String getUsuarioNombre() {
    return usuarioNombre;
  }

  public void setUsuarioNombre(String usuarioNombre) {
    this.usuarioNombre = usuarioNombre;
  }
}
