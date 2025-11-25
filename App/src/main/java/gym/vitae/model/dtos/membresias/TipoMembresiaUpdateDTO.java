package gym.vitae.model.dtos.membresias;

import java.math.BigDecimal;

public class TipoMembresiaUpdateDTO {

  private String nombre;
  private String descripcion;
  private Integer duracionDias;
  private BigDecimal costo;
  private Boolean accesoCompleto;
  private Boolean activo;

  public TipoMembresiaUpdateDTO() {}

  public TipoMembresiaUpdateDTO(
      String nombre,
      String descripcion,
      Integer duracionDias,
      BigDecimal costo,
      Boolean accesoCompleto,
      Boolean activo) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.duracionDias = duracionDias;
    this.costo = costo;
    this.accesoCompleto = accesoCompleto;
    this.activo = activo;
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
}
