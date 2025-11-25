package gym.vitae.model.dtos.horarios;

import gym.vitae.model.enums.DiaSemana;
import gym.vitae.model.enums.EstadoInscripcion;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;

public class InscripcionClaseDetalleDTO {

  private Integer id;
  private LocalDate fechaInscripcion;
  private EstadoInscripcion estado;
  private Instant createdAt;
  private Instant updatedAt;

  // Cliente info
  private Integer clienteId;
  private String clienteNombre;
  private String clienteDocumento;

  // Horario info
  private Integer horarioId;
  private DiaSemana diaSemana;
  private LocalTime horaInicio;
  private LocalTime horaFin;

  // Clase info (via horario)
  private String claseNombre;

  // Instructor info (via horario)
  private String instructorNombre;

  public InscripcionClaseDetalleDTO() {}

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Integer getClienteId() {
    return clienteId;
  }

  public void setClienteId(Integer clienteId) {
    this.clienteId = clienteId;
  }

  public String getClienteNombre() {
    return clienteNombre;
  }

  public void setClienteNombre(String clienteNombre) {
    this.clienteNombre = clienteNombre;
  }

  public String getClienteDocumento() {
    return clienteDocumento;
  }

  public void setClienteDocumento(String clienteDocumento) {
    this.clienteDocumento = clienteDocumento;
  }

  public Integer getHorarioId() {
    return horarioId;
  }

  public void setHorarioId(Integer horarioId) {
    this.horarioId = horarioId;
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

  public String getClaseNombre() {
    return claseNombre;
  }

  public void setClaseNombre(String claseNombre) {
    this.claseNombre = claseNombre;
  }

  public String getInstructorNombre() {
    return instructorNombre;
  }

  public void setInstructorNombre(String instructorNombre) {
    this.instructorNombre = instructorNombre;
  }
}
