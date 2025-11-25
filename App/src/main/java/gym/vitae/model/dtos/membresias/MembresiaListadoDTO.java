package gym.vitae.model.dtos.membresias;

import gym.vitae.model.enums.EstadoMembresia;
import java.math.BigDecimal;
import java.time.LocalDate;

public class MembresiaListadoDTO {

  private Integer id;
  private LocalDate fechaInicio;
  private LocalDate fechaFin;
  private BigDecimal precioPagado;
  private EstadoMembresia estado;
  private String clienteNombre;
  private String clienteDocumento;
  private String tipoMembresiaNombre;

  public MembresiaListadoDTO() {}

  public MembresiaListadoDTO(
      Integer id,
      LocalDate fechaInicio,
      LocalDate fechaFin,
      BigDecimal precioPagado,
      EstadoMembresia estado,
      String clienteNombre,
      String clienteDocumento,
      String tipoMembresiaNombre) {
    this.id = id;
    this.fechaInicio = fechaInicio;
    this.fechaFin = fechaFin;
    this.precioPagado = precioPagado;
    this.estado = estado;
    this.clienteNombre = clienteNombre;
    this.clienteDocumento = clienteDocumento;
    this.tipoMembresiaNombre = tipoMembresiaNombre;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public LocalDate getFechaInicio() {
    return fechaInicio;
  }

  public void setFechaInicio(LocalDate fechaInicio) {
    this.fechaInicio = fechaInicio;
  }

  public LocalDate getFechaFin() {
    return fechaFin;
  }

  public void setFechaFin(LocalDate fechaFin) {
    this.fechaFin = fechaFin;
  }

  public BigDecimal getPrecioPagado() {
    return precioPagado;
  }

  public void setPrecioPagado(BigDecimal precioPagado) {
    this.precioPagado = precioPagado;
  }

  public EstadoMembresia getEstado() {
    return estado;
  }

  public void setEstado(EstadoMembresia estado) {
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

  public String getTipoMembresiaNombre() {
    return tipoMembresiaNombre;
  }

  public void setTipoMembresiaNombre(String tipoMembresiaNombre) {
    this.tipoMembresiaNombre = tipoMembresiaNombre;
  }
}
