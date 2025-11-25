package gym.vitae.model;

import gym.vitae.model.enums.EstadoFactura;
import gym.vitae.model.enums.TipoVenta;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import org.hibernate.annotations.BatchSize;

@Entity
@Table(
    name = "facturas",
    uniqueConstraints = {@UniqueConstraint(columnNames = "numero_factura")},
    indexes = {
      @Index(name = "idx_numero", columnList = "numero_factura"),
      @Index(name = "idx_cliente", columnList = "cliente_id"),
      @Index(name = "idx_fecha", columnList = "fecha_emision"),
      @Index(name = "idx_estado", columnList = "estado"),
      @Index(name = "idx_facturas_fecha_estado", columnList = "fecha_emision,estado")
    })
public class Factura {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "numero_factura", nullable = false, unique = true, length = 50)
  private String numeroFactura;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "empleado_responsable_id", nullable = false)
  private Empleado empleadoResponsable;

  @Column(name = "fecha_emision", nullable = false)
  private LocalDate fechaEmision;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_venta", nullable = false, length = 20)
  private TipoVenta tipoVenta;

  @Column(name = "total", nullable = false, precision = 10, scale = 2)
  private BigDecimal total;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoFactura estado = EstadoFactura.PENDIENTE;

  @Lob
  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
  @BatchSize(size = 20)
  private Set<DetallesFactura> detalles = new HashSet<>();

  @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Pago> pagos = new HashSet<>();

  @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Membresia> membresias = new HashSet<>();

  // Constructores
  public Factura() {}

  public Factura(
      String numeroFactura,
      Cliente cliente,
      Empleado empleadoResponsable,
      LocalDate fechaEmision,
      TipoVenta tipoVenta,
      BigDecimal total) {
    this.numeroFactura = numeroFactura;
    this.cliente = cliente;
    this.empleadoResponsable = empleadoResponsable;
    this.fechaEmision = fechaEmision;
    this.tipoVenta = tipoVenta;
    this.total = total;
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

  public String getNumeroFactura() {
    return numeroFactura;
  }

  public void setNumeroFactura(String numeroFactura) {
    this.numeroFactura = numeroFactura;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public Empleado getEmpleadoResponsable() {
    return empleadoResponsable;
  }

  public void setEmpleadoResponsable(Empleado empleadoResponsable) {
    this.empleadoResponsable = empleadoResponsable;
  }

  public LocalDate getFechaEmision() {
    return fechaEmision;
  }

  public void setFechaEmision(LocalDate fechaEmision) {
    this.fechaEmision = fechaEmision;
  }

  public TipoVenta getTipoVenta() {
    return tipoVenta;
  }

  public void setTipoVenta(TipoVenta tipoVenta) {
    this.tipoVenta = tipoVenta;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public EstadoFactura getEstado() {
    return estado;
  }

  public void setEstado(EstadoFactura estado) {
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

  public Set<DetallesFactura> getDetalles() {
    return detalles;
  }

  public void setDetalles(Set<DetallesFactura> detalles) {
    this.detalles = detalles;
  }

  public Set<Pago> getPagos() {
    return pagos;
  }

  public void setPagos(Set<Pago> pagos) {
    this.pagos = pagos;
  }

  public Set<Membresia> getMembresias() {
    return membresias;
  }

  public void setMembresias(Set<Membresia> membresias) {
    this.membresias = membresias;
  }

  @Override
  public String toString() {
    return "Factura{id=" + id + ", numero='" + numeroFactura + "', total=" + total + "}";
  }
}
