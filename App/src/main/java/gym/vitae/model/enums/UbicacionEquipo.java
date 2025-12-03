package gym.vitae.model.enums;

/**
 * Enum que representa las ubicaciones disponibles para equipos del gimnasio. Según RF-22: Área de
 * cardio, Zona de peso libre, Bodega
 */
public enum UbicacionEquipo {
  AREA_CARDIO("Área de cardio"),
  ZONA_PESO_LIBRE("Zona de peso libre"),
  BODEGA("Bodega");

  private final String displayName;

  UbicacionEquipo(String displayName) {
    this.displayName = displayName;
  }

  /**
   * Obtiene el enum a partir del nombre para mostrar.
   *
   * @param displayName nombre para mostrar
   * @return UbicacionEquipo correspondiente o null si no existe
   */
  public static UbicacionEquipo fromDisplayName(String displayName) {
    for (UbicacionEquipo ubicacion : values()) {
      if (ubicacion.displayName.equalsIgnoreCase(displayName)) {
        return ubicacion;
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
