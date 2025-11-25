package gym.vitae.model.dtos.facturacion;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PagoCreateDTO {

  private Integer facturaId;
  private BigDecimal monto;
  private LocalDate fechaPago;
  private String referencia;
  private String observaciones;

  public PagoCreateDTO() {}

  public PagoCreateDTO(
      Integer facturaId,
      BigDecimal monto,
      LocalDate fechaPago,
      String referencia,
      String observaciones) {
    this.facturaId = facturaId;
    this.monto = monto;
    this.fechaPago = fechaPago;
    this.referencia = referencia;
    this.observaciones = observaciones;
  }

  public Integer getFacturaId() {
    return facturaId;
  }

  public void setFacturaId(Integer facturaId) {
    this.facturaId = facturaId;
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

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
