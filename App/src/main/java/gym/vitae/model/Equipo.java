package gym.vitae.model;

import gym.vitae.model.enums.EstadoEquipo;
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

@Entity
@Table(
    name = "equipos",
    uniqueConstraints = {@UniqueConstraint(columnNames = "codigo")},
    indexes = {
      @Index(name = "idx_codigo", columnList = "codigo"),
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_estado", columnList = "estado")
    })
public class Equipo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "categoria_id", nullable = false)
  private Categoria categoria;

  @Column(name = "codigo", nullable = false, unique = true, length = 50)
  private String codigo;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "descripcion", length = 255)
  private String descripcion;

  @Column(name = "marca", length = 50)
  private String marca;

  @Column(name = "modelo", length = 50)
  private String modelo;

  @Column(name = "fecha_adquisicion", nullable = false)
  private LocalDate fechaAdquisicion;

  @Column(name = "costo", precision = 10, scale = 2)
  private BigDecimal costo;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoEquipo estado = EstadoEquipo.OPERATIVO;

  @Column(name = "ubicacion", length = 100)
  private String ubicacion;

  @Lob
  @Column(name = "observaciones", columnDefinition = "TEXT")
  private String observaciones;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "equipo", cascade = CascadeType.ALL)
  private Set<DetallesFactura> detallesFactura = new HashSet<>();

  // Constructores
  public Equipo() {}

  public Equipo(Categoria categoria, String codigo, String nombre, LocalDate fechaAdquisicion) {
    this.categoria = categoria;
    this.codigo = codigo;
    this.nombre = nombre;
    this.fechaAdquisicion = fechaAdquisicion;
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

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public LocalDate getFechaAdquisicion() {
    return fechaAdquisicion;
  }

  public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
    this.fechaAdquisicion = fechaAdquisicion;
  }

  public BigDecimal getCosto() {
    return costo;
  }

  public void setCosto(BigDecimal costo) {
    this.costo = costo;
  }

  public EstadoEquipo getEstado() {
    return estado;
  }

  public void setEstado(EstadoEquipo estado) {
    this.estado = estado;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
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

  public Set<DetallesFactura> getDetallesFactura() {
    return detallesFactura;
  }

  public void setDetallesFactura(Set<DetallesFactura> detallesFactura) {
    this.detallesFactura = detallesFactura;
  }

  @Override
  public String toString() {
    return "Equipo{id="
        + id
        + ", codigo='"
        + codigo
        + "', nombre='"
        + nombre
        + "', estado="
        + estado
        + "}";
  }
}
