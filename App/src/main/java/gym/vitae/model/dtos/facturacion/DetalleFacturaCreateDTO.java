package gym.vitae.model.dtos.facturacion;

import java.math.BigDecimal;

public class DetalleFacturaCreateDTO {

  private Integer cantidad;
  private BigDecimal precioUnitario;
  private Boolean aplicaIva;
  private Integer ivaId;

  // Solo uno debe tener valor
  private Integer tipoMembresiaId;
  private Integer productoId;
  private Integer equipoId;

  public DetalleFacturaCreateDTO() {}

  public DetalleFacturaCreateDTO(
      Integer cantidad,
      BigDecimal precioUnitario,
      Boolean aplicaIva,
      Integer ivaId,
      Integer tipoMembresiaId,
      Integer productoId,
      Integer equipoId) {
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    this.aplicaIva = aplicaIva;
    this.ivaId = ivaId;
    this.tipoMembresiaId = tipoMembresiaId;
    this.productoId = productoId;
    this.equipoId = equipoId;
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

  public Boolean getAplicaIva() {
    return aplicaIva;
  }

  public void setAplicaIva(Boolean aplicaIva) {
    this.aplicaIva = aplicaIva;
  }

  public Integer getIvaId() {
    return ivaId;
  }

  public void setIvaId(Integer ivaId) {
    this.ivaId = ivaId;
  }

  public Integer getTipoMembresiaId() {
    return tipoMembresiaId;
  }

  public void setTipoMembresiaId(Integer tipoMembresiaId) {
    this.tipoMembresiaId = tipoMembresiaId;
  }

  public Integer getProductoId() {
    return productoId;
  }

  public void setProductoId(Integer productoId) {
    this.productoId = productoId;
  }

  public Integer getEquipoId() {
    return equipoId;
  }

  public void setEquipoId(Integer equipoId) {
    this.equipoId = equipoId;
  }
}
