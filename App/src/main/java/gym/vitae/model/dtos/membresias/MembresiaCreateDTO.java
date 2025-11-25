package gym.vitae.model.dtos.membresias;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MembresiaCreateDTO {

  private Integer clienteId;
  private Integer tipoMembresiaId;
  private Integer facturaId;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private BigDecimal precioPagado;
  private String observaciones;

  public MembresiaCreateDTO() {}

  public MembresiaCreateDTO(
      Integer clienteId,
      Integer tipoMembresiaId,
      Integer facturaId,
      LocalDate fechaInicio,
      LocalDate fechaFin,
      BigDecimal precioPagado,
      String observaciones) {
    this.clienteId = clienteId;
    this.tipoMembresiaId = tipoMembresiaId;
    this.facturaId = facturaId;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.precioPagado = precioPagado;
    this.observaciones = observaciones;
  }

  public Integer getClienteId() {
    return clienteId;
  }

  public void setClienteId(Integer clienteId) {
    this.clienteId = clienteId;
  }

  public Integer getTipoMembresiaId() {
    return tipoMembresiaId;
  }

  public void setTipoMembresiaId(Integer tipoMembresiaId) {
    this.tipoMembresiaId = tipoMembresiaId;
  }

  public Integer getFacturaId() {
    return facturaId;
  }

  public void setFacturaId(Integer facturaId) {
    this.facturaId = facturaId;
  }

  public LocalDate getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public BigDecimal getPrecioPagado() {
    return precioPagado;
  }

  public void setPrecioPagado(BigDecimal precioPagado) {
    this.precioPagado = precioPagado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
