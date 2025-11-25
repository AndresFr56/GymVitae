package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.EstadoFactura;

public class FacturaUpdateDTO {

  private EstadoFactura estado;
  private String observaciones;

  public FacturaUpdateDTO() {}

  public FacturaUpdateDTO(EstadoFactura estado, String observaciones) {
    this.estado = estado;
    this.observaciones = observaciones;
  }

  public EstadoFactura getEstado() {
    return estado;
  }

  public void setEstado(EstadoFactura estado) {
    this.estado = estado;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
