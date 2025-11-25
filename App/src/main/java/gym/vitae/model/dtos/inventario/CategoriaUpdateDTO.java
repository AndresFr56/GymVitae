package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.TipoCategoria;

public class CategoriaUpdateDTO {

  private String nombre;
  private TipoCategoria tipo;
  private String subtipo;
  private String descripcion;
  private Boolean activo;

  public CategoriaUpdateDTO() {}

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public TipoCategoria getTipo() {
    return tipo;
  }

  public void setTipo(TipoCategoria tipo) {
    this.tipo = tipo;
  }

  public String getSubtipo() {
    return subtipo;
  }

  public void setSubtipo(String subtipo) {
    this.subtipo = subtipo;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}
