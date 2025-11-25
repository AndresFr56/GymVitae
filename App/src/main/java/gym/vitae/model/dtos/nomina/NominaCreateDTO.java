package gym.vitae.model.dtos.nomina;

import java.math.BigDecimal;

public class NominaCreateDTO {

  private Integer empleadoId;
  private Integer mes;
  private Integer anio;
  private BigDecimal salarioBase;
  private BigDecimal bonificaciones;
  private BigDecimal deducciones;
  private String observaciones;

  public NominaCreateDTO() {}

  public NominaCreateDTO(
      Integer empleadoId,
      Integer mes,
      Integer anio,
      BigDecimal salarioBase,
      BigDecimal bonificaciones,
      BigDecimal deducciones,
      String observaciones) {
    this.empleadoId = empleadoId;
    this.mes = mes;
    this.anio = anio;
    this.salarioBase = salarioBase;
    this.bonificaciones = bonificaciones;
    this.deducciones = deducciones;
    this.observaciones = observaciones;
  }

  public Integer getEmpleadoId() {
    return empleadoId;
  }

  public void setEmpleadoId(Integer empleadoId) {
    this.empleadoId = empleadoId;
  }

  public Integer getMes() {
    return mes;
  }

  public void setMes(Integer mes) {
    this.mes = mes;
  }

  public Integer getAnio() {
    return anio;
  }

  public void setAnio(Integer anio) {
    this.anio = anio;
  }

  public BigDecimal getSalarioBase() {
    return salarioBase;
  }

  public void setSalarioBase(BigDecimal salarioBase) {
    this.salarioBase = salarioBase;
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
}
