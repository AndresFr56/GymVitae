package gym.vitae.model.dtos.inventario;

import gym.vitae.model.enums.TipoCategoria;
import java.time.Instant;

public class CategoriaDetalleDTO {

  private Integer id;
  private String nombre;
  private TipoCategoria tipo;
  private String subtipo;
  private String descripcion;
  private Boolean activo;
  private Instant createdAt;
  private Instant updatedAt;

  public CategoriaDetalleDTO() {}

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
}
