package gym.vitae.model;

import gym.vitae.model.enums.TipoCategoria;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(
    name = "categorias",
    schema = "gym_system",
    indexes = {
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_tipo", columnList = "tipo"),
      @Index(name = "idx_subtipo", columnList = "subtipo"),
      @Index(name = "idx_activo", columnList = "activo")
    })
public class Categoria {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Enumerated(EnumType.STRING)
  @Column(name = "tipo", nullable = false)
  private TipoCategoria tipo;

  @Column(name = "subtipo", length = 50)
  private String subtipo;

  @Lob
  @Column(name = "descripcion")
  private String descripcion;

  @Column(name = "activo")
  private Boolean activo;

  @Column(name = "created_at")
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "categoria")
  private Set<Equipo> equipos = new LinkedHashSet<>();

  @OneToMany(mappedBy = "categoria")
  private Set<Producto> productos = new LinkedHashSet<>();

  @PrePersist
  protected void onCreate() {
    createdAt = Instant.now();
    updatedAt = Instant.now();
  }

  @PreUpdate
  protected void onUpdate() {
    updatedAt = Instant.now();
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public TipoCategoria getTipo() {
    return tipo;
  }

  public void setTipo(TipoCategoria tipo) {
    this.tipo = tipo;
  }

  public String getSubtipo() {
    return subtipo;
  }

  public void setSubtipo(String subtipo) {
    this.subtipo = subtipo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
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

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Set<Equipo> getEquipos() {
    return equipos;
  }

  public void setEquipos(Set<Equipo> equipos) {
    this.equipos = equipos;
  }

  public Set<Producto> getProductos() {
    return productos;
  }

  public void setProductos(Set<Producto> productos) {
    this.productos = productos;
  }

  @Override
  public String toString() {
    return "Categoria{id=" + id + ", nombre='" + nombre + "', tipo=" + tipo + "}";
  }
}
