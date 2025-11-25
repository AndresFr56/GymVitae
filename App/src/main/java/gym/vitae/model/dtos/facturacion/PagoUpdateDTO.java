package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.EstadoPago;

public class PagoUpdateDTO {

  private EstadoPago estado;
  private String referencia;
  private String observaciones;

  public PagoUpdateDTO() {}

  public PagoUpdateDTO(EstadoPago estado, String referencia, String observaciones) {
    this.estado = estado;
    this.referencia = referencia;
    this.observaciones = observaciones;
  }

  public EstadoPago getEstado() {
    return estado;
  }

  public void setEstado(EstadoPago estado) {
    this.estado = estado;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
