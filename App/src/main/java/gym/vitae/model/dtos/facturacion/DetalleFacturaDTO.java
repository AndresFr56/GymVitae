package gym.vitae.model.dtos.facturacion;

import java.math.BigDecimal;

public class DetalleFacturaDTO {

  private Integer id;
  private Integer cantidad;
  private BigDecimal precioUnitario;
  private BigDecimal subtotal;
  private Boolean aplicaIva;
  private BigDecimal ivaPorcentaje;

  // Info del item (solo uno tendrá valor)
  private Integer tipoMembresiaId;
  private String tipoMembresiaNombre;
  private Integer productoId;
  private String productoNombre;
  private Integer equipoId;
  private String equipoNombre;

  public DetalleFacturaDTO() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public Boolean getAplicaIva() {
    return aplicaIva;
  }

  public void setAplicaIva(Boolean aplicaIva) {
    this.aplicaIva = aplicaIva;
  }

  public BigDecimal getIvaPorcentaje() {
    return ivaPorcentaje;
  }

  public void setIvaPorcentaje(BigDecimal ivaPorcentaje) {
    this.ivaPorcentaje = ivaPorcentaje;
  }

  public Integer getTipoMembresiaId() {
    return tipoMembresiaId;
  }

  public void setTipoMembresiaId(Integer tipoMembresiaId) {
    this.tipoMembresiaId = tipoMembresiaId;
  }

  public String getTipoMembresiaNombre() {
    return tipoMembresiaNombre;
  }

  public void setTipoMembresiaNombre(String tipoMembresiaNombre) {
    this.tipoMembresiaNombre = tipoMembresiaNombre;
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

  public Integer getEquipoId() {
    return equipoId;
  }

  public void setEquipoId(Integer equipoId) {
    this.equipoId = equipoId;
  }

  public String getEquipoNombre() {
    return equipoNombre;
  }

  public void setEquipoNombre(String equipoNombre) {
    this.equipoNombre = equipoNombre;
  }

  /** Retorna el nombre del item (membresía, producto o equipo) */
  public String getItemNombre() {
    if (tipoMembresiaNombre != null) return tipoMembresiaNombre;
    if (productoNombre != null) return productoNombre;
    if (equipoNombre != null) return equipoNombre;
    return "Sin descripción";
  }
}
