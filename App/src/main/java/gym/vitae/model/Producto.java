package gym.vitae.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "productos",
    uniqueConstraints = {@UniqueConstraint(columnNames = "codigo")},
    indexes = {
      @Index(name = "idx_codigo", columnList = "codigo"),
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_activo", columnList = "activo"),
      @Index(name = "idx_stock", columnList = "stock"),
      @Index(name = "idx_productos_stock_critico", columnList = "stock")
    })
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoria_id", nullable = false)
  private Categoria categoria;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "proveedor_id")
  private Proveedore proveedor;

  @Column(name = "codigo", nullable = false, unique = true, length = 50)
  private String codigo;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "descripcion", length = 255)
  private String descripcion;

  @Column(name = "precio_unitario", nullable = false, precision = 10, scale = 2)
  private BigDecimal precioUnitario;

  @Column(name = "stock")
  private Integer stock = 0;

  @Column(name = "unidad_medida", length = 20)
  private String unidadMedida = "unidad";

  @Column(name = "fecha_ingreso")
  private LocalDate fechaIngreso;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
  private Set<MovimientosInventario> movimientos = new HashSet<>();

  @OneToMany(mappedBy = "producto", cascade = CascadeType.ALL)
  private Set<DetallesFactura> detallesFactura = new HashSet<>();

  // Constructores
  public Producto() {}

  public Producto(Categoria categoria, String codigo, String nombre, BigDecimal precioUnitario) {
    this.categoria = categoria;
    this.codigo = codigo;
    this.nombre = nombre;
    this.precioUnitario = precioUnitario;
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

  public Categoria getCategoria() {
    return categoria;
  }

  public void setCategoria(Categoria categoria) {
    this.categoria = categoria;
  }

  public Proveedore getProveedor() {
    return proveedor;
  }

  public void setProveedor(Proveedore proveedor) {
    this.proveedor = proveedor;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Set<MovimientosInventario> getMovimientos() {
    return movimientos;
  }

  public void setMovimientos(Set<MovimientosInventario> movimientos) {
    this.movimientos = movimientos;
  }

  public Set<DetallesFactura> getDetallesFactura() {
    return detallesFactura;
  }

  public void setDetallesFactura(Set<DetallesFactura> detallesFactura) {
    this.detallesFactura = detallesFactura;
  }

  @Override
  public String toString() {
    return "Producto{id="
        + id
        + ", codigo='"
        + codigo
        + "', nombre='"
        + nombre
        + "', stock="
        + stock
        + "}";
  }
}
