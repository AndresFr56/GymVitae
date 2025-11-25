package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.EstadoPago;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class PagoDetalleDTO {

  private Integer id;
  private BigDecimal monto;
  private LocalDate fechaPago;
  private String referencia;
  private EstadoPago estado;
  private String observaciones;
  private Instant createdAt;
  private Instant updatedAt;
  private Integer facturaId;
  private String facturaNumero;

  public PagoDetalleDTO() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public EstadoPago getEstado() {
    return estado;
  }

  public void setEstado(EstadoPago estado) {
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

  public Integer getFacturaId() {
    return facturaId;
  }

  public void setFacturaId(Integer facturaId) {
    this.facturaId = facturaId;
  }

  public String getFacturaNumero() {
    return facturaNumero;
  }

  public void setFacturaNumero(String facturaNumero) {
    this.facturaNumero = facturaNumero;
  }
}
