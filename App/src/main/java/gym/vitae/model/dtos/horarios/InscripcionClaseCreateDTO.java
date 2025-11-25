package gym.vitae.model.dtos.horarios;

import java.time.LocalDate;

public class InscripcionClaseCreateDTO {

  private Integer horarioId;
  private Integer clienteId;
  private LocalDate fechaInscripcion;

  public InscripcionClaseCreateDTO() {}

  public InscripcionClaseCreateDTO(
      Integer horarioId, Integer clienteId, LocalDate fechaInscripcion) {
    this.horarioId = horarioId;
    this.clienteId = clienteId;
    this.fechaInscripcion = fechaInscripcion;
  }

  public Integer getHorarioId() {
    return horarioId;
  }

  public void setHorarioId(Integer horarioId) {
    this.horarioId = horarioId;
  }

  public Integer getClienteId() {
    return clienteId;
  }

  public void setClienteId(Integer clienteId) {
    this.clienteId = clienteId;
  }

  public LocalDate getFechaInscripcion() {
    return fechaInscripcion;
  }

  public void setFechaInscripcion(LocalDate fechaInscripcion) {
    this.fechaInscripcion = fechaInscripcion;
  }
}
