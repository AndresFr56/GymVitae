package gym.vitae.model.dtos.horarios;

import gym.vitae.model.enums.DiaSemana;
import java.time.LocalTime;

public class HorarioCreateDTO {

  private Integer claseId;
  private Integer instructorId;
  private DiaSemana diaSemana;
  private LocalTime horaInicio;
  private LocalTime horaFin;
  private Integer cuposDisponibles;

  public HorarioCreateDTO() {}

  public HorarioCreateDTO(
      Integer claseId,
      Integer instructorId,
      DiaSemana diaSemana,
      LocalTime horaInicio,
      LocalTime horaFin,
      Integer cuposDisponibles) {
    this.claseId = claseId;
    this.instructorId = instructorId;
    this.diaSemana = diaSemana;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.cuposDisponibles = cuposDisponibles;
  }

  public Integer getClaseId() {
    return claseId;
  }

  public void setClaseId(Integer claseId) {
    this.claseId = claseId;
  }

  public Integer getInstructorId() {
    return instructorId;
  }

  public void setInstructorId(Integer instructorId) {
    this.instructorId = instructorId;
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
}
