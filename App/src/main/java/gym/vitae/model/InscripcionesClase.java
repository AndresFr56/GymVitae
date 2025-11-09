package gym.vitae.model;

import gym.vitae.model.enums.EstadoInscripcion;
import java.time.Instant;
import java.time.LocalDate;
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
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

@Entity
@Table(
    name = "inscripciones_clases",
    indexes = {
      @Index(name = "idx_horario", columnList = "horario_id"),
      @Index(name = "idx_cliente", columnList = "cliente_id"),
      @Index(name = "idx_fecha", columnList = "fecha_inscripcion"),
      @Index(name = "idx_estado", columnList = "estado")
    })
public class InscripcionesClase {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "horario_id", nullable = false)
  private Horario horario;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "cliente_id", nullable = false)
  private Cliente cliente;

  @Column(name = "fecha_inscripcion", nullable = false)
  private LocalDate fechaInscripcion;

  @Enumerated(EnumType.STRING)
  @Column(name = "estado", length = 20)
  private EstadoInscripcion estado = EstadoInscripcion.ACTIVA;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at")
  private Instant updatedAt;

  // Constructores
  public InscripcionesClase() {}

  public InscripcionesClase(Horario horario, Cliente cliente, LocalDate fechaInscripcion) {
    this.horario = horario;
    this.cliente = cliente;
    this.fechaInscripcion = fechaInscripcion;
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

  public Horario getHorario() {
    return horario;
  }

  public void setHorario(Horario horario) {
    this.horario = horario;
  }

  public Cliente getCliente() {
    return cliente;
  }

  public void setCliente(Cliente cliente) {
    this.cliente = cliente;
  }

  public LocalDate getFechaInscripcion() {
    return fechaInscripcion;
  }

  public void setFechaInscripcion(LocalDate fechaInscripcion) {
    this.fechaInscripcion = fechaInscripcion;
  }

  public EstadoInscripcion getEstado() {
    return estado;
  }

  public void setEstado(EstadoInscripcion estado) {
    this.estado = estado;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  public String toString() {
    return "InscripcionClase{id=" + id + ", fecha=" + fechaInscripcion + ", estado=" + estado + "}";
  }
}
