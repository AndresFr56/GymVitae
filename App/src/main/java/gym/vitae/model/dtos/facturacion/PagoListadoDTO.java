package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.EstadoPago;
import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoListadoDTO {

  private Integer id;
  private BigDecimal monto;
  private LocalDate fechaPago;
  private String referencia;
  private EstadoPago estado;
  private Integer facturaId;
  private String facturaNumero;

  public PagoListadoDTO() {}

  public PagoListadoDTO(
      Integer id,
      BigDecimal monto,
      LocalDate fechaPago,
      String referencia,
      EstadoPago estado,
      Integer facturaId,
      String facturaNumero) {
    this.id = id;
    this.monto = monto;
    this.fechaPago = fechaPago;
    this.referencia = referencia;
    this.estado = estado;
    this.facturaId = facturaId;
    this.facturaNumero = facturaNumero;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public BigDecimal getMonto() {
    return monto;
  }

  public void setMonto(BigDecimal monto) {
    this.monto = monto;
  }

  public LocalDate getFechaPago() {
    return fechaPago;
  }

  public void setFechaPago(LocalDate fechaPago) {
    this.fechaPago = fechaPago;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public EstadoPago getEstado() {
    return estado;
  }

  public void setEstado(EstadoPago estado) {
    this.estado = estado;
  }

  public Integer getFacturaId() {
    return facturaId;
  }

  public void setFacturaId(Integer facturaId) {
    this.facturaId = facturaId;
  }

  public String getFacturaNumero() {
    return facturaNumero;
  }

  public void setFacturaNumero(String facturaNumero) {
    this.facturaNumero = facturaNumero;
  }
}
