package gym.vitae.model.dtos.nomina;

import gym.vitae.model.enums.EstadoNomina;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class NominaDetalleDTO {

  private Integer id;
  private Integer mes;
  private Integer anio;
  private BigDecimal salarioBase;
  private BigDecimal bonificaciones;
  private BigDecimal deducciones;
  private BigDecimal totalPagar;
  private LocalDate fechaPago;
  private EstadoNomina estado;
  private String observaciones;
  private Instant createdAt;
  private Instant updatedAt;

  // Empleado info
  private Integer empleadoId;
  private String empleadoNombre;
  private String empleadoCargo;

  // Info de responsables
  private String generadaPorNombre;
  private String aprobadaPorNombre;
  private String pagadaPorNombre;

  public NominaDetalleDTO() {}

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

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Integer getEmpleadoId() {
    return empleadoId;
  }

  public void setEmpleadoId(Integer empleadoId) {
    this.empleadoId = empleadoId;
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

  public String getGeneradaPorNombre() {
    return generadaPorNombre;
  }

  public void setGeneradaPorNombre(String generadaPorNombre) {
    this.generadaPorNombre = generadaPorNombre;
  }

  public String getAprobadaPorNombre() {
    return aprobadaPorNombre;
  }

  public void setAprobadaPorNombre(String aprobadaPorNombre) {
    this.aprobadaPorNombre = aprobadaPorNombre;
  }

  public String getPagadaPorNombre() {
    return pagadaPorNombre;
  }

  public void setPagadaPorNombre(String pagadaPorNombre) {
    this.pagadaPorNombre = pagadaPorNombre;
  }

  public String getPeriodo() {
    return String.format("%02d/%d", mes, anio);
  }
}
