package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.TipoCategoria;

public class CategoriaCreateDTO {

  private String nombre;
  private TipoCategoria tipo;
  private String subtipo;
  private String descripcion;

  public CategoriaCreateDTO() {}

  public CategoriaCreateDTO(String nombre, TipoCategoria tipo, String subtipo, String descripcion) {
    this.nombre = nombre;
    this.tipo = tipo;
    this.subtipo = subtipo;
    this.descripcion = descripcion;
  }

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
}
