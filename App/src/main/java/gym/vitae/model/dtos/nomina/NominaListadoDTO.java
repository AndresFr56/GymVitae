package gym.vitae.model.dtos.nomina;

import gym.vitae.model.enums.EstadoNomina;
import java.math.BigDecimal;
import java.time.LocalDate;

public class NominaListadoDTO {

  private Integer id;
  private Integer mes;
  private Integer anio;
  private BigDecimal salarioBase;
  private BigDecimal bonificaciones;
  private BigDecimal deducciones;
  private BigDecimal totalPagar;
  private LocalDate fechaPago;
  private EstadoNomina estado;
  private String empleadoNombre;
  private String empleadoCargo;

  public NominaListadoDTO() {}

  public NominaListadoDTO(
      Integer id,
      Integer mes,
      Integer anio,
      BigDecimal salarioBase,
      BigDecimal bonificaciones,
      BigDecimal deducciones,
      BigDecimal totalPagar,
      LocalDate fechaPago,
      EstadoNomina estado,
      String empleadoNombre,
      String empleadoCargo) {
    this.id = id;
    this.mes = mes;
    this.anio = anio;
    this.salarioBase = salarioBase;
    this.bonificaciones = bonificaciones;
    this.deducciones = deducciones;
    this.totalPagar = totalPagar;
    this.fechaPago = fechaPago;
    this.estado = estado;
    this.empleadoNombre = empleadoNombre;
    this.empleadoCargo = empleadoCargo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public BigDecimal getTotalPagar() {
    return totalPagar;
  }

  public void setTotalPagar(BigDecimal totalPagar) {
    this.totalPagar = totalPagar;
  }

  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public EstadoNomina getEstado() {
    return estado;
  }

  public void setEstado(EstadoNomina estado) {
    this.estado = estado;
  }

  public String getEmpleadoNombre() {
    return empleadoNombre;
  }

  public void setEmpleadoNombre(String empleadoNombre) {
    this.empleadoNombre = empleadoNombre;
  }

  public String getEmpleadoCargo() {
    return empleadoCargo;
  }

  public void setEmpleadoCargo(String empleadoCargo) {
    this.empleadoCargo = empleadoCargo;
  }

  /** Retorna el período en formato "Mes/Año" */
  public String getPeriodo() {
    return String.format("%02d/%d", mes, anio);
  }
}
