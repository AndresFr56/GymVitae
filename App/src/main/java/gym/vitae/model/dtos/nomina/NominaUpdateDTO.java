package gym.vitae.model.dtos.nomina;

import gym.vitae.model.enums.EstadoNomina;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NominaUpdateDTO {

  private EstadoNomina estado;
  private LocalDate fechaPago;
  private BigDecimal bonificaciones;
  private BigDecimal deducciones;
  private String observaciones;
  private Integer aprobadaPorId;
  private Integer pagadaPorId;

  public NominaUpdateDTO() {}

  public EstadoNomina getEstado() {
    return estado;
  }

  public void setEstado(EstadoNomina estado) {
    this.estado = estado;
  }

  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public BigDecimal getBonificaciones() {
    return bonificaciones;
  }

  public void setBonificaciones(BigDecimal bonificaciones) {
    this.bonificaciones = bonificaciones;
  }

  public BigDecimal getDeducciones() {
    return deducciones;
  }

  public void setDeducciones(BigDecimal deducciones) {
    this.deducciones = deducciones;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Integer getAprobadaPorId() {
    return aprobadaPorId;
  }

  public void setAprobadaPorId(Integer aprobadaPorId) {
    this.aprobadaPorId = aprobadaPorId;
  }

  public Integer getPagadaPorId() {
    return pagadaPorId;
  }

  public void setPagadaPorId(Integer pagadaPorId) {
    this.pagadaPorId = pagadaPorId;
  }
}
