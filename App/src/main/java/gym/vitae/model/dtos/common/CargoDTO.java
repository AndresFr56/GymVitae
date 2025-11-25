package gym.vitae.model.dtos.common;

import java.math.BigDecimal;

/**
 * DTO básico para Cargo. Usado como referencia en otros DTOs para evitar cargar toda la entidad.
 *
 * @param id ID del cargo
 * @param nombre Nombre del cargo
 * @param salarioBase Salario base del cargo
 */
public record CargoDTO(Integer id, String nombre, BigDecimal salarioBase) {

  /**
   * Constructor simplificado sin salario.
   *
   * @param id ID del cargo
   * @param nombre Nombre del cargo
   */
  public CargoDTO(Integer id, String nombre) {
    this(id, nombre, null);
  }
}
