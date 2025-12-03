package gym.vitae.model.dtos.membresias;

import java.math.BigDecimal;
import java.util.List;

public class TipoMembresiaCreateDTO {

  private String nombre;
  private String descripcion;
  private Integer duracionDias;
  private BigDecimal costo;
  private Boolean accesoCompleto;
  private List<Integer> beneficiosIds;

  public TipoMembresiaCreateDTO() {}

  public TipoMembresiaCreateDTO(
      String nombre,
      String descripcion,
      Integer duracionDias,
      BigDecimal costo,
      Boolean accesoCompleto,
      List<Integer> beneficiosIds) {
    this.nombre = nombre;
    this.descripcion = descripcion;
    this.duracionDias = duracionDias;
    this.costo = costo;
    this.accesoCompleto = accesoCompleto;
    this.beneficiosIds = beneficiosIds;
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

  public List<Integer> getBeneficiosIds() {
    return beneficiosIds;
  }

  public void setBeneficiosIds(List<Integer> beneficiosIds) {
    this.beneficiosIds = beneficiosIds;
  }
}
