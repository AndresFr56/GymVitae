package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.TipoVenta;
import java.util.List;

public class FacturaCreateDTO {

  private Integer clienteId;
  private Integer empleadoId;
  private TipoVenta tipoVenta;
  private String observaciones;
  private List<DetalleFacturaCreateDTO> detalles;

  public FacturaCreateDTO() {}

  public FacturaCreateDTO(
      Integer clienteId,
      Integer empleadoId,
      TipoVenta tipoVenta,
      String observaciones,
      List<DetalleFacturaCreateDTO> detalles) {
    this.clienteId = clienteId;
    this.empleadoId = empleadoId;
    this.tipoVenta = tipoVenta;
    this.observaciones = observaciones;
    this.detalles = detalles;
  }

  public Integer getClienteId() {
    return clienteId;
  }

  public void setClienteId(Integer clienteId) {
    this.clienteId = clienteId;
  }

  public Integer getEmpleadoId() {
    return empleadoId;
  }

  public void setEmpleadoId(Integer empleadoId) {
    this.empleadoId = empleadoId;
  }

  public TipoVenta getTipoVenta() {
    return tipoVenta;
  }

  public void setTipoVenta(TipoVenta tipoVenta) {
    this.tipoVenta = tipoVenta;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }

  public List<DetalleFacturaCreateDTO> getDetalles() {
    return detalles;
  }

  public void setDetalles(List<DetalleFacturaCreateDTO> detalles) {
    this.detalles = detalles;
  }
}
