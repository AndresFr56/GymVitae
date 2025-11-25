package gym.vitae.model.dtos.inventario;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductoCreateDTO {

  private Integer categoriaId;
  private Integer proveedorId;
  private String codigo;
  private String nombre;
  private String descripcion;
  private BigDecimal precioUnitario;
  private Integer stock;
  private String unidadMedida;
  private LocalDate fechaIngreso;

  public ProductoCreateDTO() {}

  public ProductoCreateDTO(
      Integer categoriaId,
      Integer proveedorId,
      String codigo,
      String nombre,
      String descripcion,
      BigDecimal precioUnitario,
      Integer stock,
      String unidadMedida,
      LocalDate fechaIngreso) {
    this.categoriaId = categoriaId;
    this.proveedorId = proveedorId;
    this.codigo = codigo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precioUnitario = precioUnitario;
    this.stock = stock;
    this.unidadMedida = unidadMedida;
    this.fechaIngreso = fechaIngreso;
  }

  public Integer getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Integer categoriaId) {
    this.categoriaId = categoriaId;
  }

  public Integer getProveedorId() {
    return proveedorId;
  }

  public void setProveedorId(Integer proveedorId) {
    this.proveedorId = proveedorId;
  }

  public String getCodigo() {
    return codigo;
  }

  public void setCodigo(String codigo) {
    this.codigo = codigo;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public BigDecimal getPrecioUnitario() {
    return precioUnitario;
  }

  public void setPrecioUnitario(BigDecimal precioUnitario) {
    this.precioUnitario = precioUnitario;
  }

  public Integer getStock() {
    return stock;
  }

  public void setStock(Integer stock) {
    this.stock = stock;
  }

  public String getUnidadMedida() {
    return unidadMedida;
  }

  public void setUnidadMedida(String unidadMedida) {
    this.unidadMedida = unidadMedida;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }
}
