package gym.vitae.model.dtos.membresias;

import java.math.BigDecimal;

public class TipoMembresiaCreateDTO {

  private String nombre;
  private String descripcion;
  private Integer duracionDias;
  private BigDecimal costo;
  private Boolean accesoCompleto;

  public TipoMembresiaCreateDTO() {}

  public TipoMembresiaCreateDTO(
      String nombre,
      String descripcion,
      Integer duracionDias,
      BigDecimal costo,
      Boolean accesoCompleto) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.duracionDias = duracionDias;
    this.costo = costo;
    this.accesoCompleto = accesoCompleto;
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
}
