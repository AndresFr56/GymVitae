package gym.vitae.model;

import gym.vitae.model.enums.DiaSemana;
import java.time.Instant;
import java.time.LocalTime;
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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(
    name = "horarios",
    indexes = {
      @Index(name = "idx_clase", columnList = "clase_id"),
      @Index(name = "idx_instructor", columnList = "instructor_id"),
      @Index(name = "idx_dia", columnList = "dia_semana"),
      @Index(name = "idx_activo", columnList = "activo")
    })
public class Horario {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "clase_id", nullable = false)
  private Clase clase;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "instructor_id", nullable = false)
  private Empleado instructor;

  @Enumerated(EnumType.STRING)
  @Column(name = "dia_semana", nullable = false, length = 20)
  private DiaSemana diaSemana;

  @Column(name = "hora_inicio", nullable = false)
  private LocalTime horaInicio;

  @Column(name = "hora_fin", nullable = false)
  private LocalTime horaFin;

  @Column(name = "cupos_disponibles", nullable = false)
  private Integer cuposDisponibles;

  @Column(name = "activo")
  private Boolean activo = true;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  @OneToMany(mappedBy = "horario", cascade = CascadeType.ALL)
  private Set<InscripcionesClase> inscripciones = new HashSet<>();

  // Constructores
  public Horario() {}

  public Horario(
      Clase clase,
      Empleado instructor,
      DiaSemana diaSemana,
      LocalTime horaInicio,
      LocalTime horaFin,
      Integer cuposDisponibles) {
    this.clase = clase;
    this.instructor = instructor;
    this.diaSemana = diaSemana;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.cuposDisponibles = cuposDisponibles;
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

  public Clase getClase() {
    return clase;
  }

  public void setClase(Clase clase) {
    this.clase = clase;
  }

  public Empleado getInstructor() {
    return instructor;
  }

  public void setInstructor(Empleado instructor) {
    this.instructor = instructor;
  }

  public DiaSemana getDiaSemana() {
    return diaSemana;
  }

  public void setDiaSemana(DiaSemana diaSemana) {
    this.diaSemana = diaSemana;
  }

  public LocalTime getHoraInicio() {
    return horaInicio;
  }

  public void setHoraInicio(LocalTime horaInicio) {
    this.horaInicio = horaInicio;
  }

  public LocalTime getHoraFin() {
    return horaFin;
  }

  public void setHoraFin(LocalTime horaFin) {
    this.horaFin = horaFin;
  }

  public Integer getCuposDisponibles() {
    return cuposDisponibles;
  }

  public void setCuposDisponibles(Integer cuposDisponibles) {
    this.cuposDisponibles = cuposDisponibles;
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

  public Set<InscripcionesClase> getInscripciones() {
    return inscripciones;
  }

  public void setInscripciones(Set<InscripcionesClase> inscripciones) {
    this.inscripciones = inscripciones;
  }

  @Override
  public String toString() {
    return "Horario{id=" + id + ", dia=" + diaSemana + ", inicio=" + horaInicio + "}";
  }
}
