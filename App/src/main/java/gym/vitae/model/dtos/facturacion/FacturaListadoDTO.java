package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.EstadoFactura;
import gym.vitae.model.enums.TipoVenta;
import java.math.BigDecimal;
import java.time.LocalDate;

public class FacturaListadoDTO {

  private Integer id;
  private String numeroFactura;
  private LocalDate fechaEmision;
  private TipoVenta tipoVenta;
  private BigDecimal total;
  private EstadoFactura estado;
  private String clienteNombre;
  private String empleadoNombre;

  public FacturaListadoDTO() {}

  public FacturaListadoDTO(
      Integer id,
      String numeroFactura,
      LocalDate fechaEmision,
      TipoVenta tipoVenta,
      BigDecimal total,
      EstadoFactura estado,
      String clienteNombre,
      String empleadoNombre) {
    this.id = id;
    this.numeroFactura = numeroFactura;
    this.fechaEmision = fechaEmision;
    this.tipoVenta = tipoVenta;
    this.total = total;
    this.estado = estado;
    this.clienteNombre = clienteNombre;
    this.empleadoNombre = empleadoNombre;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getNumeroFactura() {
    return numeroFactura;
  }

  public void setNumeroFactura(String numeroFactura) {
    this.numeroFactura = numeroFactura;
  }

  public LocalDate getFechaEmision() {
    return fechaEmision;
  }

  public void setFechaEmision(LocalDate fechaEmision) {
    this.fechaEmision = fechaEmision;
  }

  public TipoVenta getTipoVenta() {
    return tipoVenta;
  }

  public void setTipoVenta(TipoVenta tipoVenta) {
    this.tipoVenta = tipoVenta;
  }

  public BigDecimal getTotal() {
    return total;
  }

  public void setTotal(BigDecimal total) {
    this.total = total;
  }

  public EstadoFactura getEstado() {
    return estado;
  }

  public void setEstado(EstadoFactura estado) {
    this.estado = estado;
  }

  public String getClienteNombre() {
    return clienteNombre;
  }

  public void setClienteNombre(String clienteNombre) {
    this.clienteNombre = clienteNombre;
  }

  public String getEmpleadoNombre() {
    return empleadoNombre;
  }

  public void setEmpleadoNombre(String empleadoNombre) {
    this.empleadoNombre = empleadoNombre;
  }
}
