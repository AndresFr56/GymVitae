package gym.vitae.model.dtos.membresias;

import gym.vitae.model.enums.EstadoMembresia;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class MembresiaDetalleDTO {

  private Integer id;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private BigDecimal precioPagado;
  private EstadoMembresia estado;
  private String observaciones;
  private Instant createdAt;
  private Instant updatedAt;

  // Cliente
  private Integer clienteId;
  private String clienteNombre;
  private String clienteDocumento;

  // Tipo Membresía
  private Integer tipoMembresiaId;
  private String tipoMembresiaNombre;

  // Factura
  private Integer facturaId;
  private String facturaNumero;

  public MembresiaDetalleDTO() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public EstadoMembresia getEstado() {
    return estado;
  }

  public void setEstado(EstadoMembresia estado) {
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

  public Integer getClienteId() {
    return clienteId;
  }

  public void setClienteId(Integer clienteId) {
    this.clienteId = clienteId;
  }

  public String getClienteNombre() {
    return clienteNombre;
  }

  public void setClienteNombre(String clienteNombre) {
    this.clienteNombre = clienteNombre;
  }

  public String getClienteDocumento() {
    return clienteDocumento;
  }

  public void setClienteDocumento(String clienteDocumento) {
    this.clienteDocumento = clienteDocumento;
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
