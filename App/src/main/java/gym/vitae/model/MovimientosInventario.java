package gym.vitae.model;

import gym.vitae.model.enums.TipoMovimiento;
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
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

@Entity
@Table(
    name = "movimientos_inventario",
    indexes = {
      @Index(name = "idx_producto", columnList = "producto_id"),
      @Index(name = "idx_fecha", columnList = "fecha_movimiento"),
      @Index(name = "idx_tipo", columnList = "tipo_movimiento")
    })
public class MovimientosInventario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id", nullable = false)
  private Producto producto;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo_movimiento", nullable = false, length = 20)
  private TipoMovimiento tipoMovimiento;

  @Column(name = "cantidad", nullable = false)
  private Integer cantidad;

  @Column(name = "precio_unitario", precision = 10, scale = 2)
  private BigDecimal precioUnitario;

  @Column(name = "fecha_movimiento", nullable = false)
  private LocalDate fechaMovimiento;

  @Column(name = "motivo", length = 200)
  private String motivo;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "usuario_id")
  private Empleado usuario;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  // Constructores
  public MovimientosInventario() {}

  public MovimientosInventario(
      Producto producto,
      TipoMovimiento tipoMovimiento,
      Integer cantidad,
      LocalDate fechaMovimiento) {
    this.producto = producto;
    this.tipoMovimiento = tipoMovimiento;
    this.cantidad = cantidad;
    this.fechaMovimiento = fechaMovimiento;
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
  }

  // Getters y Setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public TipoMovimiento getTipoMovimiento() {
    return tipoMovimiento;
  }

  public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
    this.tipoMovimiento = tipoMovimiento;
  }

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public LocalDate getFechaMovimiento() {
    return fechaMovimiento;
  }

  public void setFechaMovimiento(LocalDate fechaMovimiento) {
    this.fechaMovimiento = fechaMovimiento;
  }

  public String getMotivo() {
    return motivo;
  }

  public void setMotivo(String motivo) {
    this.motivo = motivo;
  }

  public Empleado getUsuario() {
    return usuario;
  }

  public void setUsuario(Empleado usuario) {
    this.usuario = usuario;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return "MovimientoInventario{id="
        + id
        + ", tipo="
        + tipoMovimiento
        + ", cantidad="
        + cantidad
        + ", fecha="
        + fechaMovimiento
        + "}";
  }
}
