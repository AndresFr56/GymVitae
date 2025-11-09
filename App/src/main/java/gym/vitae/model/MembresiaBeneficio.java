package gym.vitae.model;

import java.time.Instant;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(
    name = "membresia_beneficios",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "unique_membresia_beneficio",
          columnNames = {"tipo_membresia_id", "beneficio_id"})
    })
public class MembresiaBeneficio {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tipo_membresia_id", nullable = false)
  private TiposMembresia tipoMembresia;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "beneficio_id", nullable = false)
  private Beneficio beneficio;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  // Constructores
  public MembresiaBeneficio() {}

  public MembresiaBeneficio(TiposMembresia tipoMembresia, Beneficio beneficio) {
    this.tipoMembresia = tipoMembresia;
    this.beneficio = beneficio;
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

  public TiposMembresia getTipoMembresia() {
    return tipoMembresia;
  }

  public void setTipoMembresia(TiposMembresia tipoMembresia) {
    this.tipoMembresia = tipoMembresia;
  }

  public Beneficio getBeneficio() {
    return beneficio;
  }

  public void setBeneficio(Beneficio beneficio) {
    this.beneficio = beneficio;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public String toString() {
    return "MembresiaBeneficio{id=" + id + "}";
  }
}
