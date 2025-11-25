package gym.vitae.model.dtos.horarios;

import gym.vitae.model.enums.DiaSemana;
import gym.vitae.model.enums.NivelClase;
import java.time.Instant;
import java.time.LocalTime;

public class HorarioDetalleDTO {

  private Integer id;
  private DiaSemana diaSemana;
  private LocalTime horaInicio;
  private LocalTime horaFin;
  private Integer cuposDisponibles;
  private Boolean activo;
  private Instant createdAt;
  private Instant updatedAt;

  // Clase info
  private Integer claseId;
  private String claseNombre;
  private NivelClase claseNivel;

  // Instructor info
  private Integer instructorId;
  private String instructorNombre;

  public HorarioDetalleDTO() {
    /* document why this constructor is empty */
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

  public Integer getClaseId() {
    return claseId;
  }

  public void setClaseId(Integer claseId) {
    this.claseId = claseId;
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

  public Integer getInstructorId() {
    return instructorId;
  }

  public void setInstructorId(Integer instructorId) {
    this.instructorId = instructorId;
  }

  public String getInstructorNombre() {
    return instructorNombre;
  }

  public void setInstructorNombre(String instructorNombre) {
    this.instructorNombre = instructorNombre;
  }
}
