package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.TipoMovimiento;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class MovimientoInventarioDetalleDTO {

  private Integer id;
  private TipoMovimiento tipoMovimiento;
  private Integer cantidad;
  private BigDecimal precioUnitario;
  private LocalDate fechaMovimiento;
  private String motivo;
  private Instant createdAt;

  // Producto info
  private Integer productoId;
  private String productoNombre;
  private String productoCodigo;

  // Usuario info
  private Integer usuarioId;
  private String usuarioNombre;

  public MovimientoInventarioDetalleDTO() {}

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

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Integer getProductoId() {
    return productoId;
  }

  public void setProductoId(Integer productoId) {
    this.productoId = productoId;
  }

  public String getProductoNombre() {
    return productoNombre;
  }

  public void setProductoNombre(String productoNombre) {
    this.productoNombre = productoNombre;
  }

  public String getProductoCodigo() {
    return productoCodigo;
  }

  public void setProductoCodigo(String productoCodigo) {
    this.productoCodigo = productoCodigo;
  }

  public Integer getUsuarioId() {
    return usuarioId;
  }

  public void setUsuarioId(Integer usuarioId) {
    this.usuarioId = usuarioId;
  }

  public String getUsuarioNombre() {
    return usuarioNombre;
  }

  public void setUsuarioNombre(String usuarioNombre) {
    this.usuarioNombre = usuarioNombre;
  }
}
