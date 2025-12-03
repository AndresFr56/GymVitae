package gym.vitae.model.enums;

/**
 * Enum que representa las unidades de medida disponibles para productos del inventario. Según
 * RF-19: Unidad, Kg, Litro, Caja, Paquete
 */
public enum UnidadMedida {
  UNIDAD("Unidad"),
  KG("Kg"),
  LITRO("Litro"),
  CAJA("Caja"),
  PAQUETE("Paquete");

  private final String displayName;

  UnidadMedida(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Obtiene el enum a partir del nombre para mostrar.
   *
   * @param displayName nombre para mostrar
   * @return UnidadMedida correspondiente o null si no existe
   */
  public static UnidadMedida fromDisplayName(String displayName) {
    for (UnidadMedida unidad : values()) {
      if (unidad.displayName.equalsIgnoreCase(displayName)) {
        return unidad;
      }
    }
    return null;
  }

  public String getDisplayName() {
    return displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}
