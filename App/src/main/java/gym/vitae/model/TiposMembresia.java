package gym.vitae.model;

import java.math.BigDecimal;
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
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(
    name = "tipos_membresia",
    indexes = {
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_activo", columnList = "activo")
    })
public class TiposMembresia {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 50)
  private String nombre;

  @Column(name = "descripcion", length = 255)
  private String descripcion;

  @Column(name = "duracion_dias", nullable = false)
  private Integer duracionDias;

  @Column(name = "costo", nullable = false, precision = 10, scale = 2)
  private BigDecimal costo;

  @Column(name = "acceso_completo")
  private Boolean accesoCompleto = true;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "tipoMembresia", cascade = CascadeType.ALL)
  private Set<MembresiaBeneficio> membresiaBeneficios = new HashSet<>();

  @OneToMany(mappedBy = "tipoMembresia", cascade = CascadeType.ALL)
  private Set<Membresia> membresias = new HashSet<>();

  @OneToMany(mappedBy = "tipoMembresia", cascade = CascadeType.ALL)
  private Set<DetallesFactura> detallesFactura = new HashSet<>();

  // Constructores
  public TiposMembresia() {}

  public TiposMembresia(String nombre, Integer duracionDias, BigDecimal costo) {
    this.nombre = nombre;
    this.duracionDias = duracionDias;
    this.costo = costo;
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

  public Integer getDuracionDias() {
    return duracionDias;
  }

  public void setDuracionDias(Integer duracionDias) {
    this.duracionDias = duracionDias;
  }

  public BigDecimal getCosto() {
    return costo;
  }

  public void setCosto(BigDecimal costo) {
    this.costo = costo;
  }

  public Boolean getAccesoCompleto() {
    return accesoCompleto;
  }

  public void setAccesoCompleto(Boolean accesoCompleto) {
    this.accesoCompleto = accesoCompleto;
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

  public Set<Membresia> getMembresias() {
    return membresias;
  }

  public void setMembresias(Set<Membresia> membresias) {
    this.membresias = membresias;
  }

  public Set<DetallesFactura> getDetallesFactura() {
    return detallesFactura;
  }

  public void setDetallesFactura(Set<DetallesFactura> detallesFactura) {
    this.detallesFactura = detallesFactura;
  }

  @Override
  public String toString() {
    return "TipoMembresia{id="
        + id
        + ", nombre='"
        + nombre
        + "', duracion="
        + duracionDias
        + " días}";
  }
}
