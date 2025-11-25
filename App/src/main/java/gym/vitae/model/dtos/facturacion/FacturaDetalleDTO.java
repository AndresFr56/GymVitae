package gym.vitae.model.dtos.facturacion;

import gym.vitae.model.enums.EstadoFactura;
import gym.vitae.model.enums.TipoVenta;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

public class FacturaDetalleDTO {

  private Integer id;
  private String numeroFactura;
  private LocalDate fechaEmision;
  private TipoVenta tipoVenta;
  private BigDecimal total;
  private EstadoFactura estado;
  private String observaciones;
  private Instant createdAt;
  private Instant updatedAt;

  // Cliente info
  private Integer clienteId;
  private String clienteNombre;
  private String clienteDocumento;

  // Empleado info
  private Integer empleadoId;
  private String empleadoNombre;

  // Detalles de la factura
  private List<DetalleFacturaDTO> detalles;

  public FacturaDetalleDTO() {}

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

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
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

  public Integer getClienteId() {
    return clienteId;
  }

  public void setClienteId(Integer clienteId) {
    this.clienteId = clienteId;
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

  public Integer getEmpleadoId() {
    return empleadoId;
  }

  public void setEmpleadoId(Integer empleadoId) {
    this.empleadoId = empleadoId;
  }

  public String getEmpleadoNombre() {
    return empleadoNombre;
  }

  public void setEmpleadoNombre(String empleadoNombre) {
    this.empleadoNombre = empleadoNombre;
  }

  public List<DetalleFacturaDTO> getDetalles() {
    return detalles;
  }

  public void setDetalles(List<DetalleFacturaDTO> detalles) {
    this.detalles = detalles;
  }
}
