package gym.vitae.model.dtos.inventario;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class ProductoDetalleDTO {

  private Integer id;
  private String codigo;
  private String nombre;
  private String descripcion;
  private BigDecimal precioUnitario;
  private Integer stock;
  private String unidadMedida;
  private LocalDate fechaIngreso;
  private Boolean activo;
  private Instant createdAt;
  private Instant updatedAt;

  // Categoria info
  private Integer categoriaId;
  private String categoriaNombre;

  // Proveedor info
  private Integer proveedorId;
  private String proveedorNombre;

  public ProductoDetalleDTO() {}

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

  public LocalDate getFechaIngreso() {
    return fechaIngreso;
  }

  public void setFechaIngreso(LocalDate fechaIngreso) {
    this.fechaIngreso = fechaIngreso;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
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

  public Integer getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Integer categoriaId) {
    this.categoriaId = categoriaId;
  }

  public String getCategoriaNombre() {
    return categoriaNombre;
  }

  public void setCategoriaNombre(String categoriaNombre) {
    this.categoriaNombre = categoriaNombre;
  }

  public Integer getProveedorId() {
    return proveedorId;
  }

  public void setProveedorId(Integer proveedorId) {
    this.proveedorId = proveedorId;
  }

  public String getProveedorNombre() {
    return proveedorNombre;
  }

  public void setProveedorNombre(String proveedorNombre) {
    this.proveedorNombre = proveedorNombre;
  }
}
