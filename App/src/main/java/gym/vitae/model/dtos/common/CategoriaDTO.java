package gym.vitae.model.dtos.common;

import gym.vitae.model.enums.TipoCategoria;

/**
 * DTO básico para Categoria. Usado como referencia en DTOs de Producto y Equipo.
 *
 * @param id ID de la categoría
 * @param nombre Nombre de la categoría
 * @param tipo Tipo de categoría (PRODUCTO, EQUIPO, CLASE)
 * @param subtipo Subtipo opcional de la categoría
 */
public record CategoriaDTO(Integer id, String nombre, TipoCategoria tipo, String subtipo) {

  /**
   * Constructor simplificado solo con id y nombre.
   *
   * @param id ID de la categoría
   * @param nombre Nombre de la categoría
   */
  public CategoriaDTO(Integer id, String nombre) {
    this(id, nombre, null, null);
  }
}
