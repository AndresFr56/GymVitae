package gym.vitae.model.dtos.inventario;

import java.math.BigDecimal;
import java.time.LocalDate;

public class ProductoListadoDTO {

  private Integer id;
  private String codigo;
  private String nombre;
  private String descripcion;
  private BigDecimal precioUnitario;
  private Integer stock;
  private String unidadMedida;
  private Boolean activo;
  private String categoriaNombre;
  private String proveedorNombre;
  private LocalDate fechaIngreso;

  public ProductoListadoDTO() {}

  public ProductoListadoDTO(
      Integer id,
      String codigo,
      String nombre,
      String descripcion,
      BigDecimal precioUnitario,
      Integer stock,
      String unidadMedida,
      Boolean activo,
      String categoriaNombre,
      String proveedorNombre,
      LocalDate fechaIngreso) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.precioUnitario = precioUnitario;
    this.stock = stock;
    this.unidadMedida = unidadMedida;
    this.activo = activo;
    this.categoriaNombre = categoriaNombre;
    this.proveedorNombre = proveedorNombre;
    this.fechaIngreso = fechaIngreso;
  }

  // Getters y setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  public String getCategoriaNombre() {
    return categoriaNombre;
  }

  public void setCategoriaNombre(String categoriaNombre) {
    this.categoriaNombre = categoriaNombre;
  }

  public String getProveedorNombre() {
    return proveedorNombre;
  }

  public void setProveedorNombre(String proveedorNombre) {
    this.proveedorNombre = proveedorNombre;
  }

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }
}
