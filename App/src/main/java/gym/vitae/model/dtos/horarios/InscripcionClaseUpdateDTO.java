package gym.vitae.model.dtos.horarios;

import gym.vitae.model.enums.EstadoInscripcion;

public class InscripcionClaseUpdateDTO {

  private EstadoInscripcion estado;

  public InscripcionClaseUpdateDTO() {}

  public InscripcionClaseUpdateDTO(EstadoInscripcion estado) {
    this.estado = estado;
  }

  public EstadoInscripcion getEstado() {
    return estado;
  }

  public void setEstado(EstadoInscripcion estado) {
    this.estado = estado;
  }
}
