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
    name = "iva",
    indexes = {@Index(name = "idx_activo", columnList = "activo")})
public class Iva {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "porcentaje", nullable = false, precision = 5, scale = 2)
  private BigDecimal porcentaje;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "iva", cascade = CascadeType.ALL)
  private Set<DetallesFactura> detallesFactura = new HashSet<>();

  // Constructores
  public Iva() {}

  public Iva(BigDecimal porcentaje) {
    this.porcentaje = porcentaje;
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

  public BigDecimal getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(BigDecimal porcentaje) {
    this.porcentaje = porcentaje;
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

  public Set<DetallesFactura> getDetallesFactura() {
    return detallesFactura;
  }

  public void setDetallesFactura(Set<DetallesFactura> detallesFactura) {
    this.detallesFactura = detallesFactura;
  }

  @Override
  public String toString() {
    return "Iva{id=" + id + ", porcentaje=" + porcentaje + "%}";
  }
}
