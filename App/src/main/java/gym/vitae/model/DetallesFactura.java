package gym.vitae.model;

import java.math.BigDecimal;
import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
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
    name = "detalles_factura",
    indexes = {
      @Index(name = "idx_factura", columnList = "factura_id"),
      @Index(name = "idx_tipo_membresia", columnList = "tipo_membresia_id"),
      @Index(name = "idx_iva", columnList = "iva_id")
    })
public class DetallesFactura {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "factura_id", nullable = false)
  private Factura factura;

  @Column(name = "cantidad", nullable = false)
  private Integer cantidad = 1;

  @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
  private BigDecimal precioUnitario;

  @Column(name = "subtotal", nullable = false, precision = 10, scale = 2)
  private BigDecimal subtotal;

  @Column(name = "aplica_iva")
  private Boolean aplicaIva = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "iva_id")
  private Iva iva;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tipo_membresia_id")
  private TiposMembresia tipoMembresia;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "producto_id")
  private Producto producto;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "equipo_id")
  private Equipo equipo;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  public DetallesFactura() {}

  public DetallesFactura(Factura factura, Integer cantidad, BigDecimal precioUnitario) {
    this.factura = factura;
    this.cantidad = cantidad;
    this.precioUnitario = precioUnitario;
    calcularSubtotal();
  }

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    calcularSubtotal();
  }

  private void calcularSubtotal() {
    if (precioUnitario != null && cantidad != null) {
      subtotal = precioUnitario.multiply(BigDecimal.valueOf(cantidad));
    }
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

  public Integer getCantidad() {
    return cantidad;
  }

  public void setCantidad(Integer cantidad) {
    this.cantidad = cantidad;
    calcularSubtotal();
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
    calcularSubtotal();
  }

  public BigDecimal getSubtotal() {
    return subtotal;
  }

  public void setSubtotal(BigDecimal subtotal) {
    this.subtotal = subtotal;
  }

  public Boolean getAplicaIva() {
    return aplicaIva;
  }

  public void setAplicaIva(Boolean aplicaIva) {
    this.aplicaIva = aplicaIva;
  }

  public Iva getIva() {
    return iva;
  }

  public void setIva(Iva iva) {
    this.iva = iva;
  }

  public TiposMembresia getTipoMembresia() {
    return tipoMembresia;
  }

  public void setTipoMembresia(TiposMembresia tipoMembresia) {
    this.tipoMembresia = tipoMembresia;
  }

  public Producto getProducto() {
    return producto;
  }

  public void setProducto(Producto producto) {
    this.producto = producto;
  }

  public Equipo getEquipo() {
    return equipo;
  }

  public void setEquipo(Equipo equipo) {
    this.equipo = equipo;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return "DetalleFactura{id=" + id + ", cantidad=" + cantidad + ", subtotal=" + subtotal + "}";
  }
}
