package gym.vitae.model;

import gym.vitae.model.enums.NivelClase;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
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
import org.hibernate.annotations.BatchSize;

@Entity
@Table(
    name = "clases",
    indexes = {
      @Index(name = "idx_nombre", columnList = "nombre"),
      @Index(name = "idx_activa", columnList = "activa")
    })
public class Clase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "nombre", nullable = false, length = 100)
  private String nombre;

  @Lob
  @Column(name = "descripcion", columnDefinition = "TEXT")
  private String descripcion;

  @Column(name = "duracion_minutos", nullable = false)
  private Integer duracionMinutos;

  @Column(name = "capacidad_maxima", nullable = false)
  private Integer capacidadMaxima;

  @Enumerated(EnumType.STRING)
  @Column(name = "nivel", length = 20)
  private NivelClase nivel = NivelClase.TODOS;

  @Column(name = "activa")
  private Boolean activa = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "clase", cascade = CascadeType.ALL)
  @BatchSize(size = 20)
  private Set<Horario> horarios = new HashSet<>();

  // Constructores
  public Clase() {}

  public Clase(String nombre, Integer duracionMinutos, Integer capacidadMaxima) {
    this.nombre = nombre;
    this.duracionMinutos = duracionMinutos;
    this.capacidadMaxima = capacidadMaxima;
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

  public Integer getDuracionMinutos() {
    return duracionMinutos;
  }

  public void setDuracionMinutos(Integer duracionMinutos) {
    this.duracionMinutos = duracionMinutos;
  }

  public Integer getCapacidadMaxima() {
    return capacidadMaxima;
  }

  public void setCapacidadMaxima(Integer capacidadMaxima) {
    this.capacidadMaxima = capacidadMaxima;
  }

  public NivelClase getNivel() {
    return nivel;
  }

  public void setNivel(NivelClase nivel) {
    this.nivel = nivel;
  }

  public Boolean getActiva() {
    return activa;
  }

  public void setActiva(Boolean activa) {
    this.activa = activa;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public Set<Horario> getHorarios() {
    return horarios;
  }

  public void setHorarios(Set<Horario> horarios) {
    this.horarios = horarios;
  }

  @Override
  public String toString() {
    return "Clase{id=" + id + ", nombre='" + nombre + "', nivel=" + nivel + "}";
  }
}
