package gym.vitae.model;

import gym.vitae.model.enums.EstadoMembresia;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(
    name = "membresias",
    indexes = {
      @Index(name = "idx_cliente", columnList = "cliente_id"),
      @Index(name = "idx_estado", columnList = "estado"),
      @Index(name = "idx_fechas", columnList = "fecha_inicio,fecha_fin"),
      @Index(name = "idx_membresias_vigentes", columnList = "fecha_fin,estado")
    })
public class Membresia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tipo_membresia_id", nullable = false)
  private TiposMembresia tipoMembresia;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "factura_id", nullable = false)
  private Factura factura;

  @Column(name = "fecha_inicio", nullable = false)
  private LocalDate fechaInicio;

  @Column(name = "fecha_fin", nullable = false)
  private LocalDate fechaFin;

  @Column(name = "precio_pagado", nullable = false, precision = 10, scale = 2)
  private BigDecimal precioPagado;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoMembresia estado = EstadoMembresia.ACTIVA;

  @Lob
  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  // Constructores
  public Membresia() {}

  public Membresia(
      Cliente cliente,
      TiposMembresia tipoMembresia,
      Factura factura,
      LocalDate fechaInicio,
      LocalDate fechaFin,
      BigDecimal precioPagado) {
    this.cliente = cliente;
    this.tipoMembresia = tipoMembresia;
    this.factura = factura;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.precioPagado = precioPagado;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
  }

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public TiposMembresia getTipoMembresia() {
    return tipoMembresia;
  }

  public void setTipoMembresia(TiposMembresia tipoMembresia) {
    this.tipoMembresia = tipoMembresia;
  }

  public Factura getFactura() {
    return factura;
  }

  public void setFactura(Factura factura) {
    this.factura = factura;
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

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String toString() {
    return "Membresia{id=" + id + ", fechaInicio=" + fechaInicio + ", fechaFin=" + fechaFin + "}";
  }
}
