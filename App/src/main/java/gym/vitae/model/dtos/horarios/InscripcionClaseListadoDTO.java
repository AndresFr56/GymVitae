package gym.vitae.model.dtos.horarios;

import gym.vitae.model.enums.DiaSemana;
import gym.vitae.model.enums.EstadoInscripcion;
import java.time.LocalDate;
import java.time.LocalTime;

public class InscripcionClaseListadoDTO {

  private Integer id;
  private LocalDate fechaInscripcion;
  private EstadoInscripcion estado;
  private String clienteNombre;
  private String clienteDocumento;
  private String claseNombre;
  private DiaSemana diaSemana;
  private LocalTime horaInicio;
  private String instructorNombre;

  public InscripcionClaseListadoDTO() {}

  public InscripcionClaseListadoDTO(
      Integer id,
      LocalDate fechaInscripcion,
      EstadoInscripcion estado,
      String clienteNombre,
      String clienteDocumento,
      String claseNombre,
      DiaSemana diaSemana,
      LocalTime horaInicio,
      String instructorNombre) {
    this.id = id;
    this.fechaInscripcion = fechaInscripcion;
    this.estado = estado;
    this.clienteNombre = clienteNombre;
    this.clienteDocumento = clienteDocumento;
    this.claseNombre = claseNombre;
    this.diaSemana = diaSemana;
    this.horaInicio = horaInicio;
    this.instructorNombre = instructorNombre;
  }

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

  public String getClaseNombre() {
    return claseNombre;
  }

  public void setClaseNombre(String claseNombre) {
    this.claseNombre = claseNombre;
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

  public String getInstructorNombre() {
    return instructorNombre;
  }

  public void setInstructorNombre(String instructorNombre) {
    this.instructorNombre = instructorNombre;
  }
}
