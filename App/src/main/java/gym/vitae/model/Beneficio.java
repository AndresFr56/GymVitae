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

@Entity
@Table(
    name = "beneficios",
    indexes = {
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_activo", columnList = "activo")
    })
public class Beneficio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Lob
  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "beneficio", cascade = CascadeType.ALL)
  private Set<MembresiaBeneficio> membresiaBeneficios = new HashSet<>();

  // Constructores
  public Beneficio() {}

  public Beneficio(String nombre, String descripcion) {
    this.nombre = nombre;
    this.descripcion = descripcion;
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

  public Set<MembresiaBeneficio> getMembresiaBeneficios() {
    return membresiaBeneficios;
  }

  public void setMembresiaBeneficios(Set<MembresiaBeneficio> membresiaBeneficios) {
    this.membresiaBeneficios = membresiaBeneficios;
  }

  @Override
  public String toString() {
    return "Beneficio{id=" + id + ", nombre='" + nombre + "'}";
  }
}
