package gym.vitae.model.dtos.catalogos;

import java.math.BigDecimal;

public class CargoCreateDTO {

  private String nombre;
  private BigDecimal salarioBase;
  private String descripcion;

  public CargoCreateDTO() {}

  public CargoCreateDTO(String nombre, BigDecimal salarioBase, String descripcion) {
    this.nombre = nombre;
    this.salarioBase = salarioBase;
    this.descripcion = descripcion;
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

  public String getDescripcion() {
    return descripcion;
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }
}
