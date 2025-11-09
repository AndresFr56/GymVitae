package gym.vitae.model;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "proveedores",
    uniqueConstraints = {@UniqueConstraint(columnNames = "codigo")},
    indexes = {
      @Index(name = "idx_codigo", columnList = "codigo"),
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_activo", columnList = "activo")
    })
public class Proveedore {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "codigo", nullable = false, unique = true, length = 20)
  private String codigo;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Column(name = "contacto", length = 100)
  private String contacto;

  @Column(name = "telefono", length = 20)
  private String telefono;

  @Column(name = "email", length = 100)
  private String email;

  @Lob
  @Column(name = "direccion", columnDefinition = "TEXT")
  private String direccion;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "proveedor", cascade = CascadeType.ALL)
  private Set<Producto> productos = new HashSet<>();

  // Constructores
  public Proveedore() {}

  public Proveedore(String codigo, String nombre) {
    this.codigo = codigo;
    this.nombre = nombre;
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

  public String getContacto() {
    return contacto;
  }

  public void setContacto(String contacto) {
    this.contacto = contacto;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getDireccion() {
    return direccion;
  }

  public void setDireccion(String direccion) {
    this.direccion = direccion;
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

  public Set<Producto> getProductos() {
    return productos;
  }

  public void setProductos(Set<Producto> productos) {
    this.productos = productos;
  }

  @Override
  public String toString() {
    return "Proveedor{id=" + id + ", codigo='" + codigo + "', nombre='" + nombre + "'}";
  }
}
