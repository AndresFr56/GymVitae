package gym.vitae.model.dtos.catalogos;

import java.math.BigDecimal;

public class IvaListadoDTO {

  private Integer id;
  private BigDecimal porcentaje;
  private Boolean activo;

  public IvaListadoDTO() {}

  public IvaListadoDTO(Integer id, BigDecimal porcentaje, Boolean activo) {
    this.id = id;
    this.porcentaje = porcentaje;
    this.activo = activo;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public BigDecimal getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(BigDecimal porcentaje) {
    this.porcentaje = porcentaje;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }

  /** Retorna el porcentaje formateado como "X%" */
  public String getPorcentajeFormateado() {
    return porcentaje + "%";
  }
}
