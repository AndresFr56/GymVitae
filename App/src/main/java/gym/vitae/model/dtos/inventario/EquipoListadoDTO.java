package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.EstadoEquipo;
import java.time.LocalDate;

public class EquipoListadoDTO {

  private Integer id;
  private String codigo;
  private String nombre;
  private String marca;
  private String modelo;
  private EstadoEquipo estado;
  private String ubicacion;
  private String categoriaNombre;
  private String descripcion;
  private LocalDate fechaIngreso;

  public EquipoListadoDTO() {}

  public EquipoListadoDTO(
      Integer id,
      String codigo,
      String nombre,
      String marca,
      String modelo,
      EstadoEquipo estado,
      String ubicacion,
      String categoriaNombre,
      String descripcion,
      LocalDate fechaIngreso) {
    this.id = id;
    this.codigo = codigo;
    this.nombre = nombre;
    this.marca = marca;
    this.modelo = modelo;
    this.estado = estado;
    this.ubicacion = ubicacion;
    this.categoriaNombre = categoriaNombre;
    this.descripcion = descripcion;
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

  public EstadoEquipo getEstado() {
    return estado;
  }

  public void setEstado(EstadoEquipo estado) {
    this.estado = estado;
  }

  public String getUbicacion() {
    return ubicacion;
  }

  public void setUbicacion(String ubicacion) {
    this.ubicacion = ubicacion;
  }

  public String getCategoriaNombre() {
    return categoriaNombre;
  }

  public void setCategoriaNombre(String categoriaNombre) {
    this.categoriaNombre = categoriaNombre;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public LocalDate getFechaAdquisicion() {
    return fechaIngreso;
  }

  public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
    this.fechaIngreso = fechaIngreso;
  }
}
