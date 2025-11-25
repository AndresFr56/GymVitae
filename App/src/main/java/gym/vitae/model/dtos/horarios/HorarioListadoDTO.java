package gym.vitae.model.dtos.horarios;

import gym.vitae.model.enums.DiaSemana;
import gym.vitae.model.enums.NivelClase;
import java.time.LocalTime;

public class HorarioListadoDTO {

  private Integer id;
  private DiaSemana diaSemana;
  private LocalTime horaInicio;
  private LocalTime horaFin;
  private Integer cuposDisponibles;
  private Boolean activo;
  private String claseNombre;
  private NivelClase claseNivel;
  private String instructorNombre;

  public HorarioListadoDTO() {}

  public HorarioListadoDTO(
      Integer id,
      DiaSemana diaSemana,
      LocalTime horaInicio,
      LocalTime horaFin,
      Integer cuposDisponibles,
      Boolean activo,
      String claseNombre,
      NivelClase claseNivel,
      String instructorNombre) {
    this.id = id;
    this.diaSemana = diaSemana;
    this.horaInicio = horaInicio;
    this.horaFin = horaFin;
    this.cuposDisponibles = cuposDisponibles;
    this.activo = activo;
    this.claseNombre = claseNombre;
    this.claseNivel = claseNivel;
    this.instructorNombre = instructorNombre;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public String getClaseNombre() {
    return claseNombre;
  }

  public void setClaseNombre(String claseNombre) {
    this.claseNombre = claseNombre;
  }

  public NivelClase getClaseNivel() {
    return claseNivel;
  }

  public void setClaseNivel(NivelClase claseNivel) {
    this.claseNivel = claseNivel;
  }

  public String getInstructorNombre() {
    return instructorNombre;
  }

  public void setInstructorNombre(String instructorNombre) {
    this.instructorNombre = instructorNombre;
  }

  /** Retorna el horario formateado como "HH:mm - HH:mm" */
  public String getHorarioFormateado() {
    return String.format("%s - %s", horaInicio, horaFin);
  }
}
