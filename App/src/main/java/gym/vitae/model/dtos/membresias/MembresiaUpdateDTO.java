package gym.vitae.model.dtos.membresias;

import gym.vitae.model.enums.EstadoMembresia;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MembresiaUpdateDTO {
  private Integer tipoMembresiaId;
  private EstadoMembresia estado;
  private LocalDate fechaFin;
  private String observaciones;
  private BigDecimal precioPagado;

  public MembresiaUpdateDTO(Integer tipoMembresiaId, EstadoMembresia estado,
                            LocalDate fechaFin, String observaciones, BigDecimal precioPagado) {
    this.tipoMembresiaId = tipoMembresiaId;
    this.estado = estado;
    this.fechaFin = fechaFin;
    this.observaciones = observaciones;
    this.precioPagado = precioPagado;
  }

  public BigDecimal getPrecioPagado() {
    return precioPagado;
  }

  public void setPrecioPagado(BigDecimal precioPagado) {
    this.precioPagado = precioPagado;
  }

  public Integer getTipoMembresiaId() {
    return tipoMembresiaId;
  }

  public void setTipoMembresiaId(Integer tipoMembresiaId) {
    this.tipoMembresiaId = tipoMembresiaId;
  }

  public EstadoMembresia getEstado() {
    return estado;
  }

  public void setEstado(EstadoMembresia estado) {
    this.estado = estado;
  }

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}