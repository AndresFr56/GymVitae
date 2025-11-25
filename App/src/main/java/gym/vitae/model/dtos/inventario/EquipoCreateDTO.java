package gym.vitae.model.dtos.inventario;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EquipoCreateDTO {

  private Integer categoriaId;
  private String codigo;
  private String nombre;
  private String descripcion;
  private String marca;
  private String modelo;
  private LocalDate fechaAdquisicion;
  private BigDecimal costo;
  private String ubicacion;
  private String observaciones;

  public EquipoCreateDTO() {}

  public EquipoCreateDTO(
      Integer categoriaId,
      String codigo,
      String nombre,
      String descripcion,
      String marca,
      String modelo,
      LocalDate fechaAdquisicion,
      BigDecimal costo,
      String ubicacion,
      String observaciones) {
    this.categoriaId = categoriaId;
    this.codigo = codigo;
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.marca = marca;
    this.modelo = modelo;
    this.fechaAdquisicion = fechaAdquisicion;
    this.costo = costo;
    this.ubicacion = ubicacion;
    this.observaciones = observaciones;
  }

  public Integer getCategoriaId() {
    return categoriaId;
  }

  public void setCategoriaId(Integer categoriaId) {
    this.categoriaId = categoriaId;
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

  public String getMarca() {
    return marca;
  }

  public void setMarca(String marca) {
    this.marca = marca;
  }

  public String getModelo() {
    return modelo;
  }

  public void setModelo(String modelo) {
    this.modelo = modelo;
  }

  public LocalDate getFechaAdquisicion() {
    return fechaAdquisicion;
  }

  public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
    this.fechaAdquisicion = fechaAdquisicion;
  }

  public BigDecimal getCosto() {
    return costo;
  }

  public void setCosto(BigDecimal costo) {
    this.costo = costo;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public String getObservaciones() {
    return observaciones;
  }

  public void setObservaciones(String observaciones) {
    this.observaciones = observaciones;
  }
}
