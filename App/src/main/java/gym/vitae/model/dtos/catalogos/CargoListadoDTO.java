package gym.vitae.model.dtos.catalogos;

import java.math.BigDecimal;

public class CargoListadoDTO {

  private Integer id;
  private String nombre;
  private BigDecimal salarioBase;
  private Boolean activo;

  public CargoListadoDTO() {}

  public CargoListadoDTO(Integer id, String nombre, BigDecimal salarioBase, Boolean activo) {
    this.id = id;
    this.nombre = nombre;
    this.salarioBase = salarioBase;
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

  public BigDecimal getSalarioBase() {
    return salarioBase;
  }

  public void setSalarioBase(BigDecimal salarioBase) {
    this.salarioBase = salarioBase;
  }

  public Boolean getActivo() {
    return activo;
  }

  public void setActivo(Boolean activo) {
    this.activo = activo;
  }
}
