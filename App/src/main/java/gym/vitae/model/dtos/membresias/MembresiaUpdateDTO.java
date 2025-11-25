package gym.vitae.model.dtos.membresias;

import gym.vitae.model.enums.EstadoMembresia;
import java.time.LocalDate;

public class MembresiaUpdateDTO {

  private EstadoMembresia estado;
  private LocalDate fechaFin;
  private String observaciones;

  public MembresiaUpdateDTO() {}

  public MembresiaUpdateDTO(EstadoMembresia estado, LocalDate fechaFin, String observaciones) {
    this.estado = estado;
    this.fechaFin = fechaFin;
    this.observaciones = observaciones;
  }

  public EstadoMembresia getEstado() {
    return estado;
  }

  public void setEstado(EstadoMembresia estado) {
    this.estado = estado;
  }

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
