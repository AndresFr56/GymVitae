package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.TipoCategoria;

public class CategoriaListadoDTO {

  private Integer id;
  private String nombre;
  private TipoCategoria tipo;
  private String subtipo;
  private Boolean activo;

  public CategoriaListadoDTO() {}

  public CategoriaListadoDTO(
      Integer id, String nombre, TipoCategoria tipo, String subtipo, Boolean activo) {
    this.id = id;
    this.nombre = nombre;
    this.tipo = tipo;
    this.subtipo = subtipo;
    this.activo = activo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}
