package gym.vitae.model.dtos.catalogos;

import java.math.BigDecimal;

public class IvaCreateUpdateDTO {

  private BigDecimal porcentaje;
  private Boolean activo;

  public IvaCreateUpdateDTO() {}

  public IvaCreateUpdateDTO(BigDecimal porcentaje, Boolean activo) {
    this.porcentaje = porcentaje;
    this.activo = activo;
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
}
