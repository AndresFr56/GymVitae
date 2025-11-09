package gym.vitae.model;

import gym.vitae.model.enums.EstadoPago;
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
    name = "pagos",
    indexes = {
      @Index(name = "idx_factura", columnList = "factura_id"),
      @Index(name = "idx_fecha", columnList = "fecha_pago"),
      @Index(name = "idx_estado", columnList = "estado")
    })
public class Pago {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "factura_id", nullable = false)
  private Factura factura;

  @Column(name = "monto", nullable = false, precision = 10, scale = 2)
  private BigDecimal monto;

  @Column(name = "fecha_pago", nullable = false)
  private LocalDate fechaPago;

  @Column(name = "referencia", length = 100)
  private String referencia;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoPago estado = EstadoPago.COMPLETADO;

  @Lob
  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  // Constructores
  public Pago() {}

  public Pago(Factura factura, BigDecimal monto, LocalDate fechaPago) {
    this.factura = factura;
    this.monto = monto;
    this.fechaPago = fechaPago;
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

  public Factura getFactura() {
    return factura;
  }

  public void setFactura(Factura factura) {
    this.factura = factura;
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

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String toString() {
    return "Pago{id=" + id + ", monto=" + monto + ", fecha=" + fechaPago + "}";
  }
}
