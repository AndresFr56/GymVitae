package gym.vitae.model.dtos.membresias;

import java.math.BigDecimal;
import java.time.Instant;

public class TipoMembresiaDetalleDTO {

  private Integer id;
  private String nombre;
  private String descripcion;
  private Integer duracionDias;
  private BigDecimal costo;
  private Boolean accesoCompleto;
  private Boolean activo;
  private Instant createdAt;
  private Instant updatedAt;

  public TipoMembresiaDetalleDTO() {}

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

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public Integer getDuracionDias() {
    return duracionDias;
  }

  public void setDuracionDias(Integer duracionDias) {
    this.duracionDias = duracionDias;
  }

  public BigDecimal getCosto() {
    return costo;
  }

  public void setCosto(BigDecimal costo) {
    this.costo = costo;
  }

  public Boolean getAccesoCompleto() {
    return accesoCompleto;
  }

  public void setAccesoCompleto(Boolean accesoCompleto) {
    this.accesoCompleto = accesoCompleto;
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
